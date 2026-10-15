## CU14 - Générer et gérer le calendrier

| Rubrique | Contenu |
| --- | --- |
| Acteur principal | Système (génération automatique) ; Capitaine / Organisateur (gestion) |
| Préconditions | 2-0 actif avec jours/heure habituels définis (CU8). |
| Scénario nominal | 1. Le système crée automatiquement les matchs selon les jours récurrents du 2-0 (F-CAL-01).<br>2. Un capitaine ou l'organisateur peut ajouter un match exceptionnel hors calendrier (F-CAL-02).<br>3. Un capitaine ou l'organisateur peut déplacer ou annuler un match, avec un motif (F-CAL-03) ; les membres sont notifiés (« Changement de programme »). |
| Scénarios alternatifs | - |
| Exceptions | - |
| Postconditions | Calendrier de la saison à jour. |
| Règles | F-CAL-01, F-CAL-02, F-CAL-03 |

Diagramme de séquence : `../sequences/CU14-generer-et-gerer-le-calendrier.puml`.
