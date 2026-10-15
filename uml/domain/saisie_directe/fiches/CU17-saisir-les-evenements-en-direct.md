## CU17 - Saisir les événements en direct

| Rubrique | Contenu |
| --- | --- |
| Acteur principal | Marqueur (ou Marqueur suppléant) |
| Préconditions | Marqueur désigné (CU16) ; match programmé. |
| Scénario nominal | 1. Le marqueur ouvre l'écran de match et appuie sur « Coup d'envoi » : le chronomètre démarre selon la durée de mi-temps du 2-0 (F-LIV-02, RG-10).<br>2. À l'échéance de la période, l'application signale la mi-temps ; le marqueur confirme et lance la période suivante.<br>3. Pour un but : « But Camp A/B » puis sélection du buteur parmi les joueurs présents, puis passeur facultatif — 3 touches maximum (F-LIV-03).<br>4. Le bouton « Annuler le dernier événement » permet de corriger immédiatement une erreur (F-LIV-04) ; chaque correction déclenche une notification rectificative.<br>5. Les autres types d'événements peuvent être saisis : but contre son camp, penalty, arrêt décisif, carton, remplacement (voir tableau « Événements gérés », section 5.7).<br>6. À la fin du temps réglementaire, le marqueur appuie sur « Fin du match » : le match passe à l'état En attente de validation (déclenche CU19). |
| Scénarios alternatifs | A1 : mode hors connexion — les événements sont stockés localement (identifiant unique + horodatage) et synchronisés dès le retour du réseau, dans l'ordre chronologique (F-LIV-06, section 9.1).<br>A2 : passage de relais — le suppléant reprend la saisie à partir du dernier état synchronisé en cas de perte du téléphone du marqueur (F-LIV-08).<br>A3 : saisie a posteriori — si personne n'a saisi en direct, un capitaine saisit le match complet après coup (F-LIV-10). |
| Exceptions | E1 : tentative de saisie par un autre acteur que le marqueur désigné -> refusée (RG-11).<br>E2 : correction d'un buteur ou d'une minute pendant le match (F-LIV-05). |
| Postconditions | Feuille de match (score, chronologie, événements) à jour ; écran ne se met pas en veille pendant la saisie (F-LIV-09). |
| Règles | RG-10, RG-11 ; F-LIV-01 à F-LIV-10, F-NOT-01 |

Diagramme de séquence : `../sequences/CU17-saisir-les-evenements-en-direct.puml`.
