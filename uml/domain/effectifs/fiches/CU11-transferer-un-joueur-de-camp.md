## CU11 - Transférer un joueur de camp

| Rubrique | Contenu |
| --- | --- |
| Acteur principal | Administrateur de club |
| Préconditions | Le joueur appartient à un effectif engagé d'un 2-0 ; le nombre de transferts autorisés par saison (défini par l'organisateur, CU13) n'est pas dépassé. |
| Scénario nominal | 1. L'administrateur initie le transfert d'un joueur d'un camp à un autre au sein du même 2-0 (ou d'un club à un autre).<br>2. Le transfert clôture l'appartenance au camp d'origine à la date du transfert (RG-09).<br>3. Les statistiques antérieures restent attribuées à l'ancien camp.<br>4. Le joueur est ajouté au nouvel effectif (soumis aux mêmes contrôles que CU10). |
| Scénarios alternatifs | - |
| Exceptions | E1 : nombre de transferts autorisés dépassé pour la saison -> transfert refusé.<br>E2 : le joueur serait présent dans les deux camps simultanément -> blocage (RG-06). |
| Postconditions | Ancien EffectifEngagé clôturé (dateSortie renseignée) ; nouvel EffectifEngagé créé. |
| Règles | RG-06, RG-09 ; F-EFF-04 |

Diagramme de séquence : `../sequences/CU11-transferer-un-joueur-de-camp.puml`.
