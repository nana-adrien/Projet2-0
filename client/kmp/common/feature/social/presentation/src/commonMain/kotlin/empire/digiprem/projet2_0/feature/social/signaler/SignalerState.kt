package empire.digiprem.projet2_0.feature.social.signaler

import empire.digiprem.projet2_0.core.presentation.extension.UiText
import empire.digiprem.projet2_0.feature.social.model.TypeContenuSignale

/** Composant réutilisable "Signaler" (CU27, F-SOC-05). Pas d'écran dédié dans la maquette :
 * invoqué depuis les écrans existants qui affichent du contenu généré par les utilisateurs. */
data class SignalerState(
    val contenuType: TypeContenuSignale? = null,
    val contenuId: String = "",
    val motif: String = "",
    val isLoading: Boolean = false,
    val errorMessage: UiText? = null,
) {
    val peutEnvoyer: Boolean get() = motif.isNotBlank()
}
