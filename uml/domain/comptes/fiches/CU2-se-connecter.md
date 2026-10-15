## CU2 - Se connecter

| Rubrique | Contenu |
| --- | --- |
| Acteur principal | Joueur |
| Préconditions | Compte existant. |
| Scénario nominal | 1. Le joueur saisit son identifiant (téléphone/e-mail) et son mot de passe, ou demande un code à usage unique.<br>2. L'application vérifie les identifiants.<br>3. La session est conservée sur l'appareil (F-CPT-02). |
| Scénarios alternatifs | A1 : connexion par code SMS sans mot de passe.<br>A2 : connexion sur un nouvel appareil avec un compte déjà actif ailleurs (F-CPT-08, P2). |
| Exceptions | E1 : identifiants invalides -> message d'erreur, renvoi vers CU3. |
| Postconditions | Session active. |
| Règles | F-CPT-02, F-CPT-08 |

Diagramme de séquence : `../sequences/CU2-se-connecter.puml`.
