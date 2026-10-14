package empire.digiprem.projet2_0.feature.clubs.dto

import kotlinx.serialization.Serializable

@Serializable
data class CreateClubRequest(
    val nom: String,
    val logo: String?,
    val couleurs: String?,
    val quartier: String?,
    val description: String?,
)

@Serializable
data class JoinClubRequest(val code: String)

@Serializable
data class UpdateClubSettingsRequest(
    val acceptationAutomatique: Boolean?,
    val delegationCapitaines: Boolean?,
)

@Serializable
data class ClubDto(
    val id: String,
    val nom: String,
    val logo: String?,
    val couleurs: String?,
    val quartier: String?,
    val description: String?,
    val codeAdhesion: String,
    val statut: String,
    val acceptationAutomatique: Boolean,
    val delegationCapitaines: Boolean,
)

@Serializable
data class MembershipDto(
    val id: String,
    val clubId: String,
    val clubNom: String,
    val clubLogo: String?,
    val utilisateurId: String,
    val pseudo: String,
    val role: String,
    val statut: String,
    val dateDemande: String,
    val dateReponse: String?,
)
