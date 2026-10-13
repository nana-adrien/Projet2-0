package empire.digiprem.projet2_0.server.user.service

import empire.digiprem.projet2_0.contracts.auth.AuthResponse
import empire.digiprem.projet2_0.contracts.auth.LoginRequest
import empire.digiprem.projet2_0.contracts.auth.LoginWithOtpRequest
import empire.digiprem.projet2_0.contracts.auth.RegisterRequest
import empire.digiprem.projet2_0.contracts.auth.RequestOtpRequest
import empire.digiprem.projet2_0.contracts.auth.ResetPasswordRequest
import empire.digiprem.projet2_0.contracts.auth.VerifyOtpRequest
import empire.digiprem.projet2_0.contracts.auth.VerifyOtpResponse
import empire.digiprem.projet2_0.contracts.validation.validateLoginRequest
import empire.digiprem.projet2_0.contracts.validation.validateLoginWithOtpRequest
import empire.digiprem.projet2_0.contracts.validation.validateRegisterRequest
import empire.digiprem.projet2_0.contracts.validation.validateRequestOtpRequest
import empire.digiprem.projet2_0.contracts.validation.validateResetPasswordRequest
import empire.digiprem.projet2_0.contracts.validation.validateVerifyOtpRequest
import empire.digiprem.projet2_0.server.common.event.UserRegisteredEvent
import empire.digiprem.projet2_0.server.common.exception.InvalidCredentialsException
import empire.digiprem.projet2_0.server.common.exception.InvalidTokenException
import empire.digiprem.projet2_0.server.common.exception.UserAlreadyExistsException
import empire.digiprem.projet2_0.server.common.exception.UserNotFoundException
import empire.digiprem.projet2_0.server.common.security.JwtService
import empire.digiprem.projet2_0.server.common.security.PasswordEncoder
import empire.digiprem.projet2_0.server.common.util.orThrow
import empire.digiprem.projet2_0.server.user.infra.entity.RefreshTokenEntity
import empire.digiprem.projet2_0.server.user.infra.entity.UserEntity
import empire.digiprem.projet2_0.server.user.infra.repository.RefreshTokenRepository
import empire.digiprem.projet2_0.server.user.infra.repository.UserRepository
import jakarta.transaction.Transactional
import org.springframework.context.ApplicationEventPublisher
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import java.security.MessageDigest
import java.time.Instant
import java.util.Base64
import java.util.UUID

@Service
class AuthService(
    private val userRepository: UserRepository,
    private val refreshTokenRepository: RefreshTokenRepository,
    private val passwordEncoder: PasswordEncoder,
    private val jwtService: JwtService,
    private val otpService: OtpService,
    private val eventPublisher: ApplicationEventPublisher,
) {

    fun requestOtp(request: RequestOtpRequest) {
        validateRequestOtpRequest(request).orThrow()
        val purpose = request.purpose.toOtpPurpose()
        val contact = request.contact.trim()
        val existing = userRepository.findByTelephoneOrEmail(contact, contact)?.takeUnless { it.supprime }

        when (purpose) {
            OtpPurpose.SIGNUP -> {
                if (existing != null) throw UserAlreadyExistsException()
                otpService.requestCode(contact, purpose)
            }
            OtpPurpose.LOGIN, OtpPurpose.PASSWORD_RESET -> {
                // Contact inconnu -> on ne révèle rien (RG sécurité, cf. CU3-E1), pas d'erreur ni d'envoi.
                if (existing != null) otpService.requestCode(contact, purpose)
            }
        }
    }

    fun verifyOtp(request: VerifyOtpRequest): VerifyOtpResponse {
        validateVerifyOtpRequest(request).orThrow()
        val purpose = request.purpose.toOtpPurpose()
        val contact = request.contact.trim()
        otpService.verifyCode(contact, purpose, request.code)
        return VerifyOtpResponse(jwtService.generateVerificationToken(contact, purpose.name))
    }

    @Transactional
    fun register(request: RegisterRequest): AuthResponse {
        validateRegisterRequest(request).orThrow()
        val contact = request.contact.trim()
        val pseudo = request.pseudo.trim()

        if (!jwtService.validateVerificationToken(request.verificationToken, contact, OtpPurpose.SIGNUP.name)) {
            throw InvalidTokenException("Verification token is invalid or has expired")
        }
        if (userRepository.findByPseudoOrTelephoneOrEmail(pseudo, contact, contact) != null) {
            throw UserAlreadyExistsException()
        }

        val isEmail = contact.contains("@")
        val savedUser = userRepository.save(
            UserEntity(
                pseudo = pseudo,
                telephone = contact.takeUnless { isEmail },
                email = contact.takeIf { isEmail },
                hashedPassword = request.password?.let { passwordEncoder.encode(it) },
                isVerified = true,
            )
        )
        val userId = savedUser.id ?: throw UserNotFoundException()
        eventPublisher.publishEvent(UserRegisteredEvent(userId, request.clubCode?.trim()?.takeIf { it.isNotBlank() }))

        return issueTokens(userId, savedUser.pseudo)
    }

    fun login(request: LoginRequest): AuthResponse {
        validateLoginRequest(request).orThrow()
        val contact = request.contact.trim()
        val user = userRepository.findByTelephoneOrEmail(contact, contact)?.takeUnless { it.supprime }
            ?: throw InvalidCredentialsException()

        val hashedPassword = user.hashedPassword ?: throw InvalidCredentialsException()
        if (!passwordEncoder.matches(request.password, hashedPassword)) {
            throw InvalidCredentialsException()
        }

        val userId = user.id ?: throw UserNotFoundException()
        return issueTokens(userId, user.pseudo)
    }

    @Transactional
    fun loginWithOtp(request: LoginWithOtpRequest): AuthResponse {
        validateLoginWithOtpRequest(request).orThrow()
        val contact = request.contact.trim()
        if (!jwtService.validateVerificationToken(request.verificationToken, contact, OtpPurpose.LOGIN.name)) {
            throw InvalidTokenException("Verification token is invalid or has expired")
        }
        val user = userRepository.findByTelephoneOrEmail(contact, contact)?.takeUnless { it.supprime }
            ?: throw UserNotFoundException()

        val userId = user.id ?: throw UserNotFoundException()
        return issueTokens(userId, user.pseudo)
    }

    @Transactional
    fun resetPassword(request: ResetPasswordRequest) {
        validateResetPasswordRequest(request).orThrow()
        val contact = request.contact.trim()
        if (!jwtService.validateVerificationToken(request.verificationToken, contact, OtpPurpose.PASSWORD_RESET.name)) {
            throw InvalidTokenException("Verification token is invalid or has expired")
        }
        val user = userRepository.findByTelephoneOrEmail(contact, contact)?.takeUnless { it.supprime }
            ?: throw UserNotFoundException()

        userRepository.save(user.copyWith(hashedPassword = passwordEncoder.encode(request.newPassword)))
    }

    @Transactional
    fun refresh(refreshToken: String): AuthResponse {
        if (!jwtService.validateRefreshToken(refreshToken)) {
            throw InvalidTokenException("Invalid refresh token")
        }
        val userId = UUID.fromString(jwtService.getUserIdFromToken(refreshToken))
        val user = userRepository.findByIdOrNull(userId) ?: throw UserNotFoundException()

        val hashed = hashToken(refreshToken)
        refreshTokenRepository.findByUserIdAndHashedToken(userId, hashed)
            ?: throw InvalidTokenException("Refresh token not found or already used")
        refreshTokenRepository.deleteByUserIdAndHashedToken(userId, hashed)

        return issueTokens(userId, user.pseudo)
    }

    @Transactional
    fun logout(refreshToken: String) {
        runCatching {
            val userId = UUID.fromString(jwtService.getUserIdFromToken(refreshToken))
            refreshTokenRepository.deleteByUserIdAndHashedToken(userId, hashToken(refreshToken))
        }
    }

    private fun issueTokens(userId: UUID, pseudo: String): AuthResponse {
        val accessToken = jwtService.generateAccessToken(userId.toString())
        val refreshToken = jwtService.generateRefreshToken(userId.toString())
        storeRefreshToken(userId, refreshToken)
        return AuthResponse(accessToken = accessToken, refreshToken = refreshToken, userId = userId.toString(), pseudo = pseudo)
    }

    private fun storeRefreshToken(userId: UUID, token: String) {
        refreshTokenRepository.save(
            RefreshTokenEntity(
                userId = userId,
                hashedToken = hashToken(token),
                expiresAt = Instant.now().plusMillis(jwtService.refreshTokenExpiryMsValue),
            )
        )
    }

    private fun hashToken(token: String): String {
        val digest = MessageDigest.getInstance("SHA-256")
        return Base64.getEncoder().encodeToString(digest.digest(token.encodeToByteArray()))
    }
}

private fun String.toOtpPurpose(): OtpPurpose = try {
    OtpPurpose.valueOf(this)
} catch (e: IllegalArgumentException) {
    throw empire.digiprem.projet2_0.server.common.exception.ValidationException(listOf("purpose: must be one of ${OtpPurpose.entries.joinToString()}"))
}

private fun UserEntity.copyWith(hashedPassword: String): UserEntity = UserEntity(
    id = id,
    pseudo = pseudo,
    telephone = telephone,
    email = email,
    hashedPassword = hashedPassword,
    nom = nom,
    prenom = prenom,
    photo = photo,
    dateNaissance = dateNaissance,
    postePrefere = postePrefere,
    piedFort = piedFort,
    cacherNomReel = cacherNomReel,
    cacherAge = cacherAge,
    isVerified = isVerified,
    supprime = supprime,
    createdAt = createdAt,
)
