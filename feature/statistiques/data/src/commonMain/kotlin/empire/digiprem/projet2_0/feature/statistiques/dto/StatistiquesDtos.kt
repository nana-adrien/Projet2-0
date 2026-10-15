package empire.digiprem.projet2_0.feature.statistiques.dto

import empire.digiprem.projet2_0.feature.calendrier.dto.MatchDto
import empire.digiprem.projet2_0.feature.matchendirect.dto.EvenementDto
import kotlinx.serialization.Serializable

/**
 * Contrat REST en attente de confirmation côté serveur (Jalon 5 en cours de construction en
 * parallèle) : routes et formes de payload déduites des fiches UML CU22-CU25 et des conventions
 * déjà en place. À ajuster dès que le serveur est disponible.
 */
@Serializable
data class MoisResultatDto(val mois: String, val campVainqueurId: String?)

@Serializable
data class SerieDto(val campId: String, val longueur: Int)

@Serializable
data class VictoireRecordDto(val campId: String, val scoreCampA: Int, val scoreCampB: Int, val date: String)

@Serializable
data class TableauSaisonDto(
    val saisonId: String,
    val saisonLibelle: String,
    val scoreCampA: Int,
    val scoreCampB: Int,
    val nulsCount: Int,
    val butsCampA: Int,
    val butsCampB: Int,
    val bilanMensuel: List<MoisResultatDto>,
    val serieEnCours: SerieDto?,
    val plusLongueSerie: SerieDto?,
    val plusLargeVictoire: VictoireRecordDto?,
)

@Serializable
data class LigneClassementDto(
    val utilisateurId: String,
    val pseudo: String,
    val campId: String,
    val campLibelle: String,
    val nombreMatchs: Int,
    val valeur: Int,
)

@Serializable
data class BadgeObtenuDto(
    val id: String,
    val utilisateurId: String,
    val type: String,
    val contexte: String?,
    val date: String,
)

@Serializable
data class MoisValeurDto(val mois: String, val valeur: Int)

@Serializable
data class ProfilJoueurDto(
    val utilisateurId: String,
    val pseudo: String,
    val poste: String?,
    val clubs: List<String>,
    val contexteActuel: String,
    val matchs: Int,
    val buts: Int,
    val passes: Int,
    val victoires: Int,
    val hommeDuMatch: Int,
    val progressionMensuelle: List<MoisValeurDto>,
    val variationVsAnneePrecedente: Double?,
    val badgesObtenus: List<BadgeObtenuDto>,
)

@Serializable
data class CandidatDto(val utilisateurId: String, val pseudo: String, val campId: String)

@Serializable
data class VoteHommeDuMatchDto(
    val match: MatchDto,
    val evenements: List<EvenementDto>,
    val eligibles: List<CandidatDto>,
    val votesParCandidat: Map<String, Int>,
    val monVoteCandidatId: String?,
    val heuresRestantes: Int,
)

@Serializable
data class VoterRequest(val candidatId: String)

@Serializable
data class RecompenseJoueurDto(val utilisateurId: String, val pseudo: String, val valeurAffichee: String)

@Serializable
data class BilanSaisonDto(
    val saisonId: String,
    val saisonLibelle: String,
    val campChampionId: String?,
    val scoreCampA: Int,
    val scoreCampB: Int,
    val nombreMatchs: Int,
    val joueurDeLAnnee: RecompenseJoueurDto?,
    val meilleurButeur: RecompenseJoueurDto?,
    val meilleurPasseur: RecompenseJoueurDto?,
    val plusAssidu: RecompenseJoueurDto?,
    val meilleurGardien: RecompenseJoueurDto?,
)
