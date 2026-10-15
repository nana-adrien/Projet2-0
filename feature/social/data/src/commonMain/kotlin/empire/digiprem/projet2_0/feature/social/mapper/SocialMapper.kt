package empire.digiprem.projet2_0.feature.social.mapper

import empire.digiprem.projet2_0.feature.social.dto.SignalementDto
import empire.digiprem.projet2_0.feature.social.model.Signalement
import empire.digiprem.projet2_0.feature.social.model.TypeContenuSignale

fun SignalementDto.toDomain() = Signalement(
    id = id,
    contenuType = runCatching { TypeContenuSignale.valueOf(contenuType) }.getOrDefault(TypeContenuSignale.PROFIL_UTILISATEUR),
    contenuId = contenuId,
    motif = motif,
)
