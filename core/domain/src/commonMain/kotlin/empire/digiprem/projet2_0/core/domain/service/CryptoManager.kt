package empire.digiprem.projet2_0.core.domain.service

interface CryptoManager {
    fun encrypt(value: String): String
    fun decrypt(value: String): String
    fun remove(key: String)
    fun onCleared()
}
