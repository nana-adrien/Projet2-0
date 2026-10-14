package empire.digiprem.projet2_0.feature.matchendirect.mapper

import empire.digiprem.projet2_0.feature.calendrier.mapper.toDomain
import empire.digiprem.projet2_0.feature.matchendirect.dto.CompositionDto
import empire.digiprem.projet2_0.feature.matchendirect.dto.EvenementDto
import empire.digiprem.projet2_0.feature.matchendirect.dto.MatchEnDirectDto
import empire.digiprem.projet2_0.feature.matchendirect.model.Composition
import empire.digiprem.projet2_0.feature.matchendirect.model.Evenement
import empire.digiprem.projet2_0.feature.matchendirect.model.MatchEnDirect
import empire.digiprem.projet2_0.feature.matchendirect.model.TypeEvenement

fun CompositionDto.toDomain() = Composition(
    id = id,
    matchId = matchId,
    utilisateurId = utilisateurId,
    pseudo = pseudo,
    titulaire = titulaire,
    present = present,
)

fun EvenementDto.toDomain() = Evenement(
    id = id,
    matchId = matchId,
    type = runCatching { TypeEvenement.valueOf(type) }.getOrDefault(TypeEvenement.DEBUT_PERIODE),
    minute = minute,
    periode = periode,
    campId = campId,
    joueurId = joueurId,
    joueurPseudo = joueurPseudo,
    joueurSecondaireId = joueurSecondaireId,
    joueurSecondairePseudo = joueurSecondairePseudo,
    auteurSaisieId = auteurSaisieId,
    horodatage = horodatage,
    annule = annule,
)

fun MatchEnDirectDto.toDomain() = MatchEnDirect(
    match = match.toDomain(),
    evenements = evenements.map { it.toDomain() },
)
