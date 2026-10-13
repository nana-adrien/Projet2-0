package empire.digiprem.projet2_0.contracts.auth

import kotlinx.serialization.Serializable

@Serializable
data class RequestOtpRequest(
    val contact: String,
    val purpose: String,
)
