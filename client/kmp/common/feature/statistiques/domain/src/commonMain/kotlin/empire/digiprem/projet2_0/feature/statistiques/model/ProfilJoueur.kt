package empire.digiprem.projet2_0.feature.statistiques.model

/** E19 — Profil et progression (CU23). Comparateur (F-PRO-05) et masquage d'informations
 * (F-PRO-06, déjà couvert par CU4) hors scope ici. */
enum class ContexteStats { DEUX_ZERO, AMICAUX, TOTAL }

data class ProfilJoueur(
    val utilisateurId: String,
    val pseudo: String,
    val poste: String?,
    val clubs: List<String>,
    val contexteActuel: ContexteStats,
    val matchs: Int,
    val buts: Int,
    val passes: Int,
    val victoires: Int,
    val hommeDuMatch: Int,
    val progressionMensuelle: List<MoisValeur>,
    val variationVsAnneePrecedente: Double?,
    val badgesObtenus: List<BadgeObtenu>,
) {
    val butsParMatch: Double get() = if (matchs == 0) 0.0 else buts.toDouble() / matchs
    val badgesVerrouilles: List<TypeBadge> get() = TypeBadge.entries - badgesObtenus.map { it.type }.toSet()
}

data class MoisValeur(val mois: String, val valeur: Int)
