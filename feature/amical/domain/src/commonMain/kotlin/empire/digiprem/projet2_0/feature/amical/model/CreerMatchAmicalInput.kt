package empire.digiprem.projet2_0.feature.amical.model

/** E26 — Nouveau match amical (CU21). Adversaire "club inscrit" (invitation, F-AMI-02) hors
 * scope de ce premier jalon mobile : seule l'équipe invitée (RG-15, F-AMI-03) est implémentée. */
data class CreerMatchAmicalInput(
    val clubId: String,
    val nomEquipeInvitee: String,
    val date: String,
    val heure: String,
    val dureeMiTempsMinutes: Int?,
)
