## CU5 - Créer un club

| Rubrique | Contenu |
| --- | --- |
| Acteur principal | Joueur |
| Préconditions | Joueur connecté. |
| Scénario nominal | 1. Le joueur renseigne nom, logo, couleurs, quartier et description (F-CLB-01).<br>2. L'application génère automatiquement un code d'adhésion lisible, ex. BELAIR-A7K2 (F-CLB-02).<br>3. Le joueur devient automatiquement administrateur du club. |
| Scénarios alternatifs | - |
| Exceptions | - |
| Postconditions | Club créé avec code d'adhésion actif ; créateur = Administrateur de club. |
| Règles | F-CLB-01, F-CLB-02 |

Diagramme de séquence : `../sequences/CU5-creer-un-club.puml`.
