package empire.digiprem.projet2_0.server.user.service

import empire.digiprem.projet2_0.server.common.exception.InvalidOtpException
import empire.digiprem.projet2_0.server.common.exception.OtpLockedException
import empire.digiprem.projet2_0.server.user.infra.entity.OtpCodeEntity
import empire.digiprem.projet2_0.server.user.infra.repository.OtpCodeRepository
import jakarta.transaction.Transactional
import org.slf4j.LoggerFactory
import org.springframework.mail.javamail.JavaMailSender
import org.springframework.mail.javamail.MimeMessageHelper
import org.springframework.stereotype.Service
import java.security.MessageDigest
import java.time.Duration
import java.time.Instant
import java.util.Base64
import kotlin.random.Random

enum class OtpPurpose { SIGNUP, LOGIN, PASSWORD_RESET }

private const val CODE_LENGTH = 6
private val CODE_EXPIRY: Duration = Duration.ofMinutes(5)
private val LOCKOUT_DURATION: Duration = Duration.ofMinutes(15)
private const val MAX_ATTEMPTS = 5

/**
 * Mécanisme OTP unique pour CU1 (inscription), CU2-A1 (connexion sans mot de passe)
 * et CU3 (réinitialisation de mot de passe) — "contact" est un numéro de téléphone ou un e-mail.
 */
@Service
class OtpService(
    private val otpCodeRepository: OtpCodeRepository,
    private val mailSender: JavaMailSender?,
) {
    private val logger = LoggerFactory.getLogger(javaClass)

    @Transactional
    fun requestCode(contact: String, purpose: OtpPurpose) {
        otpCodeRepository.deleteByContactAndPurpose(contact, purpose.name)
        val code = Random.nextInt(0, 1_000_000).toString().padStart(CODE_LENGTH, '0')
        otpCodeRepository.save(
            OtpCodeEntity(
                contact = contact,
                purpose = purpose.name,
                hashedCode = hash(code),
                expiresAt = Instant.now().plus(CODE_EXPIRY),
            )
        )
        send(contact, code)
    }

    /** Valide le code et consomme l'entrée OTP. Lève une exception si invalide/expiré/verrouillé. */
    @Transactional
    fun verifyCode(contact: String, purpose: OtpPurpose, code: String) {
        val record = otpCodeRepository.findByContactAndPurpose(contact, purpose.name)
            ?: throw InvalidOtpException()

        record.lockedUntil?.let { lockedUntil ->
            if (lockedUntil.isAfter(Instant.now())) throw OtpLockedException()
        }

        if (record.expiresAt.isBefore(Instant.now())) {
            otpCodeRepository.delete(record)
            throw InvalidOtpException()
        }

        if (hash(code) != record.hashedCode) {
            val attempts = record.attempts + 1
            val lockedUntil = if (attempts >= MAX_ATTEMPTS) Instant.now().plus(LOCKOUT_DURATION) else null
            otpCodeRepository.save(
                OtpCodeEntity(
                    id = record.id,
                    contact = record.contact,
                    purpose = record.purpose,
                    hashedCode = record.hashedCode,
                    attempts = attempts,
                    expiresAt = record.expiresAt,
                    lockedUntil = lockedUntil,
                    createdAt = record.createdAt,
                )
            )
            if (lockedUntil != null) throw OtpLockedException()
            throw InvalidOtpException()
        }

        otpCodeRepository.delete(record)
    }

    private fun send(contact: String, code: String) {
        val isEmail = contact.contains("@")
        if (isEmail && mailSender != null) {
            try {
                val message = mailSender.createMimeMessage()
                val helper = MimeMessageHelper(message, false, "UTF-8")
                helper.setTo(contact)
                helper.setSubject("Ton code 2-0")
                helper.setText("Ton code de vérification : $code\n\nIl expire dans 5 minutes.", false)
                mailSender.send(message)
            } catch (e: Exception) {
                logger.warn("Échec d'envoi du code OTP par e-mail à $contact (${e.message}) — code : $code")
            }
        } else {
            // SMS : prestataire à brancher plus tard (décision ouverte, cf. plan). En dev, on journalise.
            logger.info("Code OTP pour $contact ($isEmail) : $code")
        }
    }

    private fun hash(code: String): String {
        val digest = MessageDigest.getInstance("SHA-256")
        return Base64.getEncoder().encodeToString(digest.digest(code.encodeToByteArray()))
    }
}
