# Acteurs

Source : cahier des charges `docs/Cahier_des_charges_2-0.pdf`, section 3 (Acteurs et rôles).

L'application repose sur un **compte unique par personne** (RG-01). Les rôles ci-dessous (hors
Visiteur, Équipe invitée, Système et Service de notifications push) sont des **rôles contextuels**
attribués à un compte joueur, pas des comptes séparés : un même utilisateur peut être simple
joueur dans un 2-0, capitaine dans un autre et organisateur d'un troisième.

| # | Acteur | Contexte | Attribution | Nature |
| --- | --- | --- | --- | --- |
| 1 | Visiteur | Application | Personne non connectée. | Principal, humain |
| 2 | Joueur | Application, club, effectif | Tout utilisateur inscrit ; rôle de base. | Principal, humain |
| 3 | Administrateur de club | Club | Créateur du club, transférable, plusieurs co-administrateurs possibles. | Principal, humain |
| 4 | Capitaine | Camp d'un 2-0 | Nommé par l'administrateur du club pour un effectif engagé. | Principal, humain |
| 5 | Vice-capitaine | Camp d'un 2-0 | Nommé par l'administrateur du club ou le capitaine ; remplace le capitaine en son absence. | Principal, humain |
| 6 | Organisateur de 2-0 | 2-0 | Créateur du 2-0, transférable ; arbitre les litiges. | Principal, humain |
| 7 | Marqueur | Match | Désigné par un capitaine avant/au début d'un match. | Principal, humain |
| 8 | Marqueur suppléant | Match | Désigné en complément du marqueur ; prend le relais en cours de match. | Principal, humain |
| 9 | Supporter / Abonné | 2-0 | Suit un 2-0 public sans en être membre. | Principal, humain |
| 10 | Administrateur plateforme | Back-office | Équipe d'exploitation : modération, support, suppression de contenus, statistiques d'usage. | Principal, humain |
| 11 | Équipe invitée | Match amical | Adversaire sans compte, saisi par un simple nom (F-AMI-03). | Secondaire, passif |
| 12 | Système | Application | Acteur non humain : validation automatique à l'expiration du délai (RG-12), génération du calendrier récurrent (F-CAL-01), calcul des badges/séries/records (F-REC-02, F-STA-04). | Secondaire, système |
| 13 | Service de notifications push | Infrastructure | FCM / APNs : relaie les notifications instantanées (F-NOT-01). | Secondaire, système externe |

## Règles de nommage reprises du glossaire (section 2)
Les diagrammes et fiches réutilisent à l'identique le vocabulaire du cahier des charges : 2-0, camp,
effectif engagé, 2-0 interne, saison, match de 2-0, match amical, équipe invitée, code d'adhésion,
code d'engagement, convocation, feuille de match, événement de match, validation, contestation,
transfert, homme du match, badge, bilan de saison.

Voir aussi : diagramme `acteurs.puml` (généralisations) et [[modele-domaine]].
