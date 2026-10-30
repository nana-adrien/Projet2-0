package empire.digiprem.projet2_0.feature.deuxzero.model

data class Camp(
    val id: String,
    val deuxZeroId: String,
    val clubId: String?,
    val clubNom: String?,
    val libelle: String,
    val codeEngagement: String,
    val capitaineId: String?,
    val capitainePseudo: String?,
    val viceCapitaineId: String?,
    val viceCapitainePseudo: String?,
)
