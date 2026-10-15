package empire.digiprem.projet2_0.feature.statistiques.model

/** E18 — Classements des joueurs (CU22, F-STA-02). Seuls buteurs/passeurs/votes sont construits
 * (assiduité et moyenne par match, mentionnées "en version 2" dans la maquette, différées). */
enum class TypeClassement { BUTEURS, PASSEURS, VOTES }

data class LigneClassement(
    val utilisateurId: String,
    val pseudo: String,
    val campId: String,
    val campLibelle: String,
    val nombreMatchs: Int,
    val valeur: Int,
)
