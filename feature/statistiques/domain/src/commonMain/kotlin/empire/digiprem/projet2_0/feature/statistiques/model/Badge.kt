package empire.digiprem.projet2_0.feature.statistiques.model

enum class TypeBadge {
    PREMIER_BUT, DOUBLE, TRIPLE, QUADRUPLE, PASSEUR_DE_GENIE,
    FIDELE, MUR, INVAINCU, HOMME_FORT, TOUJOURS_LA, PERSONNALISE,
}

data class BadgeObtenu(
    val id: String,
    val utilisateurId: String,
    val type: TypeBadge,
    val contexte: String?,
    val date: String,
)
