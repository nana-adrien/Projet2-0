package empire.digiprem.projet2_0.core.data.mappers

import empire.digiprem.projet2_0.core.data.dto.AuthSessionSerializable
import empire.digiprem.projet2_0.core.domain.model.AuthSession

fun AuthSessionSerializable.toDomain(): AuthSession = AuthSession(
    accessToken  = accessToken,
    refreshToken = refreshToken,
    userId       = userId,
    pseudo       = pseudo,
)

fun AuthSession.toSerializable(): AuthSessionSerializable = AuthSessionSerializable(
    accessToken  = accessToken,
    refreshToken = refreshToken,
    userId       = userId,
    pseudo       = pseudo,
)
