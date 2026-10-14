package empire.digiprem.projet2_0.feature.matchendirect.model

data class Composition(
    val id: String,
    val matchId: String,
    val utilisateurId: String,
    val pseudo: String,
    val titulaire: Boolean,
    val present: Boolean,
)
