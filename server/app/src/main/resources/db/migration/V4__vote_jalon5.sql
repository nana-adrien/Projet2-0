-- Jalon 5 (CU24) : résout la dernière lacune UML documentée en tête de V1__schema_initial.sql.
--   - vote : aucune référence au votant ni au candidat -> colonnes votant_id, candidat_id.

alter table vote add column votant_id uuid not null references users(id);
alter table vote add column candidat_id uuid not null references users(id);
alter table vote add constraint vote_pas_pour_soi_meme check (votant_id <> candidat_id);
alter table vote add constraint vote_unique_par_match unique (match_id, votant_id);
