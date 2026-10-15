## CU21 - Organiser un match amical

| Rubrique | Contenu |
| --- | --- |
| Acteur principal | Administrateur de club (ou Capitaine si autorisé, F-CLB-08) |
| Préconditions | Club existant. |
| Scénario nominal | 1. Création d'un amical : date, lieu, durée des mi-temps, adversaire inscrit ou invité (F-AMI-01).<br>2. Composition libre parmi tous les membres du club, quel que soit leur effectif de 2-0.<br>3. Le match est saisi en direct comme un match de 2-0 (réutilise CU17/CU18).<br>4. Validation : si adversaire inscrit, les deux capitaines valident comme pour un match de 2-0 (CU19) ; si équipe invitée, le capitaine du club inscrit valide seul (RG-15).<br>5. Les statistiques sont enregistrées dans une rubrique « Amicaux » séparée, sans impact sur le classement de la saison (RG-14). |
| Scénarios alternatifs | A1 : adversaire inscrit — invitation envoyée à son administrateur/capitaine, qui l'accepte ou la refuse (F-AMI-02).<br>A2 : équipe invitée — adversaire sans compte, saisi par un simple nom ; seuls les buts du club inscrit sont attribués nominativement, les buts adverses sont comptés sans buteur (F-AMI-03).<br>A3 : un club peut lancer un défi public à un autre club de son quartier (F-AMI-05, P3). |
| Exceptions | E1 : invitation refusée par le club adverse -> match non créé. |
| Postconditions | Match de type Amical créé, statistiques dans la rubrique séparée (RG-14). |
| Règles | RG-14, RG-15 ; F-AMI-01, F-AMI-02, F-AMI-03, F-AMI-04, F-AMI-05 |

Diagramme de séquence : `../sequences/CU21-organiser-un-match-amical.puml`.
