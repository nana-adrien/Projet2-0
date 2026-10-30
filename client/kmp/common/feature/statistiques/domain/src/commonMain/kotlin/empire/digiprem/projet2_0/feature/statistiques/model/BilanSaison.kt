package empire.digiprem.projet2_0.feature.statistiques.model

/** E21 — Bilan de la saison (CU25, RG-17). Visuel partageable (F-BIL-05) et palmarès
 * historique multi-saisons (F-BIL-06) hors scope. */
data class BilanSaison(
    val saisonId: String,
    val saisonLibelle: String,
    val campChampionId: String?,
    val scoreCampA: Int,
    val scoreCampB: Int,
    val nombreMatchs: Int,
    val joueurDeLAnnee: RecompenseJoueur?,
    val meilleurButeur: RecompenseJoueur?,
    val meilleurPasseur: RecompenseJoueur?,
    val plusAssidu: RecompenseJoueur?,
    val meilleurGardien: RecompenseJoueur?,
)

data class RecompenseJoueur(val utilisateurId: String, val pseudo: String, val valeurAffichee: String)
