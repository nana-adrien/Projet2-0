package empire.digiprem.projet2_0.feature.matchendirect.model

import empire.digiprem.projet2_0.feature.calendrier.model.Match

/** Types saisissables via AjouterEvenementUseCase. DEBUT_PERIODE/FIN_PERIODE ne sont jamais
 * saisis directement, uniquement produits par démarrer/mi-temps/terminer. */
enum class TypeEvenementSaisissable {
    BUT, BUT_CONTRE_SON_CAMP, PENALTY_MARQUE, PENALTY_MANQUE, ARRET_DECISIF, CARTON, REMPLACEMENT
}

enum class TypeEvenement {
    BUT, BUT_CONTRE_SON_CAMP, PENALTY_MARQUE, PENALTY_MANQUE, ARRET_DECISIF, CARTON, REMPLACEMENT, DEBUT_PERIODE, FIN_PERIODE
}

data class Evenement(
    val id: String,
    val matchId: String,
    val type: TypeEvenement,
    val minute: Int,
    val periode: Int,
    val campId: String?,
    val joueurId: String?,
    val joueurPseudo: String?,
    val joueurSecondaireId: String?,
    val joueurSecondairePseudo: String?,
    val auteurSaisieId: String,
    val horodatage: String,
    val annule: Boolean,
)

data class MatchEnDirect(
    val match: Match,
    val evenements: List<Evenement>,
)
