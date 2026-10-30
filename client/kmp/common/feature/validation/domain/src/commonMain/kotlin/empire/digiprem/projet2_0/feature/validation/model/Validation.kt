package empire.digiprem.projet2_0.feature.validation.model

enum class DecisionValidation { VALIDE, CONTESTE }

/** CU19 — une signature par camp. [campId]/[signataireId] résolvent la lacune UML
 * (classe Validation sans référence au camp ni au signataire dans le modèle d'origine). */
data class Validation(
    val id: String,
    val matchId: String,
    val campId: String,
    val signataireId: String,
    val signatairePseudo: String,
    val decision: DecisionValidation,
    val commentaire: String?,
    val correctionProposee: String?,
    val horodatage: String,
)
