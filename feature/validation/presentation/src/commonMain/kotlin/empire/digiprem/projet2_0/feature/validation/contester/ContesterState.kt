package empire.digiprem.projet2_0.feature.validation.contester

import empire.digiprem.projet2_0.core.presentation.extension.UiText
import empire.digiprem.projet2_0.feature.matchendirect.model.Evenement
import empire.digiprem.projet2_0.feature.matchendirect.model.TypeEvenement

enum class TypeErreur { SCORE, BUTEUR_OU_PASSEUR, MATCH_NON_JOUE }

/** E15 — Contester et proposer une correction (CU19, A1). */
data class ContesterState(
    val matchId: String = "",
    val evenements: List<Evenement> = emptyList(),
    val typeErreur: TypeErreur? = null,
    val evenementACorrigerId: String? = null,
    val bonJoueur: String = "",
    val commentaire: String = "",
    val isLoading: Boolean = false,
    val errorMessage: UiText? = null,
) {
    val evenementsCorrigeables: List<Evenement>
        get() = evenements.filterNot { it.annule }.filter { it.type != TypeEvenement.DEBUT_PERIODE && it.type != TypeEvenement.FIN_PERIODE }

    val peutEnvoyer: Boolean
        get() = when (typeErreur) {
            null -> false
            TypeErreur.SCORE -> true
            TypeErreur.BUTEUR_OU_PASSEUR -> evenementACorrigerId != null && bonJoueur.isNotBlank()
            TypeErreur.MATCH_NON_JOUE -> true
        }

    /** Construit le texte envoyé au serveur dans Validation.correctionProposee (UML : simple String). */
    fun construireCorrectionProposee(): String = when (typeErreur) {
        TypeErreur.SCORE -> "Le score affiché est incorrect."
        TypeErreur.BUTEUR_OU_PASSEUR -> {
            val evenement = evenements.firstOrNull { it.id == evenementACorrigerId }
            "Événement ${evenement?.minute ?: "?"}' : bon joueur = $bonJoueur (au lieu de ${evenement?.joueurPseudo ?: "?"})."
        }
        TypeErreur.MATCH_NON_JOUE -> "Le match n'a pas eu lieu."
        null -> ""
    }
}
