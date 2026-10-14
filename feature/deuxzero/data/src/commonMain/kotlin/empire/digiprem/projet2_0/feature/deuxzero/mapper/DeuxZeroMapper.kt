package empire.digiprem.projet2_0.feature.deuxzero.mapper

import empire.digiprem.projet2_0.feature.deuxzero.dto.CampDto
import empire.digiprem.projet2_0.feature.deuxzero.dto.DeuxZeroDto
import empire.digiprem.projet2_0.feature.deuxzero.dto.EffectifDto
import empire.digiprem.projet2_0.feature.deuxzero.dto.SaisonDto
import empire.digiprem.projet2_0.feature.deuxzero.model.Camp
import empire.digiprem.projet2_0.feature.deuxzero.model.DeuxZero
import empire.digiprem.projet2_0.feature.deuxzero.model.DeuxZeroVisibilite
import empire.digiprem.projet2_0.feature.deuxzero.model.Effectif
import empire.digiprem.projet2_0.feature.deuxzero.model.Saison
import empire.digiprem.projet2_0.feature.deuxzero.model.SaisonStatut

fun DeuxZeroDto.toDomain() = DeuxZero(
    id = id,
    nom = nom,
    lieu = lieu,
    visibilite = runCatching { DeuxZeroVisibilite.valueOf(visibilite) }.getOrDefault(DeuxZeroVisibilite.PRIVEE),
    estInterne = estInterne,
    joursHabituels = joursHabituels,
    heureHabituelle = heureHabituelle,
    dureeMiTempsMinutes = dureeMiTempsMinutes,
    nombreMiTemps = nombreMiTemps,
    dureePauseMinutes = dureePauseMinutes,
    nombreMaxJoueursParCamp = nombreMaxJoueursParCamp,
    delaiValidationAutomatiqueHeures = delaiValidationAutomatiqueHeures,
    transfertsAutorisesParSaison = transfertsAutorisesParSaison,
    statut = statut,
    organisateurId = organisateurId,
)

fun CampDto.toDomain() = Camp(
    id = id,
    deuxZeroId = deuxZeroId,
    clubId = clubId,
    clubNom = clubNom,
    libelle = libelle,
    codeEngagement = codeEngagement,
    capitaineId = capitaineId,
    capitainePseudo = capitainePseudo,
    viceCapitaineId = viceCapitaineId,
    viceCapitainePseudo = viceCapitainePseudo,
)

fun EffectifDto.toDomain() = Effectif(
    id = id,
    campId = campId,
    utilisateurId = utilisateurId,
    pseudo = pseudo,
    numeroMaillot = numeroMaillot,
    dateEntree = dateEntree,
    dateSortie = dateSortie,
)

fun SaisonDto.toDomain() = Saison(
    id = id,
    deuxZeroId = deuxZeroId,
    libelle = libelle,
    dateDebut = dateDebut,
    dateFin = dateFin,
    statut = runCatching { SaisonStatut.valueOf(statut) }.getOrDefault(SaisonStatut.EN_COURS),
    campChampionId = campChampionId,
)
