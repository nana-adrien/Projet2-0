package empire.digiprem.projet2_0.server.common.dto

import empire.digiprem.projet2_0.contracts.base.ApiResponseWithPayload

/**
 * Enveloppe de réponse attendue par le client mobile (core:data/dto/ApiResponse.kt) :
 * safeApiCall() désérialise toujours vers ce format { hasSucceeded, errorMessages, payload }.
 * Toutes les routes qui renvoient 2xx avec un corps doivent utiliser [ApiResponseWithPayload]
 * ou [ApiResponse.ok] — jamais un DTO nu, et jamais un 204 sans corps (Ktor doit pouvoir
 * désérialiser la réponse).
 */



fun <T> T.toApiResponse() = ApiResponseWithPayload(payload = this, hasSucceeded = true, errorMessages = emptyList())
