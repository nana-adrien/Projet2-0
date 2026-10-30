package empire.digiprem.projet2_0.core.domain.util

sealed interface Result<out D, out E : ResultError> {
    data class Success<out D>(val data: D) : Result<D, Nothing>
    data class Failure<out E : ResultError>(val error: E) : Result<Nothing, E>
}
