package empire.digiprem.projet2_0.server.clubs.service

// Caractères sans ambiguïté visuelle (pas de 0/O ni 1/I), cf. maquettes — ShareCode.
private const val CODE_CHARS = "ABCDEFGHJKLMNPQRSTUVWXYZ23456789"

object ClubCodeGenerator {
    fun generate(nom: String): String {
        val prefix = nom
            .uppercase()
            .filter { it.isLetter() }
            .take(6)
            .ifBlank { "CLUB" }
        val suffix = (1..4).map { CODE_CHARS.random() }.joinToString("")
        return "$prefix-$suffix"
    }
}
