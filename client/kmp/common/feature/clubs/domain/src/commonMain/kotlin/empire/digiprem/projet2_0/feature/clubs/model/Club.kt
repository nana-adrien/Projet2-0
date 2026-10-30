package empire.digiprem.projet2_0.feature.clubs.model

enum class ClubStatut { ACTIF, ARCHIVE }

data class Club(
    val id: String,
    val nom: String,
    val logo: String?,
    val couleurs: String?,
    val quartier: String?,
    val description: String?,
    val codeAdhesion: String,
    val statut: ClubStatut,
    val acceptationAutomatique: Boolean,
    val delegationCapitaines: Boolean,
)
