package empire.digiprem.projet2_0.feature.matchendirect.marqueur

import empire.digiprem.projet2_0.core.domain.util.parseIsoInstantToEpochMillis
import empire.digiprem.projet2_0.core.presentation.extension.UiText
import empire.digiprem.projet2_0.feature.calendrier.model.Match
import empire.digiprem.projet2_0.feature.deuxzero.model.Camp
import empire.digiprem.projet2_0.feature.deuxzero.model.Effectif
import empire.digiprem.projet2_0.feature.matchendirect.model.Composition
import empire.digiprem.projet2_0.feature.matchendirect.model.Evenement
import empire.digiprem.projet2_0.feature.matchendirect.model.TypeEvenement
import empire.digiprem.projet2_0.feature.matchendirect.model.TypeEvenementSaisissable

/** Sous-écran ouvert par-dessus E10. [But] déclenche E11 (buteur/passeur). */
sealed interface MarqueurSheet {
    data class But(val camp: Camp) : MarqueurSheet
    data object Autre : MarqueurSheet
    data class JoueurUnique(val camp: Camp, val type: TypeEvenementSaisissable, val titre: String) : MarqueurSheet
    data class Remplacement(val camp: Camp) : MarqueurSheet
}

/** E10/E11 — Écran marqueur, match en cours (CU17). */
data class MarqueurState(
    val matchId: String = "",
    val deuxZeroId: String = "",
    val match: Match? = null,
    val evenements: List<Evenement> = emptyList(),
    val campA: Camp? = null,
    val campB: Camp? = null,
    val effectifA: List<Effectif> = emptyList(),
    val effectifB: List<Effectif> = emptyList(),
    val compositions: List<Composition> = emptyList(),
    val nowEpochMillis: Long = 0L,
    val isLoading: Boolean = true,
    val isActionLoading: Boolean = false,
    val errorMessage: UiText? = null,
    val message: UiText? = null,
    val messageActionLabel: String? = null,
    val sheet: MarqueurSheet? = null,
    val showConfirmerFin: Boolean = false,
) {
    val scoreCampA: Int get() = match?.scoreCampA ?: 0
    val scoreCampB: Int get() = match?.scoreCampB ?: 0

    val evenementsActifs: List<Evenement> get() = evenements.filterNot { it.annule }

    val periodeActuelle: Int
        get() = evenementsActifs.count { it.type == TypeEvenement.DEBUT_PERIODE }.coerceAtLeast(1)

    private val debutPeriodeActuelle: Evenement?
        get() = evenementsActifs
            .filter { it.type == TypeEvenement.DEBUT_PERIODE }
            .maxByOrNull { it.horodatage }

    val dureeMiTempsMinutes: Int get() = match?.dureeMiTempsAppliqueeMinutes ?: 45

    private val elapsedSecondesPeriode: Long
        get() {
            val debut = debutPeriodeActuelle?.horodatage?.let { parseIsoInstantToEpochMillis(it) } ?: return 0L
            return ((nowEpochMillis - debut) / 1000).coerceAtLeast(0)
        }

    val chronoLibelle: String
        get() {
            val minutes = elapsedSecondesPeriode / 60
            val secondes = elapsedSecondesPeriode % 60
            return "${minutes.toString().padStart(2, '0')}:${secondes.toString().padStart(2, '0')}"
        }

    /** Minute absolue cumulée (période 2 continue après la durée de la période 1, convention football). */
    val minuteActuelle: Int
        get() = (periodeActuelle - 1) * dureeMiTempsMinutes + (elapsedSecondesPeriode / 60).toInt()

    fun effectifPresentDuCamp(camp: Camp?): List<Effectif> {
        if (camp == null) return emptyList()
        val effectif = if (camp.id == campA?.id) effectifA else effectifB
        val presents = compositions.filter { it.present }.map { it.utilisateurId }.toSet()
        return effectif.filter { it.utilisateurId in presents }
    }

    fun campAdverse(camp: Camp): Camp? = if (camp.id == campA?.id) campB else campA

    val dernierEvenementsAffiches: List<Evenement>
        get() = evenementsActifs
            .filter { it.type != TypeEvenement.DEBUT_PERIODE && it.type != TypeEvenement.FIN_PERIODE }
            .sortedByDescending { it.horodatage }
            .take(3)
}
