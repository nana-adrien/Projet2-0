## CU10 - Composer un effectif engagé

| Rubrique | Contenu |
| --- | --- |
| Acteur principal | Administrateur de club (ou Capitaine si délégation activée, F-CLB-08) |
| Préconditions | Le club est engagé dans un camp du 2-0 (CU9) ; le joueur ajouté est membre accepté du club (RG-08). |
| Scénario nominal | 1. L'administrateur ouvre l'effectif du camp.<br>2. Il ajoute un membre du club à l'effectif engagé (F-EFF-01).<br>3. Il attribue facultativement un numéro de maillot (F-EFF-05).<br>4. Il nomme ou change le capitaine et le vice-capitaine (F-EFF-03). |
| Scénarios alternatifs | A1 : ajout ponctuel d'un joueur occasionnel à la feuille de match sans l'intégrer à l'effectif permanent, pour dépanner (F-EFF-06, CU16). |
| Exceptions | E1 : le joueur est déjà présent dans l'autre camp du même 2-0 -> blocage avec message explicite (RG-06, F-EFF-02).<br>E2 : le joueur n'est pas membre accepté du club -> ajout refusé (RG-08). |
| Postconditions | Effectif engagé mis à jour ; capitaine/vice-capitaine désignés. |
| Règles | RG-06, RG-08 ; F-EFF-01, F-EFF-02, F-EFF-03, F-EFF-05, F-EFF-06 |

Diagramme de séquence : `../sequences/CU10-composer-un-effectif-engage.puml`.
