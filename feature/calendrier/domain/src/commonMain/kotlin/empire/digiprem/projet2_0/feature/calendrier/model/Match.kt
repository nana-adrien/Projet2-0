package empire.digiprem.projet2_0.feature.calendrier.model

/** Statuts modifiables depuis l'écran E07/E08 (CU14). EN_COURS/EN_ATTENTE_DE_VALIDATION/OFFICIEL/
 * EN_LITIGE existent côté serveur mais sont pilotés par la saisie de résultat (CU16, Jalon 3). */
enum class StatutMatchModifiable { PROGRAMME, REPORTE, ANNULE }

data class Match(
    val id: String,
    val saisonId: String?,
    val type: String,
    val date: String,
    val heure: String,
    val lieu: String?,
    val statut: String,
    val scoreCampA: Int?,
    val scoreCampB: Int?,
    val dureeMiTempsAppliqueeMinutes: Int?,
    val nomEquipeInvitee: String?,
    val marqueurId: String?,
)
