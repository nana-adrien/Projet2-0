package empire.digiprem.projet2_0.feature.clubs.model

enum class MembershipRole { MEMBRE, ADMINISTRATEUR }
enum class MembershipStatut { EN_ATTENTE, ACCEPTEE, REFUSEE, QUITTEE }

data class Membership(
    val id: String,
    val clubId: String,
    val clubNom: String,
    val clubLogo: String?,
    val utilisateurId: String,
    val pseudo: String,
    val role: MembershipRole,
    val statut: MembershipStatut,
    val dateDemande: String,
    val dateReponse: String?,
)
