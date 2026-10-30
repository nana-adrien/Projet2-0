package empire.digiprem.projet2_0.feature.statistiques.model

/** E17 — Tableau de la saison (CU22, F-STA-01/03/04). */
data class TableauSaison(
    val saisonId: String,
    val saisonLibelle: String,
    val scoreCampA: Int,
    val scoreCampB: Int,
    val nulsCount: Int,
    val butsCampA: Int,
    val butsCampB: Int,
    val bilanMensuel: List<MoisResultat>,
    val serieEnCours: Serie?,
    val plusLongueSerie: Serie?,
    val plusLargeVictoire: VictoireRecord?,
)

/** [campVainqueurId] null = égalité ce mois-ci. */
data class MoisResultat(val mois: String, val campVainqueurId: String?)

data class Serie(val campId: String, val longueur: Int)

data class VictoireRecord(val campId: String, val scoreCampA: Int, val scoreCampB: Int, val date: String)
