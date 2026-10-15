package empire.digiprem.projet2_0.core.data.extensions

import empire.digiprem.projet2_0.contracts.base.ApiResponseWithPayload
import empire.digiprem.projet2_0.core.domain.network.DataError
import empire.digiprem.projet2_0.core.domain.util.Result
import empire.digiprem.projet2_0.core.domain.util.map

fun <T> Result<ApiResponseWithPayload<T>, DataError.Remote>.toPayload(): Result<T?, DataError.Remote> =
    map { it.payload }

// Returns fallback data when the call fails — useful during development without a backend
fun <T> Result<T, DataError.Remote>.onTestIfFailure(
    fallback: () -> T,
): Result<T, DataError.Remote> = when (this) {
    is Result.Success -> this
    is Result.Failure -> Result.Success(fallback())
}
