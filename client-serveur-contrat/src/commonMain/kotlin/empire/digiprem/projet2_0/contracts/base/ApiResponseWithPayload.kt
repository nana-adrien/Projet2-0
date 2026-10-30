package empire.digiprem.projet2_0.contracts.base

import kotlinx.serialization.Serializable

@Serializable
data class ApiResponseWithPayload<T>(
    val payload: T?,
    override val hasSucceeded: Boolean,
    override val errorMessages: List<ErrorMessageDto>,
) : ApiResponseEnvelope
