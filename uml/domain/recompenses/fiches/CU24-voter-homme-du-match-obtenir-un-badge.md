## CU24 - Voter pour l'homme du match / obtenir un badge

| Rubrique | Contenu |
| --- | --- |
| Acteur principal | Joueur (vote) ; Système (attribution des badges) |
| Préconditions | Match Officiel (RG-12) ; pour le vote, le joueur doit être présent sur la feuille de match. |
| Scénario nominal | 1. Après l'officialisation, le vote homme du match s'ouvre pour 24 heures (F-REC-01).<br>2. Chaque joueur présent vote une seule fois, pour un joueur de n'importe quel camp, sauf lui-même (RG-16).<br>3. Le joueur ayant obtenu le plus de voix est désigné ; en cas d'égalité, la distinction est partagée.<br>4. En parallèle, le Système attribue automatiquement les badges dès qu'une condition est remplie (premier but, doublé/triplé/quadruplé, passeur de génie, fidèle, mur, invaincu, homme fort, toujours là — voir tableau des badges, section 5.13) et notifie le joueur concerné (F-REC-02).<br>5. Les badges obtenus sont affichés sur le profil du joueur (F-REC-03). |
| Scénarios alternatifs | A1 : l'organisateur crée des badges personnalisés propres à son 2-0 (F-REC-04, P3). |
| Exceptions | E1 : tentative de voter pour soi-même -> refusée (RG-16).<br>E2 : vote en dehors de la fenêtre de 24h -> refusé. |
| Postconditions | Vote enregistré, homme du match désigné ; badges attribués et notifiés. |
| Règles | RG-16 ; F-REC-01, F-REC-02, F-REC-03, F-REC-04 |

Diagramme de séquence : `../sequences/CU24-voter-homme-du-match-obtenir-un-badge.puml`.
