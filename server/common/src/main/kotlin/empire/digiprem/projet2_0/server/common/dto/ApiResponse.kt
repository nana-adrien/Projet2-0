package empire.digiprem.projet2_0.server.common.dto

/**
 * Enveloppe de réponse attendue par le client mobile (core:data/dto/ApiResponse.kt) :
 * safeApiCall() désérialise toujours vers ce format { hasSucceeded, errorMessages, payload }.
 * Toutes les routes qui renvoient 2xx avec un corps doivent utiliser [ApiResponseWithPayload]
 * ou [ApiResponse.ok] — jamais un DTO nu, et jamais un 204 sans corps (Ktor doit pouvoir
 * désérialiser la réponse).
 */
data class ErrorMessageDto(
    val memberNames: List<String> = emptyList(),
    val errorMessage: String = "",
)

open class ApiResponse(
    val hasSucceeded: Boolean = true,
    val errorMessages: List<ErrorMessageDto> = emptyList(),
) {
    companion object {
        fun ok(): ApiResponse = ApiResponse()
    }
}

class ApiResponseWithPayload<T>(
    val payload: T?,
) : ApiResponse()

fun <T> T.toApiResponse(): ApiResponseWithPayload<T> = ApiResponseWithPayload(this)
