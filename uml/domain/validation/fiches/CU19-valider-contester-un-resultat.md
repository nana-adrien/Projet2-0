## CU19 - Valider / contester un résultat

| Rubrique | Contenu |
| --- | --- |
| Acteur principal | Capitaine / Vice-capitaine |
| Préconditions | Match à l'état En attente de validation (marqueur a déclaré la fin du match, CU17). |
| Scénario nominal | 1. Les capitaines et vice-capitaines des deux camps reçoivent une notification et ouvrent le récapitulatif (score, buteurs, passeurs, chronologie) (F-VAL-01).<br>2. Chaque camp donne sa décision : Valider ou Contester ; une seule signature par camp suffit, horodatée (F-VAL-02).<br>3. Lorsque les deux camps ont validé, le match passe à Officiel : statistiques intégrées, vote homme du match ouvert (RG-12).<br>4. Sans réponse d'un camp à l'expiration du délai défini par le 2-0 (24h ou 48h), la validation est automatique (RG-12, F-VAL-03). |
| Scénarios alternatifs | A1 : contestation — le capitaine indique ce qui ne va pas et propose une correction (F-VAL-04). Le capitaine adverse accepte (match corrigé puis officialisé) ou refuse (match -> En litige, CU20).<br>A2 : match amical contre une équipe invitée -> validé par le seul capitaine du club inscrit (RG-15). |
| Exceptions | - |
| Postconditions | Match Officiel (RG-12) ou En litige (RG-13, statistiques non comptabilisées tant que non résolu). |
| Règles | RG-12, RG-13, RG-15 ; F-VAL-01, F-VAL-02, F-VAL-03, F-VAL-04, F-VAL-06 |

Diagramme de séquence : `../sequences/CU19-valider-contester-un-resultat.puml`.
