package empire.digiprem.projet2_0.contracts.statistiques

import kotlinx.serialization.Serializable

// Pas de valeur par défaut (cf. shared-contracts/base/ApiResponse.kt) : kotlinx.serialization
// avec encodeDefaults=false omet silencieusement tout champ égal à sa valeur par défaut Kotlin.

/** CU22 (F-STA-01) — un point de l'évolution mensuelle des buts d'une saison. */
@Serializable
data class PointMensuelDto(
    val mois: String,
    val butsCampA: Int,
    val butsCampB: Int,
)

/** CU22 (F-STA-01) — tableau de bord de saison : bilan global camp A / camp B. */
@Serializable
data class TableauSaisonDto(
    val victoiresCampA: Int,
    val victoiresCampB: Int,
    val nuls: Int,
    val butsCampA: Int,
    val butsCampB: Int,
    val serieEnCoursCampId: String?,
    val longueurSerieEnCours: Int,
    val evolutionMensuelle: List<PointMensuelDto>,
)

/** Entrée générique de classement (buteur, passeur, assiduité, homme du match...). */
@Serializable
data class ClassementEntryDto(
    val utilisateurId: String,
    val pseudo: String,
    val valeur: Int,
)

/** CU22 (F-STA-02) — classements d'une saison. Pas de "meilleur gardien" : aucune donnée
 * d'arrêt/clean-sheet fiable n'existe dans le modèle Evenement actuel (limitation produit,
 * pas un oubli). */
@Serializable
data class ClassementsDto(
    val meilleursButeurs: List<ClassementEntryDto>,
    val meilleursPasseurs: List<ClassementEntryDto>,
    val meilleursButeursPasseurs: List<ClassementEntryDto>,
    val plusAssidus: List<ClassementEntryDto>,
    val hommesDuMatch: List<ClassementEntryDto>,
    val moyenneButsParMatch: Double,
)

/** CU22 (F-STA-03) — statistiques d'un club, uniquement sur les matchs DEUX_ZERO (le module
 * amical CU21 est volontairement exclu de ce jalon, cf. KDoc StatistiquesService). */
@Serializable
data class StatistiquesClubDto(
    val clubId: String,
    val nombreMatchsDeuxZero: Int,
    val victoires: Int,
    val nuls: Int,
    val defaites: Int,
    val butsMarques: Int,
    val butsEncaisses: Int,
)

/** CU22 (F-STA-04) — un match d'une confrontation historique entre deux clubs. */
@Serializable
data class ConfrontationMatchDto(
    val matchId: String,
    val date: String,
    val scoreCampA: Int,
    val scoreCampB: Int,
    val clubVainqueurId: String?,
)

/** CU22 (F-STA-04) — historique des confrontations directes entre deux clubs. */
@Serializable
data class ConfrontationsDto(
    val clubAId: String,
    val clubBId: String,
    val matchs: List<ConfrontationMatchDto>,
)

/** CU23 — statistiques personnelles d'un utilisateur (profil public, progression). */
@Serializable
data class StatistiquesUtilisateurDto(
    val utilisateurId: String,
    val buts: Int,
    val passes: Int,
    val presences: Int,
    val moyenneButsParMatch: Double,
)
