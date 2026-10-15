## CU22 - Consulter les statistiques et classements

| Rubrique | Contenu |
| --- | --- |
| Acteur principal | Tout utilisateur connecté (Joueur, Capitaine, Administrateur, Organisateur, Marqueur) |
| Préconditions | Au moins un match Officiel existe. |
| Scénario nominal | 1. L'utilisateur consulte le tableau de saison : score de l'année, buts marqués/encaissés, bilan mensuel, séries, records, graphique d'évolution (F-STA-01, F-STA-03, F-STA-04).<br>2. Il consulte les classements individuels : meilleur buteur, meilleur passeur, buteur+passeur, plus assidu, homme du match, meilleur gardien, moyenne par match (F-STA-02).<br>3. Il consulte les statistiques consolidées d'un club sur tous ses 2-0 et amicaux (F-STA-05).<br>4. Il consulte l'historique des confrontations entre deux clubs (F-STA-06).<br>5. Il filtre par saison, 2-0, type de match ou période (F-STA-07). |
| Scénarios alternatifs | - |
| Exceptions | - |
| Postconditions | - (consultation uniquement) |
| Règles | F-STA-01 à F-STA-07 |

Diagramme de séquence : `../sequences/CU22-consulter-les-statistiques-et-classements.puml`.

Note : les statistiques sont calculées **exclusivement** à partir des matchs au statut Officiel
(section 5.11) ; elles sont alimentées par le calcul automatique du Système après chaque
officialisation (voir CU19, CU20 et `commun/activites/A1-deroulement-match.puml`).
