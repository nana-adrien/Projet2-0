package empire.digiprem.projet2_0.feature.calendrier.mapper

import empire.digiprem.projet2_0.feature.calendrier.dto.ConvocationDto
import empire.digiprem.projet2_0.feature.calendrier.dto.MatchDto
import empire.digiprem.projet2_0.feature.calendrier.dto.PresencesCampDto
import empire.digiprem.projet2_0.feature.calendrier.dto.PresencesDto
import empire.digiprem.projet2_0.feature.calendrier.model.Convocation
import empire.digiprem.projet2_0.feature.calendrier.model.Match
import empire.digiprem.projet2_0.feature.calendrier.model.Presences
import empire.digiprem.projet2_0.feature.calendrier.model.PresencesCamp
import empire.digiprem.projet2_0.feature.calendrier.model.ReponseConvocation

fun MatchDto.toDomain() = Match(
    id = id,
    saisonId = saisonId,
    type = type,
    date = date,
    heure = heure,
    lieu = lieu,
    statut = statut,
    scoreCampA = scoreCampA,
    scoreCampB = scoreCampB,
    dureeMiTempsAppliqueeMinutes = dureeMiTempsAppliqueeMinutes,
    nomEquipeInvitee = nomEquipeInvitee,
    marqueurId = marqueurId,
)

fun ConvocationDto.toDomain() = Convocation(
    id = id,
    matchId = matchId,
    utilisateurId = utilisateurId,
    pseudo = pseudo,
    reponse = runCatching { ReponseConvocation.valueOf(reponse) }.getOrDefault(ReponseConvocation.SANS_REPONSE),
    dateReponse = dateReponse,
)

fun PresencesCampDto.toDomain() = PresencesCamp(
    campId = campId,
    libelle = libelle,
    presents = presents,
    absents = absents,
    incertains = incertains,
    sansReponse = sansReponse,
)

fun PresencesDto.toDomain() = Presences(
    matchId = matchId,
    camps = camps.map { it.toDomain() },
)
