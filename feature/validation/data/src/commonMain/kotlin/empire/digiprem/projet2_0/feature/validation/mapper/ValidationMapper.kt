package empire.digiprem.projet2_0.feature.validation.mapper

import empire.digiprem.projet2_0.feature.calendrier.mapper.toDomain
import empire.digiprem.projet2_0.feature.matchendirect.mapper.toDomain
import empire.digiprem.projet2_0.feature.validation.dto.ResultatMatchDto
import empire.digiprem.projet2_0.feature.validation.dto.ValidationDto
import empire.digiprem.projet2_0.feature.validation.model.DecisionValidation
import empire.digiprem.projet2_0.feature.validation.model.ResultatMatch
import empire.digiprem.projet2_0.feature.validation.model.Validation

fun ValidationDto.toDomain() = Validation(
    id = id,
    matchId = matchId,
    campId = campId,
    signataireId = signataireId,
    signatairePseudo = signatairePseudo,
    decision = runCatching { DecisionValidation.valueOf(decision) }.getOrDefault(DecisionValidation.CONTESTE),
    commentaire = commentaire,
    correctionProposee = correctionProposee,
    horodatage = horodatage,
)

fun ResultatMatchDto.toDomain() = ResultatMatch(
    match = match.toDomain(),
    evenements = evenements.map { it.toDomain() },
    validations = validations.map { it.toDomain() },
)
