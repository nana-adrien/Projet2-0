package empire.digiprem.projet2_0.feature.social.model

/** CU27 — F-SOC-05. Entité non détaillée dans le cahier des charges (section 5.15) : [contenuType]/
 * [contenuId] résolvent la lacune UML (Signalement sans référence au contenu signalé). */
enum class TypeContenuSignale { PROFIL_UTILISATEUR }

data class Signalement(
    val id: String,
    val contenuType: TypeContenuSignale,
    val contenuId: String,
    val motif: String,
)
