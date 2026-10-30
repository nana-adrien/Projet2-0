package empire.digiprem.projet2_0.core.domain.model

/** Poste préféré d'un joueur (UML : `Utilisateur.postePrefere`). */
enum class Poste(val label: String) {
    GARDIEN(label = "Gardien"),
    DEFENSEUR(label = "Défenseur"),
    MILIEU(label = "Milieu"),
    ATTAQUANT(label = "Attaquant"),
}
