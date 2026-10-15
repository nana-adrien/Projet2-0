## CU7 - Gérer les membres d'un club

| Rubrique | Contenu |
| --- | --- |
| Acteur principal | Administrateur de club |
| Préconditions | Le joueur est administrateur du club. |
| Scénario nominal | 1. L'administrateur consulte la liste des membres et des demandes en attente.<br>2. Il régénère le code d'adhésion si besoin (RG-19, l'ancien devient invalide).<br>3. Il retire un membre, ou nomme un co-administrateur (F-CLB-05).<br>4. Il peut transférer le rôle d'administrateur à un autre membre (F-CLB-06).<br>5. Il active ou non des délégations aux capitaines (composition d'effectif, organisation d'amicaux) (F-CLB-08). |
| Scénarios alternatifs | A1 : un membre quitte le club de lui-même (F-CLB-07) -> retiré des effectifs engagés à venir, historique conservé. |
| Exceptions | E1 : tentative de retrait du dernier administrateur sans successeur -> bloquée. |
| Postconditions | Composition du club et droits délégués à jour. |
| Règles | RG-19 ; F-CLB-03, F-CLB-05, F-CLB-06, F-CLB-07, F-CLB-08 |

Diagramme de séquence : `../sequences/CU7-gerer-les-membres-d-un-club.puml`.
