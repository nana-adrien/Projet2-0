package empire.digiprem.projet2_0.feature.deuxzero.model

enum class SaisonStatut { EN_COURS, CLOTUREE }

data class Saison(
    val id: String,
    val deuxZeroId: String,
    val libelle: String,
    val dateDebut: String,
    val dateFin: String?,
    val statut: SaisonStatut,
    val campChampionId: String?,
)
