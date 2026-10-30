package empire.digiprem.projet2_0.feature.social.error

import empire.digiprem.projet2_0.core.domain.util.ResultError

sealed interface SocialError : ResultError {
    data object InvalidData : SocialError
    data object Unknown     : SocialError
}
