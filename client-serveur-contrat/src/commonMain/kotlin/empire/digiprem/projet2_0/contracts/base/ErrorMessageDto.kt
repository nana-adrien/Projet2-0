package empire.digiprem.projet2_0.contracts.base

import kotlinx.serialization.Serializable

// Pas de valeur par défaut : kotlinx.serialization génère un constructeur synthétique pour les
// défauts qui casse l'introspection Jackson côté serveur (propriétés sérialisées en {} vide).
// La nullabilité seule (sans défaut) fonctionne nativement avec Jackson, donc on la garde.
@Serializable
data class ErrorMessageDto(
    val memberNames: List<String>,
    val errorMessage: String,
)
