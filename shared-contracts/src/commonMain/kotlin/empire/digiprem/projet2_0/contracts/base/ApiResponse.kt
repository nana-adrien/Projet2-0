package empire.digiprem.projet2_0.contracts.base

import kotlinx.serialization.Serializable

/**
 * kotlinx.serialization ne sérialise pas correctement via Jackson les propriétés avec valeur par
 * défaut (constructeur synthétique généré par le plugin, qui casse l'introspection Jackson côté
 * serveur — voir ErrorMessageDto). Chaque appelant doit donc passer des valeurs explicites.
 */
interface ApiResponseEnvelope {
    val hasSucceeded: Boolean
    val errorMessages: List<ErrorMessageDto>
}

@Serializable
data class ApiResponse(
    override val hasSucceeded: Boolean,
    override val errorMessages: List<ErrorMessageDto>,
) : ApiResponseEnvelope {
    companion object {
        fun ok() = ApiResponse(hasSucceeded = true, errorMessages = emptyList())
    }
}
