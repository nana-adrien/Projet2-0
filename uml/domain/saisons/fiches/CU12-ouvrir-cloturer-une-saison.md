## CU12 - Ouvrir / clôturer une saison

| Rubrique | Contenu |
| --- | --- |
| Acteur principal | Organisateur de 2-0 (ouverture et clôture manuelle) ; Système (clôture automatique) |
| Préconditions | Un 2-0 actif existe (RG-04). |
| Scénario nominal | 1. L'organisateur définit le libellé et les dates de la saison (F-SAI-01).<br>2. Pendant la saison, le tableau de saison affiche le score global (victoires A, nuls, victoires B), les buts cumulés et les séries en cours (F-SAI-02).<br>3. À la date de fin (ou sur décision manuelle), la saison se clôture : les résultats sont figés (RG-18).<br>4. Le camp champion est calculé selon RG-17 (victoires, puis différence de buts cumulée, puis buts marqués, puis résultat du dernier match).<br>5. Le bilan de saison est déclenché (CU25) et la saison suivante est proposée automatiquement (F-SAI-01). |
| Scénarios alternatifs | - |
| Exceptions | E1 : tentative de modification d'une saison clôturée -> refusée, sauf intervention de l'administrateur plateforme à la demande de l'organisateur (RG-18). |
| Postconditions | Saison à l'état Clôturée ; champion et récompenses calculés. |
| Règles | RG-17, RG-18 ; F-SAI-01, F-SAI-02, F-SAI-03, F-SAI-04 |

Diagramme de séquence : `../sequences/CU12-ouvrir-cloturer-une-saison.puml`.
