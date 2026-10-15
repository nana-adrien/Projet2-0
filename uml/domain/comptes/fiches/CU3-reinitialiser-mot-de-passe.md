## CU3 - Réinitialiser son mot de passe

| Rubrique | Contenu |
| --- | --- |
| Acteur principal | Joueur (ou Visiteur ayant un compte) |
| Préconditions | Compte existant associé au téléphone/e-mail saisi. |
| Scénario nominal | 1. Le joueur demande la réinitialisation depuis l'écran de connexion.<br>2. Un code est envoyé par SMS ou e-mail (F-CPT-03).<br>3. Le joueur saisit le code et définit un nouveau mot de passe. |
| Scénarios alternatifs | - |
| Exceptions | E1 : contact inconnu -> message générique (pas de confirmation d'existence du compte, sécurité). |
| Postconditions | Mot de passe mis à jour. |
| Règles | F-CPT-03 |

Diagramme de séquence : `../sequences/CU3-reinitialiser-mot-de-passe.puml`.
