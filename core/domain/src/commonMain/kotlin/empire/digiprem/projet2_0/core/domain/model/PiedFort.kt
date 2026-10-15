package empire.digiprem.projet2_0.core.domain.model

/** Pied fort d'un joueur (UML : `Utilisateur.piedFort`). */
enum class PiedFort(val label: String) {
    GAUCHE(label = "Gauche"),
    DROIT(label = "Droit"),
    AMBIDEXTRE(label = "Ambidextre"),
}
