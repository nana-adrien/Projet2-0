## CU8 - Créer un 2-0

| Rubrique | Contenu |
| --- | --- |
| Acteur principal | Joueur (devient Organisateur de 2-0) |
| Préconditions | Joueur connecté. |
| Scénario nominal | 1. Informations générales : nom du 2-0, lieu habituel, visibilité publique ou privée (F-DZ-01).<br>2. Règles de jeu : durée d'une mi-temps (45 min, 60 min ou durée libre), nombre de mi-temps (2 par défaut), nombre max de joueurs par camp, durée de la pause (F-DZ-02, RG-10).<br>3. Calendrier : jours de match habituels et heure de coup d'envoi.<br>4. Validation : délai de validation automatique (24h ou 48h), nombre de transferts autorisés par saison.<br>5. Camps : noms des deux camps, génération d'un code d'engagement pour chacun (F-DZ-03).<br>6. Saison : dates de la première saison (par défaut l'année en cours).<br>7. Le 2-0 est créé à l'état « En attente d'engagement » ; le créateur devient Organisateur. |
| Scénarios alternatifs | A1 : option « 2-0 interne » choisie dès l'étape 1 -> enchaîne sur CU9 variante interne. |
| Exceptions | - |
| Postconditions | 2-0 créé (RG-04 : contiendra exactement 2 camps), 2 codes d'engagement générés, première saison préparée (CU12). |
| Règles | RG-04, RG-10 ; F-DZ-01, F-DZ-02, F-DZ-03 |

Diagramme de séquence : `../sequences/CU8-creer-un-2-0.puml`.
