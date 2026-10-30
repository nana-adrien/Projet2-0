package empire.digiprem.projet2_0.feature.profile.error

import empire.digiprem.projet2_0.core.domain.util.ResultError

sealed interface ProfileError : ResultError {
    data object NotFound       : ProfileError
    data object Forbidden      : ProfileError
    /** E1 (CU4) : pseudo déjà pris par un autre compte -> rejet. */
    data object PseudoDejaPris : ProfileError
    data object Unknown        : ProfileError
}
