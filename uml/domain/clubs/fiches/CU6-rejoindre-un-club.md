## CU6 - Rejoindre un club

| Rubrique | Contenu |
| --- | --- |
| Acteur principal | Joueur |
| Préconditions | Le joueur dispose d'un code d'adhésion, d'un QR code ou d'un lien d'invitation valide. |
| Scénario nominal | 1. Le joueur saisit le code reçu, scanne le QR code ou ouvre le lien.<br>2. L'application affiche nom, logo et quartier du club pour confirmation.<br>3. La demande est transmise à l'administrateur.<br>4. L'administrateur accepte la demande.<br>5. Le joueur voit le club dans son espace ; il peut être ajouté aux effectifs engagés (CU10). |
| Scénarios alternatifs | A1 : acceptation automatique activée par l'administrateur -> adhésion immédiate.<br>A2 : code saisi à l'inscription (CU1) -> demande envoyée immédiatement. |
| Exceptions | E1 : code invalide ou régénéré (RG-19) -> message d'erreur.<br>E2 : administrateur refuse la demande -> joueur notifié, club non rejoint. |
| Postconditions | AdhesionClub à l'état Acceptée (ou Refusée). |
| Règles | RG-02, RG-19 ; F-CLB-02, F-CLB-03, F-CLB-04 |

Diagramme de séquence : `../sequences/CU6-rejoindre-un-club.puml`.
