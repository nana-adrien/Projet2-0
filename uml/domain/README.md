# Domaine

Ranger les cas d'utilisation par groupe fonctionnel, un sous-dossier par groupe. À l'intérieur de
chaque groupe, les diagrammes sont eux-mêmes rangés **par type**, dans un sous-dossier dédié :

```
domain/
  commun/              acteurs, classes et processus transverses à tous les groupes
    acteurs.md           description des acteurs
    usecase/              diagramme(s) d'acteurs / de cas d'utilisation transverses
    classe/                diagramme de classes métier (modele-domaine.puml)
    etats/                  diagrammes d'états-transitions (un par entité à cycle de vie)
    activites/              diagrammes d'activités transverses (un par processus, + README.md listant les CU traversés)

  <groupe-fonctionnel>/   ex. comptes/, clubs/, deuxzero/, effectifs/, saisons/, calendrier/,
                          saisie_directe/, validation/, amicaux/, statistiques/, profil/,
                          recompenses/, bilan/, social/
    fiches/                 une fiche .md par cas d'utilisation : CUxx-nom-slug.md
    sequences/              un diagramme de séquence .puml par CU : CUxx-nom-slug.puml
    usecase/                diagramme de cas d'utilisation du groupe : cas-utilisation.puml
```

Chaque fiche commence par un titre `## <ID> - <Nom>` (ex. `## CU12 - Payer une commande`) : c'est
ainsi que les cas d'utilisation sont reconnus (peu importe le sous-dossier, l'extraction parcourt
tout `domain/**/*.md` et `domain/**/*.puml`). La fiche se termine par un renvoi vers son diagramme
de séquence (`../sequences/CUxx-....puml`) plutôt que d'embarquer le PlantUML en ligne. Modèle de
fiche : `_modele-fiche.md`.
