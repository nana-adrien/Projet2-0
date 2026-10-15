package empire.digiprem.projet2_0.feature.statistiques.mapper

import empire.digiprem.projet2_0.feature.calendrier.mapper.toDomain
import empire.digiprem.projet2_0.feature.matchendirect.mapper.toDomain
import empire.digiprem.projet2_0.feature.statistiques.dto.BadgeObtenuDto
import empire.digiprem.projet2_0.feature.statistiques.dto.BilanSaisonDto
import empire.digiprem.projet2_0.feature.statistiques.dto.CandidatDto
import empire.digiprem.projet2_0.feature.statistiques.dto.LigneClassementDto
import empire.digiprem.projet2_0.feature.statistiques.dto.MoisResultatDto
import empire.digiprem.projet2_0.feature.statistiques.dto.MoisValeurDto
import empire.digiprem.projet2_0.feature.statistiques.dto.ProfilJoueurDto
import empire.digiprem.projet2_0.feature.statistiques.dto.RecompenseJoueurDto
import empire.digiprem.projet2_0.feature.statistiques.dto.SerieDto
import empire.digiprem.projet2_0.feature.statistiques.dto.TableauSaisonDto
import empire.digiprem.projet2_0.feature.statistiques.dto.VictoireRecordDto
import empire.digiprem.projet2_0.feature.statistiques.dto.VoteHommeDuMatchDto
import empire.digiprem.projet2_0.feature.statistiques.model.BadgeObtenu
import empire.digiprem.projet2_0.feature.statistiques.model.BilanSaison
import empire.digiprem.projet2_0.feature.statistiques.model.Candidat
import empire.digiprem.projet2_0.feature.statistiques.model.ContexteStats
import empire.digiprem.projet2_0.feature.statistiques.model.LigneClassement
import empire.digiprem.projet2_0.feature.statistiques.model.MoisResultat
import empire.digiprem.projet2_0.feature.statistiques.model.MoisValeur
import empire.digiprem.projet2_0.feature.statistiques.model.ProfilJoueur
import empire.digiprem.projet2_0.feature.statistiques.model.RecompenseJoueur
import empire.digiprem.projet2_0.feature.statistiques.model.Serie
import empire.digiprem.projet2_0.feature.statistiques.model.TableauSaison
import empire.digiprem.projet2_0.feature.statistiques.model.TypeBadge
import empire.digiprem.projet2_0.feature.statistiques.model.VictoireRecord
import empire.digiprem.projet2_0.feature.statistiques.model.VoteHommeDuMatch

fun MoisResultatDto.toDomain() = MoisResultat(mois = mois, campVainqueurId = campVainqueurId)
fun SerieDto.toDomain() = Serie(campId = campId, longueur = longueur)
fun VictoireRecordDto.toDomain() = VictoireRecord(campId = campId, scoreCampA = scoreCampA, scoreCampB = scoreCampB, date = date)

fun TableauSaisonDto.toDomain() = TableauSaison(
    saisonId = saisonId,
    saisonLibelle = saisonLibelle,
    scoreCampA = scoreCampA,
    scoreCampB = scoreCampB,
    nulsCount = nulsCount,
    butsCampA = butsCampA,
    butsCampB = butsCampB,
    bilanMensuel = bilanMensuel.map { it.toDomain() },
    serieEnCours = serieEnCours?.toDomain(),
    plusLongueSerie = plusLongueSerie?.toDomain(),
    plusLargeVictoire = plusLargeVictoire?.toDomain(),
)

fun LigneClassementDto.toDomain() = LigneClassement(
    utilisateurId = utilisateurId,
    pseudo = pseudo,
    campId = campId,
    campLibelle = campLibelle,
    nombreMatchs = nombreMatchs,
    valeur = valeur,
)

fun BadgeObtenuDto.toDomain() = BadgeObtenu(
    id = id,
    utilisateurId = utilisateurId,
    type = runCatching { TypeBadge.valueOf(type) }.getOrDefault(TypeBadge.PERSONNALISE),
    contexte = contexte,
    date = date,
)

fun MoisValeurDto.toDomain() = MoisValeur(mois = mois, valeur = valeur)

fun ProfilJoueurDto.toDomain() = ProfilJoueur(
    utilisateurId = utilisateurId,
    pseudo = pseudo,
    poste = poste,
    clubs = clubs,
    contexteActuel = runCatching { ContexteStats.valueOf(contexteActuel) }.getOrDefault(ContexteStats.TOTAL),
    matchs = matchs,
    buts = buts,
    passes = passes,
    victoires = victoires,
    hommeDuMatch = hommeDuMatch,
    progressionMensuelle = progressionMensuelle.map { it.toDomain() },
    variationVsAnneePrecedente = variationVsAnneePrecedente,
    badgesObtenus = badgesObtenus.map { it.toDomain() },
)

fun CandidatDto.toDomain() = Candidat(utilisateurId = utilisateurId, pseudo = pseudo, campId = campId)

fun VoteHommeDuMatchDto.toDomain() = VoteHommeDuMatch(
    match = match.toDomain(),
    evenements = evenements.map { it.toDomain() },
    eligibles = eligibles.map { it.toDomain() },
    votesParCandidat = votesParCandidat,
    monVoteCandidatId = monVoteCandidatId,
    heuresRestantes = heuresRestantes,
)

fun RecompenseJoueurDto.toDomain() = RecompenseJoueur(utilisateurId = utilisateurId, pseudo = pseudo, valeurAffichee = valeurAffichee)

fun BilanSaisonDto.toDomain() = BilanSaison(
    saisonId = saisonId,
    saisonLibelle = saisonLibelle,
    campChampionId = campChampionId,
    scoreCampA = scoreCampA,
    scoreCampB = scoreCampB,
    nombreMatchs = nombreMatchs,
    joueurDeLAnnee = joueurDeLAnnee?.toDomain(),
    meilleurButeur = meilleurButeur?.toDomain(),
    meilleurPasseur = meilleurPasseur?.toDomain(),
    plusAssidu = plusAssidu?.toDomain(),
    meilleurGardien = meilleurGardien?.toDomain(),
)
