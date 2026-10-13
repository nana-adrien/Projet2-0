package empire.digiprem.projet2_0.feature.auth.mapper

import empire.digiprem.projet2_0.core.domain.model.AuthSession
import empire.digiprem.projet2_0.feature.auth.dto.AuthResponse

fun AuthResponse.toDomain() = AuthSession(
    accessToken  = accessToken,
    refreshToken = refreshToken,
    userId       = userId,
    pseudo       = pseudo,
)
