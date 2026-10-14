package empire.digiprem.projet2_0.feature.deuxzero.model

data class Effectif(
    val id: String,
    val campId: String,
    val utilisateurId: String,
    val pseudo: String,
    val numeroMaillot: Int?,
    val dateEntree: String,
    val dateSortie: String?,
)
