## CU1 - Créer un compte

| Rubrique | Contenu |
| --- | --- |
| Acteur principal | Visiteur |
| Préconditions | Aucune. |
| Scénario nominal | 1. Le visiteur saisit son numéro de téléphone (ou e-mail).<br>2. Il reçoit un code de vérification à usage unique et le saisit (F-CPT-01).<br>3. Il choisit un pseudo unique et un mot de passe, ou poursuit sans mot de passe (connexion par code SMS).<br>4. Il saisit facultativement un code d'adhésion de club (F-CPT-04) ; si valide, une demande d'adhésion est envoyée (CU6).<br>5. Il complète facultativement son profil (photo, prénom, poste, pied fort).<br>6. Il accepte les conditions d'utilisation et la politique de confidentialité.<br>7. Le compte est créé, le visiteur devient Joueur. |
| Scénarios alternatifs | A1 : inscription via connexion sociale Google/Apple (F-CPT-06, P3).<br>A2 : code d'adhésion saisi invalide -> message d'erreur, inscription poursuivie sans club. |
| Exceptions | E1 : pseudo déjà utilisé -> nouvelle saisie demandée.<br>E2 : code de vérification expiré ou incorrect -> renvoi possible. |
| Postconditions | Compte Utilisateur créé (RG-01, un seul compte par personne, identifié par téléphone ou e-mail). |
| Règles | RG-01 ; F-CPT-01, F-CPT-04, F-CPT-06 |

Diagramme de séquence : `../sequences/CU1-creer-un-compte.puml`.
