# Modèles UML du projet

| Dossier | Contenu | Vérifié dans |
| --- | --- | --- |
| `domain/` | Cas d'utilisation, séquences, classes métier. Un sous-dossier par groupe fonctionnel (`domain/comptes/`, `domain/paiements/`…) pour ranger ensemble les cas d'utilisation proches. `domain/commun/` pour ce qui est partagé : acteurs, classes métier, états. | couches domaine du serveur et de l'application |

Formats lus : Markdown avec blocs ```plantuml, fichiers .puml. Après chaque modification : `/uml-verifier` ou `extraire_modele.py`.
