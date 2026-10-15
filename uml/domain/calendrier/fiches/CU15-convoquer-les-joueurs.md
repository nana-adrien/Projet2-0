## CU15 - Convoquer les joueurs

| Rubrique | Contenu |
| --- | --- |
| Acteur principal | Système (envoi automatique) ; Joueur (réponse) |
| Préconditions | Match programmé. |
| Scénario nominal | 1. La veille du match (délai réglable), chaque joueur des deux effectifs reçoit une convocation (F-CAL-04).<br>2. Chaque joueur répond en un geste : Présent, Absent ou Incertain.<br>3. Une relance automatique est envoyée quelques heures avant le match aux joueurs n'ayant pas répondu (F-CAL-05).<br>4. Le capitaine visualise en temps réel le nombre de présents de chaque camp (F-CAL-06). |
| Scénarios alternatifs | - |
| Exceptions | - |
| Postconditions | Convocations créées, réponses enregistrées. |
| Règles | F-CAL-04, F-CAL-05, F-CAL-06 |

Diagramme de séquence : `../sequences/CU15-convoquer-les-joueurs.puml`.
