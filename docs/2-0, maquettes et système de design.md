2-0

Système de design et maquettes, v1.0

1. [Principes UX](#principes)
2. [Couleurs](#couleurs)
3. [Typographie](#typo)
4. [Espacements et grille](#espaces)
5. [Iconographie](#icones)
6. [Composants](#composants)
7. [Écrans](#ecrans)
8. [Inscription](#e-onboarding)
9. [Accueil et calendrier](#e-accueil)
10. [Jour de match](#e-jour)
11. [Validation](#e-validation)
12. [Stats et profil](#e-stats)
13. [Clubs et 2-0](#e-clubs)
14. [Réglages et états](#e-etats)
15. [Navigation](#navigation)
16. [Interactions et accessibilité](#interactions)
17. [Livraison aux développeurs](#handoff)

# 2-0, le match du quartier, saison après saison

Ce document rassemble le système de design et les maquettes haute fidélité de l'application mobile 2-0. Il donne tout ce qu'il faut pour reproduire l'interface à l'identique : couleurs, polices, tailles, espacements, composants réutilisables avec leurs états, et chaque écran annoté.

Les maquettes sont dessinées sur un écran de référence de **360 × 760 points** (Android standard). Toutes les mesures sont en points (pt) à l'échelle 1, identiques aux pixels CSS et aux dp Android.

Version 1.010 octobre 2026iOS 15+ et Android 9+Basé sur le cahier des charges et l'analyse UML

Saison 2026, 41 matchs

BB**Bel-Air Bleus**victoires

23–18

BR**Bel-Air Rouges**victoires

4 nulsBleus devant de 5

Le tableau de score de la saison est l'élément signature de l'application : il reprend les lignes blanches d'un terrain (rond central, médiane) sur le vert du gazon synthétique, et les chiffres en condensé comme sur un panneau d'affichage.

## Principes UX

L'application se vit au bord d'un terrain : téléphone tenu d'une main, soleil sur l'écran, réseau faible, adrénaline du match. Chaque décision de design découle de ce contexte.

### Un pouce, trois touches

Les actions de match se font d'une main, dans la moitié basse de l'écran. Un but s'enregistre en trois touches au maximum : camp, buteur, passeur. Aucune saisie au clavier pendant un match.

### Lisible en plein soleil

Contrastes AA au minimum (AAA pour les scores), chiffres en grand corps condensé, cibles tactiles de 48 pt minimum, 132 pt pour les boutons de but. L'écran marqueur passe en fond sombre pour réduire l'éblouissement et la consommation.

### Le score d'abord

Sur chaque écran, l'information la plus attendue (score de l'année, score du match, prochain match) occupe le haut de l'écran et se lit en moins d'une seconde.

### Deux camps, deux couleurs

Le camp A est toujours bleu et à gauche, le camp B toujours rouge et à droite, dans toute l'application. Le joueur repère son camp sans lire.

### Rien ne se perd

Hors connexion, tout continue de fonctionner et l'interface le dit clairement. Chaque action importante peut être annulée juste après (dernier but, réponse à une convocation).

### Les mots du terrain

Le vocabulaire est celui des joueurs : 2-0, camp, capitaine, homme du match, amical. Phrases courtes, verbes d'action sur les boutons, tutoiement.

### Personas de référence

**Karim, 19 ans, attaquant**

Joue deux 2-0 (lundi et samedi). Veut voir ses buts, son classement et chambrer ses potes. Ouvre l'app plusieurs fois par jour.

**Moussa, 27 ans, capitaine**

Compose l'équipe, relance les absents, signe les résultats. Veut gagner du temps et éviter les embrouilles sur le score.

**Nadia, 41 ans, présidente d'association**

Organise un 2-0 interne avec 22 membres. Veut un outil simple pour répartir les équipes et trancher en cas de litige.

## Couleurs

La palette part du city stade : le vert du gazon synthétique, le blanc de la craie, le gris du bitume et le jaune des lignes de traçage. Le bleu et le rouge sont réservés aux deux camps et ne servent à rien d'autre.

### Couleurs de marque

Gazon

**#15664A**`--gazon`\
Couleur principale, boutons primaires, onglet actif, tableau de score.

Gazon 700

**#0F4F39**`--gazon-700`\
Texte vert sur fond clair, état appuyé.

Gazon 900

**#0B3A2A**`--gazon-900`\
Fond de l'écran marqueur.

Gazon 100

**#DCEEE4**`--gazon-100`\
Avatars, halo de focus, graphiques secondaires.

Gazon 50

**#EEF7F2**`--gazon-50`\
Fonds de tags, zones de code.

Ligne

**#FFC21A**`--ligne`\
Accent rare : meilleur joueur, badges or, focus clavier, liens dans les toasts.

### Couleurs des camps

Camp A

**#2563EB**`--campA`\
Toujours à gauche. Fond 50 : `#E8EFFE`

Camp B

**#DC3B2F**`--campB`\
Toujours à droite. Fond 50 : `#FCEAE8`

### Neutres

Bitume

**#18201D**`--bitume`\
Texte principal (contraste 15,8:1 sur craie).

Gris 600

**#5B6661**`--gris-600`\
Texte secondaire (5,6:1).

Gris 400

**#98A39E**`--gris-400`\
Icônes inactives, placeholders. Jamais pour du texte utile.

Gris 300

**#C9D1CC**`--gris-300`\
Bordures de champs.

Gris 200

**#DDE3DF**`--gris-200`\
Bordures de cartes, séparateurs.

Craie

**#F4F6F3**`--craie`\
Fond des écrans.

Blanc

**#FFFFFF**`--blanc`\
Surfaces : cartes, barres, feuilles.

### Couleurs sémantiques

Direct

**#E5322D**`--live`\
Uniquement le badge « En direct ».

Succès

**#1E9E5A**`--succes`\
Présent, validé, officiel.

Alerte

**#E38B00**`--alerte`\
Hors connexion, en attente. Texte associé : `#6B4600` sur `#FFF3D6`.

Erreur

**#B3261E**`--erreur`\
Champs en erreur, actions destructrices.

### Thème sombre

Le thème sombre suit le réglage du téléphone. Les couleurs des camps et du direct restent identiques ; seuls fonds, textes et verts s'adaptent.

| Jeton | Clair | Sombre | Remarque |
| --- | --- | --- | --- |
| `--craie` (fond) | #F4F6F3 | #101614 | Fond d'écran |
| `--blanc` (surface) | #FFFFFF | #1A2320 | Cartes, barres |
| `--bitume` (texte) | #18201D | #E8EEEB | Texte principal |
| `--gris-600` | #5B6661 | #9AA8A1 | Texte secondaire |
| `--gris-200` | #DDE3DF | #2C3833 | Bordures |
| `--gazon` | #15664A | #3FAF7E | Éclairci pour garder 4,5:1 sur fond sombre |
| `--gazon-50` | #EEF7F2 | #16302A | Fonds de tags |

## Typographie

Une seule famille, deux largeurs. **Barlow Condensed** porte les chiffres et les titres, avec la compacité d'un panneau d'affichage de stade ; **Barlow** porte tout le texte courant. Les deux sont gratuites (Google Fonts, licence OFL) et couvrent tous les accents. Les chiffres utilisent toujours `font-variant-numeric: tabular-nums` pour que les scores ne bougent pas quand ils changent.

**Score**Barlow Condensed 700\
56 / 56 pt, chiffres tabulaires

3 – 2

**Chrono**Barlow Condensed 700\
72 / 72 pt

34:12

**Display**Barlow Condensed 700\
32 / 36 pt

Bilan de la saison 2026

**Titre 1**Barlow Condensed 700\
24 / 28 pt

Classements

**Titre 2**Barlow 600\
18 / 24 pt

Prochain match

**Corps**Barlow 400\
16 / 24 pt

Réponds avant samedi 20 h pour que ton capitaine compose l'équipe.

**Corps S**Barlow 400\
14 / 20 pt

Dimanche 12 octobre, 10 h, city stade Bel-Air

**Légende**Barlow 500\
12 / 16 pt

Mis à jour il y a 2 min

**Chiffre stat**Barlow Condensed 600\
20 / 24 pt (26 / 28 en tuile)

14 buts, 0,82 par match

**Bouton**Barlow 600\
16 / 20 pt (14 en petit)

Valider le résultat

**Onglet**Barlow 600\
11 / 14 pt

Classements

#### À faire

Texte en casse de phrase partout (« Valider le résultat »). Respecter la taille dynamique du système jusqu'à 130 % : les cartes s'allongent, elles ne tronquent pas les noms.

#### À éviter

Les capitales sur des libellés, le condensé pour du texte de plus d'une ligne, le texte gris 400 pour une information utile.

## Espacements, grille, rayons, élévations

Tout est construit sur une base de **4 pt**. Les écrans ont des marges latérales de 16 pt et les éléments d'une même liste sont espacés de 12 pt.

#### Échelle d'espacement

`space-1`4

`space-2`8

`space-3`12

`space-4`16

`space-6`24

`space-8`32

`space-12`48

8 entre icône et texte, 12 entre cartes, 16 à l'intérieur d'une carte et en marge d'écran, 24 entre sections, 32 au-dessus d'un bouton principal en bas d'écran.

#### Grille d'écran

| Élément | Valeur |
| --- | --- |
| Largeur de référence | 360 pt (s'adapte de 320 à 430) |
| Marges latérales | 16 pt |
| Colonnes | 4 colonnes, gouttière 8 pt |
| Barre d'état | 44 pt (zone sûre système) |
| Barre d'app | 56 pt |
| Barre d'onglets | 58 pt + zone sûre (84 pt au total) |
| Cible tactile minimale | 48 × 48 pt |

#### Rayons

`r-s` 6

`r-m` 12

`r-l` 16

feuille 20

`r-pill`

6 : tags. 12 : cartes, champs, boutons. 16 : tableau de score, boutons de but. 20 : haut des feuilles modales. Pilule : avatars, sélecteur de 2-0.

#### Élévations

| Niveau | Rendu | Usage |
| --- | --- | --- |
| e0 | aucun | Fond d'écran |
| e1 | bordure 1 pt `--gris-200`, pas d'ombre | Cartes, listes |
| e2 | `0 -8 24 rgba(24,32,29,.16)` | Feuilles modales |
| e3 | `0 12 32 rgba(24,32,29,.22)` | Toasts |

Les cartes n'ont pas d'ombre : en extérieur, une bordure nette se lit mieux qu'une ombre douce.

## Iconographie

Icônes au trait de 2 pt, extrémités arrondies, dessinées sur une grille de 24 pt avec 2 pt de marge. Trois tailles : 24 (barres, onglets), 20 (dans les boutons et listes), 16 (dans les tags). L'icône est toujours accompagnée d'un libellé, sauf dans la barre d'app où un libellé d'accessibilité la décrit.

Accueil

Matchs

Classements

Clubs

Profil

But

Coup d'envoi

Notifications

Présent, valider

Absent, fermer

Incertain

Annuler

Contester

Arbitrer

Transfert

Homme du match

Code QR

Partager

Hors connexion

Synchroniser

Lieu

Heure

Réglages

Verrouillé

## Composants réutilisables

Chaque composant est présenté avec ses variantes, ses états et ses mesures. Le nom de code entre parenthèses est celui à utiliser dans le code et dans le fichier de design, pour que designers et développeurs parlent du même objet.

#### Bouton (Button)

Valider le résultat

Composer l'équipe

Plus tard

Contester

Petit

Appuyé

Désactivé

Envoi

Hauteur 52 (petit : 40), padding horizontal 20 (petit : 14), rayon 12 (petit : 10), Barlow 600 16 pt. Primaire : un seul par écran. Secondaire : bordure 1,5 pt. Danger : actions irréversibles ou de désaccord. Appuyé : `--gazon-900`. Chargement : spinner 18 pt, libellé conservé, bouton non cliquable.

#### Bouton de but (GoalButton)

Réservé à l'écran marqueur. 132 pt de haut, largeur partagée moitié-moitié, espace 10, rayon 16, Barlow Condensed 700 28 pt. Couleur du camp, position fixe (A à gauche). Retour haptique moyen à l'appui ; pas de confirmation, l'annulation est possible pendant 10 s via un toast.

#### Tableau de score de saison (SeasonBoard)

BB**Bleus**

23–18

BR**Rouges**

4 nulsSérie : Bleus x3

Fond `--gazon`, rayon 16, padding 16. Tracés blancs à 22 % d'opacité, 1,5 pt : cadre intérieur décalé de 10, médiane, rond central de 96. Chiffres Score 56 pt. Pastilles en bas : tags « sur terrain » (blanc à 18 %). Toucher le tableau ouvre le tableau de saison.

#### Carte match (MatchCard)

En direct, 34'2-0 du dimanche

BBBleus

2 – 1

RougesBR

BBBleus

10:00

RougesBR

Dim. 12 oct., city stade Bel-AirProgrammé

Carte e1, padding 12. Grille 1fr / auto / 1fr : camp A aligné à gauche, score centré (Barlow Condensed 700 28 pt, 64 pt mini), camp B aligné à droite. Écusson 28 pt. Sous l'en-tête, un tag de statut. Pied de carte séparé par un filet, Corps S gris 600.

#### Tag de statut (StatusTag)

En directProgramméÀ validerOfficielEn litigeReportéAnnuléAmical

Hauteur 24, padding 8, rayon 6, Barlow 600 12 pt. Un tag par statut de l'énumération StatutMatch du modèle UML. Le tag « En direct » porte un point blanc qui pulse (1,4 s) ; il est désactivé si l'utilisateur a demandé la réduction des animations.

#### Sélecteur de 2-0 (DeuxZeroSwitcher)

BA**2-0 du city stade Bel-Air**Dimanche, camp Bleus

Pilule blanche de 48 pt, bordure gris 200. Écusson 36. Titre Barlow 600 15 pt tronqué sur une ligne, sous-titre légende. Ouvre une feuille listant tous les 2-0 du joueur, chacun avec son camp et sa couleur. Toujours placé en haut de l'accueil.

#### Réponse à convocation (RsvpGroup)

PrésentIncertainAbsent

PrésentIncertainAbsent

Trois boutons égaux de 48 pt, espace 8, rayon 10. Non sélectionné : bordure gris 300. Présent sélectionné : plein vert succès. Absent sélectionné : fond rouge clair. Incertain sélectionné : fond `#FFF3D6`, texte `#6B4600`. Un seul choix, modifiable jusqu'au coup d'envoi.

#### Contrôle segmenté (Segmented)

ButeursPasseursAssiduité

Ce 2-0AmicauxTotal

Conteneur gris 200, rayon 10, padding 3. Segments de 34 pt, Barlow 600 14 pt ; segment actif blanc avec ombre fine. 2 à 4 segments, jamais plus.

#### Champs de saisie (TextField, CodeField)

Numéro de téléphone

+33 6 12 34 56 7

Pseudo

karim

Ce pseudo est déjà pris. Essaie karim93 ou karim.b

48

Champ : 52 pt, rayon 12, bordure 1,5 gris 300 ; focus : bordure gazon + halo 3 pt gazon 100 ; erreur : bordure et message erreur 13 pt sous le champ, avec une piste de correction. Libellé toujours visible au-dessus (14 pt 600). Code à 6 cases : 56 pt de haut, chiffres 28 pt, collage automatique du SMS.

#### Ligne de liste et avatar (ListRow, Avatar)

KB

**Karim B.**Attaquant, Bleus

14

MD

**Moussa D.**Capitaine, Bleus

YT

**Yanis T.**Demande en attente

Hauteur mini 56, séparateur gris 200. Avatar : 40 (liste), 32 (compact), 72 (profil), initiales Barlow 700 sur gazon 100, ou photo. Anneau de 2 pt à la couleur du camp quand le contexte est un match. Élément de fin : valeur, interrupteur (48 × 28) ou chevron.

#### Tuile de stat (StatTile) et graphique (BarChart)

32Matchs

14Buts

9Passes

Tuiles en grille de 3, espace 8, chiffre 26 / 28 pt condensé, légende dessous. Graphique en barres : barres gazon 100, la période en cours en gazon plein, rayon 4 en haut, mois en légende 11 pt. Valeur exacte affichée au toucher.

#### Bannière, toast, notification (Banner, Toast, Push)

Hors connexion. 2 buts seront envoyés au retour du réseau.

But de Karim enregistré[Annuler](#composants)

2-0

**But ! Bel-Air Bleus 2 – 1**

Karim marque à la 34e, passe de Moussa.

maint.

Bannière : pleine largeur, rayon 12, icône 20, texte 14 pt 500, persistante tant que l'état dure. Toast : fond bitume, apparaît au-dessus de la barre d'onglets, 10 s si action d'annulation, sinon 4 s. Notification : titre = fait + score, corps = buteur, minute, passeur.

#### Chronologie de match (Timeline)

34'**Karim B.**\
passe de Moussa D.

**Adama S.**\
sans passe21'

Coup d'envoi, 10:02

Événements du plus récent au plus ancien. Camp A aligné à gauche, camp B à droite (miroir). Minute 18 pt condensé gris. Un événement annulé reste visible, barré, avec la mention « annulé ».

#### Feuille de choix de joueur (PlayerPickerSheet)

Qui a marqué ?

KBKarim#9

MDMoussa#6

YTYanis#10

LRLucas#4

Feuille : rayon 20 en haut, poignée 40 × 4, ombre e2, voile bitume à 45 %. Grille de 4 puces joueur (avatar 32, prénom 13 pt 600, numéro). Seuls les joueurs présents du camp sont listés. Fermeture par glissement ou bouton retour.

#### Barre d'onglets (TabBar)

AccueilMatchsClassementsClubsProfil

5 onglets fixes, 58 pt + zone sûre. Icône 24 + libellé 11 pt 600. Actif : gazon, trait d'icône 2,4. Pendant un match en direct, l'onglet Matchs affiche un point rouge.

#### Code à partager (ShareCode) et étapes (Stepper)

Code d'adhésion\
**BELAIR-A7K2**

Code : cadre pointillé gazon 1,5 pt sur gazon 50, code en condensé 26 pt avec espacement 1 pt, sans caractères ambigus (0/O, 1/I). Stepper : segments de 4 pt, espace 6, autant de segments que d'étapes.

## Écrans

Chaque écran porte un identifiant (E01, E02...) et des repères jaunes numérotés qui renvoient aux annotations en dessous. Les écrans couvrent les 27 cas d'utilisation de l'analyse UML ; le numéro du cas concerné est indiqué à côté du titre.

### Inscription et connexion

E01, CU01 et CU02

#### Bienvenue

9:415G 87 %

2-0

Tous vos matchs de quartier, toute la saison.

Les scores, les buteurs et le vainqueur de l'année, enfin gardés quelque part.

Créer mon compte

J'ai déjà un compte

1. Logotype en Barlow Condensed 700 84 pt, blanc sur gazon : seul écran entièrement vert, il installe la marque.
2. Promesse en Display 32 pt, sous-texte Corps à 85 % d'opacité.
3. Bouton principal en jaune « ligne » (seule exception au primaire vert, pour le contraste sur fond vert). Bouton secondaire en contour blanc.

E02, CU01

#### Numéro de téléphone

9:415G 87 %

Ton numéro de téléphone

On t'envoie un code par SMS pour vérifier que c'est bien toi.

Numéro

+336 12 34 56 78

Utiliser une adresse e-mail à la place

Recevoir le code

En continuant, tu acceptes les conditions d'utilisation et la politique de confidentialité.

1. Stepper de 3 étapes (numéro, code, profil) en haut du contenu.
2. Titre 28 / 32 (Titre 1 agrandi pour les écrans d'une seule question).
3. Indicatif modifiable à gauche, clavier numérique ouvert automatiquement.
4. Bouton principal collé au-dessus du clavier, actif dès que le numéro est valide.

E03, CU01

#### Code de vérification

9:415G 87 %

Entre le code reçu

Envoyé au +33 6 12 34 56 78. **Modifier**

482

Renvoyer le code dans 0:42

Le code se remplit tout seul quand le SMS arrive.

1. 6 cases de 56 pt ; la case active porte le halo de focus.
2. Validation automatique à la 6e saisie, sans bouton.
3. Compte à rebours avant renvoi possible (60 s). Après 5 erreurs, message d'erreur et blocage de 15 min.

E04, CU01 et CU05

#### Profil et code de club

9:415G 87 %

Comment on t'appelle ?

Ajoute une photo pour que tes potes te reconnaissent.

Pseudo

karim93

Poste préféré

GardienDéfenseMilieuAttaque

Code de club (facultatif)

Ex. BELAIR-A7K2

Ton capitaine te l'a envoyé ? Colle-le ici ou scanne le QR.

C'est parti

1. Photo facultative : cercle de 72 en pointillés, ouvre l'appareil photo ou la galerie.
2. Poste en contrôle segmenté à 4 options.
3. Code de club facultatif (F-CPT-04), prérempli si l'utilisateur vient d'un lien d'invitation. Icône QR à droite pour scanner.

### Accueil, calendrier et convocations

E05, CU18 et CU22

#### Accueil

9:415G 87 %

BA**2-0 du city stade Bel-Air**Dimanche, camp Bleus

BB**Bleus**

23–18

BR**Rouges**

Saison 20264 nuls

Prochain match[Calendrier](#e-accueil)

BBBleus

10:00

RougesBR

Dimanche 12 oct.9 présents, 8 présents

PrésentIncertainAbsent

Dernier résultat

BBBleus

3 – 2

RougesBR

AccueilMatchsClassementsClubsProfil

1. Sélecteur de 2-0 et cloche des notifications (point rouge si non lues).
2. Tableau de score de la saison : réponse immédiate à « qui domine l'année ? ».
3. Prochain match avec compteur de présents par camp et réponse à la convocation directement depuis l'accueil.
4. Pendant un match, la carte « Prochain match » est remplacée par la carte en direct (tag rouge, score, minute).
5. Barre d'onglets à 5 entrées.

E06

#### Changer de 2-0

9:415G 87 %

BA**2-0 du city stade Bel-Air**Dimanche, camp Bleus

Mes 2-0

BA

**2-0 du city stade Bel-Air**Dimanche 10 h, camp Bleus

TI

**2-0 des Tilleuls**Samedi 18 h, camp Rouges

LU

**2-0 du lundi**Supporter

Rejoindre

Créer un 2-0

1. Feuille modale sur voile à 45 %.
2. Chaque 2-0 avec son écusson à la couleur du camp du joueur, son créneau et son rôle (camp ou supporter).
3. Accès direct à « Rejoindre » (code) et « Créer un 2-0 ».

E07, CU14

#### Calendrier des matchs

9:415G 87 %

# Matchs

À venirPassésAmicaux

Cette semaine

BBBleus

10:00

RougesBR

Dim. 12 oct.Programmé

TITilleuls R.

18:00

Tilleuls V.TV

Sam. 18 oct., 2-0 des TilleulsProgrammé

Plus tard

BBBleus

10:00

RougesBR

Dim. 19 oct.Reporté au lun. 20

BABel-Air

15:00

Lions de la CitéLC

Mer. 22 oct.Amical

AccueilMatchsClassementsClubsProfil

1. Tous les 2-0 du joueur réunis, triés par date ; le nom du 2-0 s'affiche quand il diffère du 2-0 courant.
2. Filtre segmenté À venir / Passés / Amicaux.
3. Bouton « + » réservé aux capitaines et organisateurs (match exceptionnel ou amical).
4. Les reports et annulations restent visibles avec leur tag.

E08, CU15 et CU16

#### Fiche du match et présences

9:415G 87 %

# Dim. 12 oct.

BBBleus

10:00

RougesBR

City stade Bel-Air2 × 45 min

Présences des Bleus9 sur 12

KB

**Karim B.**

Présent

MD

**Moussa D.**Capitaine

Présent

YT

**Yanis T.**

Incertain

IS

**Ibrahim S.**

Sans réponse

Composer l'équipe

1. Résumé du match : lieu, durée des mi-temps propre au 2-0 (ici 2 × 45).
2. Barre de présences empilée : présents (vert), incertains (orange), sans réponse (gris).
3. Liste triée : présents, incertains, sans réponse, absents. Un capitaine peut relancer un joueur d'un glissement.
4. Bouton « Composer l'équipe » visible uniquement pour les responsables du camp.

### Jour de match

E09, CU16

#### Composition et marqueur

9:415G 87 %

# Composition des Bleus

Titulaires (7)Remplaçants (2)

AD

**Adama D.**Gardien

MD

**Moussa D.**Milieu

KB

**Karim B.**Attaquant

YT

**Yanis T.**Incertain

Ajouter un joueur occasionnel

Marqueur du match

TH

**Théo H.**Marqueur, spectateur

Changer

RN

**Rayan N.**Suppléant

Changer

Enregistrer la composition

1. Seuls les joueurs présents ou incertains sont proposés ; cases à cocher 22 pt dans une ligne de 56 pt entièrement tactile.
2. Joueur occasionnel : dépannage sans l'ajouter à l'effectif.
3. Marqueur et suppléant : n'importe quel utilisateur, joueur ou spectateur. Ils reçoivent une notification.

E10, CU17

#### Écran marqueur, match en cours

10:365G 64 %

En direct1re mi-temps, 45 min

34:12

**Bleus**

2–1

**Rouges**

**34'** Karim B. (Moussa D.)Bleus

**21'** Adama S.Rouges

Annuler

Autre

Fin du match

1. Fond gazon 900, texte blanc : moins d'éblouissement, batterie préservée, écran maintenu allumé.
2. Chrono 72 pt calé sur la durée de mi-temps du 2-0 ; signal sonore et vibration à la fin de la période.
3. Deux boutons de but de 132 pt dans la zone du pouce, camp A à gauche.
4. Annuler, Autre (penalty, carton, remplacement), Fin du match (demande une confirmation).
5. Les trois derniers événements restent visibles pour contrôle.

E11, CU17

#### Qui a marqué ?

10:365G 64 %

34:12

But des Bleus, 34'

1\. Buteur

KBKarim#9

MDMoussa#6

YTYanis#10

LRLucas#4

ADAdama#1

SBSofiane#7

NANassim#11

?Contre son camp

2\. Passeur

Sans passe

MDMoussa

YTYanis

LRLucas

Enregistrer le but

1. La feuille s'ouvre dès l'appui sur « But » ; la minute est prise automatiquement.
2. Touche 2 : buteur parmi les présents du camp (le buteur devient indisponible comme passeur).
3. Touche 3 : passeur ou « Sans passe ». Le choix du passeur enregistre directement ; le bouton sert seulement si on veut revenir en arrière.
4. Après enregistrement : toast « But de Karim enregistré, Annuler » pendant 10 s.

E12, CU18

#### Suivre le match en direct

10:365G 87 %

# En direct

34'

BB**Bleus**

2–1

BR**Rouges**

RésuméCompositions

34'**Karim B.**\
passe de Moussa D.

**Adama S.**\
sans passe21'

8'**Yanis T.**\
passe de Karim B.

Coup d'envoi à 10:02, marqueur Théo H.

AccueilMatchsClassementsClubsProfil

1. Tableau de score réutilisé à l'échelle du match ; minute dans le tag direct.
2. Chronologie en miroir mise à jour en temps réel ; un nouveau but apparaît avec un surlignage jaune de 2 s.
3. Partage du score courant en image.

E13, CU17 et CU18

#### Notification de but, écran verrouillé

5G 87 %

10:36

Dimanche 12 octobre

2-0

**But ! Bleus 2 – 1 Rouges**

Karim B. marque à la 34e, passe de Moussa D.

maint.

2-0

**But ! Bleus 1 – 1 Rouges**

Adama S. égalise pour les Rouges (21e).

13 min

2-0

**C'est parti, Bleus – Rouges**

Coup d'envoi au city stade Bel-Air.

34 min

1. Envoyée à tous les membres et supporters, présents ou absents, selon leurs préférences (moins de 5 s après la saisie).
2. Titre : fait + score complet. Corps : buteur, minute, passeur. Toucher ouvre E12.
3. Notifications d'un même match regroupées par le système.

### Validation et litiges

E14, CU19

#### Résultat à valider

12:055G 87 %

# Valider le résultat

À valider avant lun. 12:05

BBBleus

3 – 2

RougesBR

78'**Karim B.**\
passe de Lucas R.

**Adama S.**\
sans passe61'

34'**Karim B.**\
passe de Moussa D.

\+ 2 événements

BleusValidé par toi, 12:04

RougesEn attente de Sofiane B.

Contester

Valider

1. Échéance de validation automatique (délai du 2-0, ici 24 h) visible dans le tag.
2. Récapitulatif complet : score, buteurs, passeurs.
3. État des deux signatures : qui a signé, quand, qui est attendu.
4. Valider (primaire, à droite, plus large) et Contester (danger). Valider demande une confirmation par appui long de 0,6 s : c'est la « signature ».

E15, CU19

#### Contester et proposer une correction

12:065G 87 %

# Contester

Indique ce qui ne va pas. Le capitaine des Bleus pourra accepter ta correction ; sinon l'organisateur tranchera.

Qu'est-ce qui est faux ?

**Le score**

**Un buteur ou un passeur**

**Le match n'a pas eu lieu**

Événement à corriger

34', but de Karim B.

Bon buteur

Moussa D.

Commentaire (facultatif)

Ex. Karim a fait la passe

Envoyer la correction

1. Ton factuel, conséquence expliquée en une phrase.
2. Formulaire guidé : type d'erreur, puis champs adaptés. Pas de texte libre obligatoire.
3. La correction devient visible dans E16 si elle est refusée.

E16, CU20

#### Arbitrer un litige (organisateur)

18:205G 87 %

# Litige du 12 oct.

Les capitaines ne sont pas d'accord. Ta décision sera définitive.

Résultat saisi par Théo H. (marqueur)

**34'** But de Karim B., passe de Moussa D.

Correction proposée par Sofiane B. (Rouges)

**34'** But de Moussa D., passe de Karim B.

Réponse de Moussa D. (Bleus)

Refusée : « C'est bien Karim qui a marqué. »

Voir le journal complet des saisies

Garder le résultat saisi

Appliquer la correction

Annuler le match

1. Bannière rouge rappelant le caractère définitif.
2. Trois versions côte à côte dans l'ordre chronologique, chacune signée par son auteur.
3. Journal d'audit accessible (toutes les saisies et annulations horodatées).
4. Trois décisions possibles ; un motif court est demandé à l'étape suivante.

### Statistiques, profil et récompenses

E17, CU22

#### Tableau de la saison

9:415G 87 %

# Saison 2026

**Bleus**

23–18

**Rouges**

4 nuls96 buts à 81

Mois par mois

FMAMJJSSO

Bleus ont gagné 5 mois, Rouges 3, un mois à égalité.

Séries et records

Série en cours**Bleus, 3 victoires**

Plus longue série**Rouges, 6 victoires**

Plus large victoire**Bleus 9 – 2, 15 juin**

AccueilMatchsClassementsClubsProfil

1. Sélecteur de saison dans la barre d'app (saisons précédentes en lecture seule).
2. Bande mensuelle : une case par mois à la couleur du camp vainqueur, gris pour l'égalité.
3. Séries et records sous forme de paires clé-valeur.

E18, CU22

#### Classements des joueurs

9:415G 87 %

# Classements

ButeursPasseursVotes

1AS

**Adama S.**Rouges, 30 matchs

19

2KB

**Karim B. (toi)**Bleus, 32 matchs

14

3YT

**Yanis T.**Bleus, 28 matchs

12

4SB

**Sofiane B.**Rouges, 35 matchs

11

5NA

**Nassim A.**Rouges, 22 matchs

9

6MD

**Moussa D.**Bleus, 33 matchs

7

À égalité, le joueur qui a joué le moins de matchs passe devant.

AccueilMatchsClassementsClubsProfil

1. Trois classements en segments (buteurs, passeurs, votes homme du match) ; « Assiduité » et « Moyenne » accessibles par défilement horizontal en version 2.
2. Premier rang en jaune ligne (seul usage du jaune dans le classement).
3. Ligne du joueur connecté surlignée en gazon 50, toujours visible : épinglée en bas si elle sort de l'écran.
4. Anneau d'avatar à la couleur du camp.

E19, CU23

#### Profil et progression

9:415G 87 %

KB

karim93

Attaquant, pied droit

Bel-Air BleusTilleuls Rouges

2-0 Bel-AirAmicauxTotal

32Matchs

14Buts

9Passes

0,44Buts / match

19Victoires

4Homme du match

Buts par mois+40 % vs 2025

FMAMJJSSO

Tous les badges

AccueilMatchsClassementsClubsProfil

1. Carte joueur : avatar 72, pseudo, poste, clubs.
2. Contexte des stats : un 2-0, les amicaux, ou le total tous 2-0 confondus.
3. 6 tuiles, dont les moyennes par match.
4. Progression mensuelle comparée à la saison précédente.
5. Badges : or pour les exploits, verrouillés en gris.

E20, CU24

#### Vote homme du match

13:105G 87 %

# Homme du match

BBBleus

3 – 2

RougesBR

Vote ouvert encore 22 h11 votes sur 16

Choisis un joueur des deux équipes. Tu ne peux pas voter pour toi.

MD

**Moussa D.**1 but, 1 passe

AS

**Adama S.**1 but

AD

**Adama D.**Gardien, 4 arrêts

KB

**Karim B. (toi)**2 buts

Voter pour Moussa

1. Temps restant et participation.
2. Stats du match sous chaque nom pour aider à choisir.
3. Le votant apparaît grisé et non sélectionnable (RG-16).
4. Le libellé du bouton reprend le nom choisi.

E21, CU25

#### Bilan de la saison

9:415G 87 %

Bilan de la saison 2026

Champions

**Bleus**

23–18

**Rouges**

Joueur de l'année**Moussa D.**

Meilleur buteur**Adama S., 19 buts**

Meilleur passeur**Moussa D., 15 passes**

Le plus assidu**Sofiane B., 40 matchs**

45Matchs

177Buts

26Joueurs

Partager le bilan

1. Le tableau de score prend la couleur du camp champion : c'est le seul moment où il n'est pas vert.
2. Récompenses individuelles en liste avec icône de badge.
3. Chiffres clés de l'année.
4. Partage : image 1080 × 1350 générée, reprenant ce même écran.

### Clubs, 2-0 et effectifs

E22, CU04 et CU05

#### Mes clubs

9:415G 87 %

# Mes clubs

BA

**Bel-Air**24 membres, 2 2-0, administrateur

TI

**Les Tilleuls**14 membres, 1 2-0

ES

**Étoile du Sud**Demande en attente

En attente

Rejoindre un club

Créer un club

AccueilMatchsClassementsClubsProfil

1. Un joueur peut appartenir à plusieurs clubs ; son rôle s'affiche dans le sous-titre.
2. Les demandes en attente restent visibles avec leur tag.
3. Deux actions : rejoindre (code, lien ou QR) et créer.

E23, CU06 et CU07

#### Gestion du club

9:415G 87 %

# Bel-Air

Code d'adhésion\
**BELAIR-A7K2**

Générer un nouveau code

Demandes (2)

YT

**Yanis T.**il y a 2 h

LR

**Lucas R.**hier

Réglages

**Accepter automatiquement**Toute personne avec le code entre directement

**Les capitaines composent les effectifs**

**Membres (24)**Rôles, retraits, co-administrateurs

1. Code d'adhésion en tête, avec QR et partage natif.
2. Régénération du code (confirmation : l'ancien code cesse de fonctionner).
3. Demandes en attente : refuser (gauche) ou accepter (droite, vert plein), boutons de 40 pt.
4. Délégations aux capitaines sous forme d'interrupteurs.

E24, CU08

#### Créer un 2-0, règles du jeu

9:415G 87 %

# Nouveau 2-0

Étape 2 sur 5

Règles du jeu

Durée d'une mi-temps

45 min60 minLibre

Nombre de mi-temps

–**2**+

Joueurs par camp au maximum

–**9**+

**2-0 interne**

Les deux camps viennent du même club

Continuer

1. Assistant en 5 étapes : infos, règles, calendrier, validation, camps.
2. Durée de mi-temps propre au 2-0 (RG-10), modifiable ensuite pour un match précis.
3. Compteurs avec boutons – / + de 36 pt.
4. Option 2-0 interne : déclenche l'écran de répartition E25 à l'étape « camps ».

E25, CU10

#### Répartir les membres (2-0 interne)

9:415G 87 %

# Répartir les 22 membres

**Bleus**

9 joueurs, capitaine Moussa

**Rouges**

9 joueurs, capitaine Sofiane

Tirage équilibré automatique

KB

**Karim B.**Attaquant

AB

AS

**Adama S.**Attaquant

AB

TH

**Théo H.**Milieu

AB

NA

**Nassim A.**Défenseur

AB

4 membres non répartis restent disponibles comme remplaçants.

Valider la répartition

1. Résumé des deux camps avec bord supérieur de 4 pt à la couleur du camp.
2. Tirage équilibré selon les postes et les stats (version 2).
3. Choix A / B par joueur : un joueur ne peut être que dans un camp (RG-06), le contrôle segmenté rend l'erreur impossible.

E26, CU21

#### Nouveau match amical

9:415G 87 %

# Match amical

Un amical ne compte pas dans la saison du 2-0. Les stats sont rangées à part.

Adversaire

Club inscritÉquipe invitée

Nom de l'équipe

Lions de la Cité

Elle n'a pas besoin de compte. Seuls tes buteurs seront nommés.

Date

Mer. 22 oct.

Heure

15:00

Mi-temps

45 min30 minLibre

Créer et convoquer

1. Bannière d'information sur l'impact (RG-14).
2. Adversaire : club inscrit (invitation) ou équipe invitée (simple nom, RG-15).
3. Convocation ouverte à tous les membres du club, quel que soit leur 2-0.

### Réglages et états particuliers

E27, CU26

#### Préférences de notification

9:415G 87 %

# Notifications

Bel-AirTilleulsLundi

**Tout**Buts, mi-temps, score final, convocations

**Buts et score final**

**Seulement ce qui me concerne**Convocations, validations, mes buts

**Aucune**

**Ne pas déranger**De 23:00 à 8:00

Aperçu

**But ! Bleus 2 – 1 Rouges**\
Karim B. marque à la 34e.

1. Un réglage par 2-0, choisi par onglets.
2. Quatre niveaux exclusifs en boutons radio, avec description.
3. Plage « Ne pas déranger ».
4. Aperçu de la notification correspondant au niveau choisi.

E28

#### État vide : pas encore de 2-0

9:415G 87 %

Pas encore de 2-0

Rejoins le club de ton équipe avec son code, ou lance ton propre 2-0 avec tes potes.

Rejoindre avec un code

Créer un 2-0

AccueilMatchsClassementsClubsProfil

1. Illustration simple sur disque gazon 50 de 120 pt.
2. Un état vide propose toujours une action, jamais un simple constat.
3. Même modèle pour les autres vides : pas de match à venir, pas encore de stats, aucune demande.

E29, CU17

#### Marqueur hors connexion

10:52Pas de réseau

Hors connexion. Les 2 derniers buts sont gardés sur ton téléphone et partiront tout seuls.

50:03

**Bleus**

3–2

**Rouges**

**49'** Karim B.En attente

**47'** Sofiane B.En attente

1. Bannière orange persistante : rassure et dit ce qui va se passer.
2. Chaque événement non envoyé porte l'icône de synchronisation « En attente ».
3. La saisie reste identique ; au retour du réseau, la bannière devient verte « Tout est envoyé » pendant 3 s.

## Navigation et parcours

Cinq onglets donnent accès à tout ; les actions ponctuelles s'ouvrent en écrans empilés (flèche retour) ou en feuilles modales (croix, glissement vers le bas).

| Onglet | Écrans racines | Écrans empilés | Feuilles modales |
| --- | --- | --- | --- |
| Accueil | E05 | E12 suivi en direct, E21 bilan | E06 changer de 2-0 |
| Matchs | E07 | E08 fiche, E09 composition, E10 marqueur, E14 validation, E16 arbitrage | E11 buteur, E15 contestation, E20 vote, E26 amical |
| Classements | E17, E18 | Profil d'un autre joueur (E19 en lecture) | Choix de saison |
| Clubs | E22 | E23 gestion, E24 création de 2-0, E25 répartition | Scanner un QR |
| Profil | E19 | Réglages, E27 notifications, badges | Modifier le profil |

### Parcours principaux

**Rejoindre son équipe**

Lien d'invitation*puis*E01*puis*E02*puis*E03*puis*E04 (code prérempli)*puis*E05

**Jour de match (capitaine)**

Notification convocation*puis*E08*puis*E09*puis*coup d'envoi*puis*E14

**Jour de match (marqueur)**

Notification « tu es marqueur »*puis*E10*puis*E11 à chaque but*puis*Fin du match

**Joueur absent**

E13 notification*puis*E12*puis*E20 vote

**Litige**

E14*puis*E15*puis*refus*puis*E16 organisateur

**Fin d'année**

Notification bilan*puis*E21*puis*partage

## Interactions, animations et accessibilité

#### États des éléments interactifs

| État | Rendu |
| --- | --- |
| Repos | Couleurs de base |
| Appuyé | Fond assombri d'un cran (gazon vers gazon 900), échelle 0,98 pendant 100 ms |
| Focus clavier | Contour jaune ligne 3 pt, décalage 2 pt |
| Désactivé | Fond gris 200, texte gris 400, non focalisable ; une aide explique pourquoi |
| Chargement | Spinner 18 pt dans le bouton, libellé conservé |
| Succès | Toast ou coche verte 1,5 s |
| Erreur | Bordure et message erreur, vibration légère |

#### Animations

| Moment | Durée, courbe |
| --- | --- |
| Ouverture d'une feuille | 280 ms, ease-out (0.2, 0.8, 0.2, 1) |
| Écran empilé | Transition native de la plateforme |
| Changement de score | Chiffre qui défile vers le haut, 320 ms |
| Nouveau but dans la chronologie | Surlignage jaune ligne à 30 %, s'efface en 2 s |
| Tag « En direct » | Point qui pulse, 1,4 s, en boucle |
| Bilan de saison | Seule animation de célébration : les chiffres du tableau comptent de 0 au total, 900 ms |

Avec « réduire les animations » activé, tout devient un fondu de 150 ms et le point du direct reste fixe.

#### Retours haptiques

- But enregistré : impact moyen.
- Fin de période : notification de succès système + son court.
- Signature d'un résultat (appui long) : impact léger au début, succès à la fin.
- Erreur de saisie : vibration d'erreur système.

#### Accessibilité

- Contraste texte 4,5:1 minimum, scores et chrono 7:1.
- Les camps ne se distinguent jamais par la seule couleur : position fixe (A gauche, B droite) et nom toujours écrit.
- Cibles de 48 pt minimum, espacées de 8 pt.
- Lecteur d'écran : le score se lit « Bleus 2, Rouges 1, 34e minute » ; chaque bouton d'icône a un libellé.
- Taille de texte dynamique jusqu'à 130 % sans troncature des noms.
- Annonce vocale optionnelle des buts pour le marqueur.

### Rédaction de l'interface

#### À faire

Tutoiement, phrases courtes, verbe d'action sur les boutons. Une action garde le même nom de bout en bout : le bouton « Valider » produit le message « Résultat validé ». Les erreurs disent quoi faire : « Ce code ne marche plus. Demande le nouveau code à ton capitaine. »

#### À éviter

« Soumettre », « OK », « Erreur inconnue », les points d'exclamation à répétition, le jargon technique (synchronisation, serveur, requête).

## Livraison aux développeurs

Les jetons ci-dessous sont la source unique de vérité. Ils se déclinent à l'identique en variables CSS, en thème Flutter (`ThemeData`) ou en thème React Native. Les noms de composants sont ceux de la section Composants.

```
:root {
  /* Couleurs de marque */
  --gazon: #15664A;   --gazon-700: #0F4F39; --gazon-900: #0B3A2A;
  --gazon-100: #DCEEE4; --gazon-50: #EEF7F2; --ligne: #FFC21A;
  /* Camps */
  --campA: #2563EB; --campA-50: #E8EFFE;
  --campB: #DC3B2F; --campB-50: #FCEAE8;
  /* Neutres */
  --bitume: #18201D; --gris-700: #3E4844; --gris-600: #5B6661; --gris-400: #98A39E;
  --gris-300: #C9D1CC; --gris-200: #DDE3DF; --craie: #F4F6F3; --blanc: #FFFFFF;
  /* Sémantique */
  --live: #E5322D; --succes: #1E9E5A; --alerte: #E38B00; --erreur: #B3261E;
  /* Typographie */
  --f-cond: "Barlow Condensed", "Arial Narrow", sans-serif;
  --f-body: "Barlow", system-ui, sans-serif;
  /* Espacements (base 4) */
  --space-1: 4px; --space-2: 8px; --space-3: 12px; --space-4: 16px;
  --space-6: 24px; --space-8: 32px; --space-12: 48px;
  /* Rayons */
  --r-s: 6px; --r-m: 12px; --r-l: 16px; --r-sheet: 20px; --r-pill: 999px;
  /* Élévations */
  --e2: 0 -8px 24px rgba(24, 32, 29, .16);
  --e3: 0 12px 32px rgba(24, 32, 29, .22);
}
```

### Inventaire des composants

| Composant | Variantes | Écrans |
| --- | --- | --- |
| Button | primary, secondary, ghost, danger, ligne ; normal, sm ; états | Tous |
| GoalButton | campA, campB | E10, E29 |
| SeasonBoard | saison, match en direct, champion (couleur du camp) | E05, E12, E17, E21 |
| MatchCard | programmé, direct, terminé, avec convocation | E05, E07, E08, E14, E20 |
| StatusTag | live, prog, wait, ok, lit, off, amical, onfield | Tous |
| DeuxZeroSwitcher | fermé, feuille ouverte | E05, E06 |
| RsvpGroup | sans réponse, présent, incertain, absent | E05, E08 |
| Segmented | 2 à 4 segments | E04, E07, E12, E18, E19, E24 à E27 |
| TextField, CodeField | repos, focus, erreur, désactivé | E02 à E04, E15, E26 |
| ListRow, Avatar | avec valeur, interrupteur, chevron, radio, case ; avatar 32, 40, 72, anneau de camp | Tous |
| StatTile, BarChart | 3 ou 6 tuiles | E19, E21 |
| Timeline | miroir A / B, événement annulé | E12, E14 |
| PlayerPickerSheet | buteur, passeur, vote | E11 |
| Banner, Toast, Push | offline, info, erreur ; toast avec ou sans action | E10, E13, E16, E26, E29 |
| TabBar | 5 onglets, point de direct | Écrans racines |
| ShareCode, Stepper | code d'adhésion, code d'engagement | E02 à E04, E23, E24 |

### Règles de construction

**Composants d'abord**

Aucun écran ne doit contenir de style isolé : tout passe par les composants et les jetons.

**Données réelles**

Tester avec des pseudos longs (24 caractères), des scores à deux chiffres et 30 joueurs dans un effectif.

**Mode sombre et grand texte**

Chaque écran est vérifié en thème sombre et avec la taille de texte à 130 % avant livraison.

Maquettes réalisées à partir du cahier des charges v1.0 et de l'analyse UML v1.0 du projet 2-0. Noms et données fictifs.