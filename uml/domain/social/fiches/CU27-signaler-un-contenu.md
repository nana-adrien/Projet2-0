## CU27 - Signaler un contenu

| Rubrique | Contenu |
| --- | --- |
| Acteur principal | Joueur, Supporter / Abonné |
| Acteur secondaire | Administrateur plateforme |
| Préconditions | Utilisateur connecté, contenu ou comportement visible dans l'application (fil du 2-0, réaction, média). |
| Scénario nominal | 1. L'utilisateur signale un contenu ou un comportement inapproprié depuis l'écran concerné (F-SOC-05).<br>2. Le signalement est transmis à l'administrateur plateforme (back-office).<br>3. L'administrateur plateforme consulte le signalement, modère (suppression de contenu, suspension de compte) ou le classe sans suite. |
| Scénarios alternatifs | - |
| Exceptions | - |
| Postconditions | Signalement enregistré et traité par le back-office ; le contenu concerné peut être retiré. |
| Règles | F-SOC-05 (contexte : section 8.2 Sécurité — protection contre les abus ; aucune RG-xx dédiée dans le cahier des charges) |

Diagramme de séquence : `../sequences/CU27-signaler-un-contenu.puml`.

Note : ce cas d'utilisation s'appuie sur le module 5.15 (Partage et vie communautaire). Les autres
exigences de ce module (fil du 2-0 F-SOC-01, réactions F-SOC-02, partage F-SOC-03, médias
F-SOC-04) sont des fonctionnalités d'accompagnement de la consultation (CU18, CU22, CU25) et
n'ont pas de cas d'utilisation dédié dans cette analyse.
