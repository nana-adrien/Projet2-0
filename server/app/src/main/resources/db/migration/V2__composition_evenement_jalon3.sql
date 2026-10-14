-- Jalon 3 (CU16, CU17) : résout 2 des 6 lacunes UML documentées en tête de V1__schema_initial.sql.
--   - match : marqueur suppléant non modélisé -> colonne marqueur_suppleant_id.
--   - evenement : aucune référence au joueur concerné (buteur/passeur/carton/remplacement) ni au camp
--     -> colonnes camp_id, joueur_id, joueur_secondaire_id. client_event_id ajouté pour le mode hors
--     connexion (F-LIV-06, section 9.1 du cahier des charges) : identifiant unique généré côté mobile,
--     le serveur ignore les doublons à la resynchronisation.
-- Les 4 lacunes restantes (validation, vote, signalement, motif de report) restent non résolues,
-- prévues pour leurs jalons respectifs (cf. uml/tracabilite.json).

alter table match add column marqueur_suppleant_id uuid references users(id);

alter table evenement add column camp_id uuid references camp(id);
alter table evenement add column joueur_id uuid references users(id);
alter table evenement add column joueur_secondaire_id uuid references users(id);
alter table evenement add column client_event_id uuid unique;
