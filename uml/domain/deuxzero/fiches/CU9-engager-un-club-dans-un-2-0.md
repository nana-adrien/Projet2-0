## CU9 - Engager un club dans un 2-0 (y compris 2-0 interne)

| Rubrique | Contenu |
| --- | --- |
| Acteur principal | Administrateur de club |
| Préconditions | Un 2-0 existe avec des codes d'engagement actifs (CU8). |
| Scénario nominal | 1. L'organisateur transmet un code d'engagement à l'administrateur du club.<br>2. L'administrateur saisit le code : son club est rattaché au camp correspondant (F-DZ-04).<br>3. Il compose l'effectif en sélectionnant des membres du club (CU10).<br>4. Il nomme le capitaine et le vice-capitaine (CU10).<br>5. Quand les deux camps sont engagés avec un capitaine chacun, le 2-0 passe à l'état Actif (RG-04). |
| Scénarios alternatifs | A1 : 2-0 interne — l'administrateur choisit l'option « 2-0 interne » dès CU8 ; son club occupe alors les deux camps. Un écran de répartition permet d'affecter chaque membre au camp A ou B, manuellement ou par tirage au sort équilibré selon postes/statistiques (F-DZ-05, F-DZ-06). Un capitaine est nommé par camp ; RG-06 s'applique (un membre, un seul camp).<br>A2 : l'organisateur est lui-même administrateur du ou des clubs -> engagement direct sans code. |
| Exceptions | E1 : code d'engagement invalide ou régénéré (RG-19) -> message d'erreur.<br>E2 : tentative d'affecter un même membre aux deux camps (2-0 interne) -> blocage (RG-06). |
| Postconditions | Camp rattaché à un club, effectif initial composé, capitaine/vice-capitaine nommés. |
| Règles | RG-04, RG-05, RG-06, RG-19 ; F-DZ-04, F-DZ-05, F-DZ-06 |

Diagramme de séquence : `../sequences/CU9-engager-un-club-dans-un-2-0.puml`.
