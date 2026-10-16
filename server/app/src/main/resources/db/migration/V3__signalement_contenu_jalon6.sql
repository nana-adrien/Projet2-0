-- Jalon 6 (CU27) : résout la dernière des 6 lacunes UML documentées en tête de V1__schema_initial.sql.
--   - signalement : aucune référence polymorphe vers le contenu signalé (club/match/utilisateur)
--     -> colonnes contenu_type, contenu_id. La colonne auteur_id (déjà présente en V1) joue le
--     rôle de signaleur, aucun renommage nécessaire.
-- Seule la valeur PROFIL_UTILISATEUR est utilisée par le mobile à ce stade (bouton "Signaler ce
-- profil" sur E19) ; les autres natures de contenu (MATCH, CLUB, MESSAGE...) ne sont pas
-- modélisées ici car aucun CU ne les déclenche encore (cf. note CU27 dans uml/tracabilite.json).
-- La table est vide à ce stade du projet (fonctionnalité pas encore livrée) : colonnes ajoutées
-- directement en NOT NULL, sans étape de backfill.

alter table signalement add column contenu_type varchar(30) not null
    check (contenu_type in ('PROFIL_UTILISATEUR'));
alter table signalement add column contenu_id uuid not null;
