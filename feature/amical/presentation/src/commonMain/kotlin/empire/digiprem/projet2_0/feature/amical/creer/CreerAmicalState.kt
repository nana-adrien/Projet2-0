package empire.digiprem.projet2_0.feature.amical.creer

import empire.digiprem.projet2_0.core.presentation.extension.UiText

enum class DureeMiTemps(val minutes: Int?) { QUARANTE_CINQ(45), TRENTE(30), LIBRE(null) }

/** E26 — Nouveau match amical (CU21). Adversaire "club inscrit" (invitation, F-AMI-02) hors
 * scope : seule l'équipe invitée (RG-15, F-AMI-03) est proposée ici. */
data class CreerAmicalState(
    val clubId: String = "",
    val nomEquipeInvitee: String = "",
    val date: String = "",
    val heure: String = "",
    val dureeMiTemps: DureeMiTemps = DureeMiTemps.QUARANTE_CINQ,
    val isLoading: Boolean = false,
    val errorMessage: UiText? = null,
) {
    val peutCreer: Boolean
        get() = nomEquipeInvitee.isNotBlank() && date.isNotBlank() && heure.isNotBlank()
}
