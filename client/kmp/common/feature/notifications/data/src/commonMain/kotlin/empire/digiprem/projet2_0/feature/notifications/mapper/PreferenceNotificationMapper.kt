package empire.digiprem.projet2_0.feature.notifications.mapper

import empire.digiprem.projet2_0.feature.notifications.dto.PreferenceNotificationDto
import empire.digiprem.projet2_0.feature.notifications.model.NiveauNotification
import empire.digiprem.projet2_0.feature.notifications.model.PreferenceNotification

fun PreferenceNotificationDto.toDomain() = PreferenceNotification(
    id = id,
    deuxZeroId = deuxZeroId,
    niveau = runCatching { NiveauNotification.valueOf(niveau) }.getOrDefault(NiveauNotification.TOUT),
    neRienDerangerNocturne = neRienDerangerNocturne,
)
