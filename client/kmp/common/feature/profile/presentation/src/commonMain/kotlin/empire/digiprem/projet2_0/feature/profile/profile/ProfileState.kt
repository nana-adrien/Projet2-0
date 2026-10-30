package empire.digiprem.projet2_0.feature.profile.profile

import empire.digiprem.projet2_0.core.domain.model.PiedFort
import empire.digiprem.projet2_0.core.domain.model.Poste
import empire.digiprem.projet2_0.core.presentation.extension.UiText

/** CU4 — Modifier son profil (et supprimer son compte). Pas d'écran dédié dans les maquettes :
 * atteint depuis E19 (bouton "Modifier le profil"). Upload de photo hors scope (aucun
 * AppFilePicker câblé dans le projet à ce jour) — [photo] reste en lecture seule. */
data class ProfileState(
    val pseudo           : String    = "",
    val photo             : String?   = null,
    val nom               : String    = "",
    val prenom            : String    = "",
    val dateNaissance     : String    = "",
    val postePrefere      : Poste?    = null,
    val piedFort          : PiedFort? = null,
    val cacherNomReel     : Boolean   = false,
    val cacherAge         : Boolean   = false,
    val isLoading         : Boolean   = true,
    val isSaving          : Boolean   = false,
    val isDeleting        : Boolean   = false,
    val showDeleteDialog  : Boolean   = false,
    val errorMessage      : UiText?   = null,
) {
    val isFormValid: Boolean get() = pseudo.isNotBlank()
}
