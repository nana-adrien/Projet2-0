## CU4 - Modifier son profil (et supprimer son compte)

| Rubrique | Contenu |
| --- | --- |
| Acteur principal | Joueur |
| Préconditions | Joueur connecté. |
| Scénario nominal | 1. Le joueur ouvre ses paramètres de profil.<br>2. Il modifie pseudo, photo, poste préféré, pied fort, informations personnelles (F-CPT-05).<br>3. Il choisit de masquer certaines informations (nom réel, âge) (F-PRO-06).<br>4. Il enregistre. |
| Scénarios alternatifs | A1 : le joueur demande la suppression de son compte (F-CPT-07) -> confirmation -> anonymisation (RG-20) : le joueur apparaît comme « Ancien joueur » dans les feuilles de match passées, scores et classements inchangés. |
| Exceptions | E1 : pseudo déjà pris par un autre compte -> rejet. |
| Postconditions | Profil mis à jour, ou compte supprimé et historique anonymisé. |
| Règles | RG-20 ; F-CPT-05, F-CPT-07, F-PRO-06 |

Diagramme de séquence : `../sequences/CU4-modifier-son-profil.puml`.
