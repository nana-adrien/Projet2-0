package empire.digiprem.projet2_0.feature.social.error

import empire.digiprem.projet2_0.core.domain.network.DataError

fun DataError.Remote.toSocialError(): SocialError = when (this) {
    DataError.Remote.BadRequest -> SocialError.InvalidData
    else                        -> SocialError.Unknown
}
