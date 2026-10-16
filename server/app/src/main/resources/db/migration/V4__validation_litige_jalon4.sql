-- Jalon 4 (CU19, CU20) : résout la lacune UML "validation : aucune référence au camp ni au
-- signataire" (documentée en tête de V1__schema_initial.sql, ligne 7).
--   - validation : ajout de camp_id (nullable -> NULL signale une ligne d'arbitrage CU20, non une
--     signature de camp DEUX_ZERO) et signataire_id NOT NULL (capitaine/vice-capitaine pour une
--     signature de camp, organisateur du 2-0 pour une ligne d'arbitrage). Pas de contrainte unique
--     SQL sur (match_id, camp_id) : "une signature par camp, modifiable tant que le match n'est pas
--     EN_LITIGE/OFFICIEL" est une règle APPLICATIVE (vérifiée côté service), pas une contrainte SQL.
--   - match : ajout de entre_en_attente_validation_a (timestamptz, nullable), horodatage du passage
--     à EN_ATTENTE_DE_VALIDATION (posé par EvenementService.terminerMatch), nécessaire pour calculer
--     l'échéance RG-12 (= cette date + deux_zero.delai_validation_automatique_heures). Nécessité
--     technique (comme client_event_id ajouté en V2), pas une lacune UML au sens strict.
-- Numérotée V4 (et non V3 comme suggéré initialement) : V3 est déjà occupée par
-- V3__signalement_contenu_jalon6.sql (Jalon 6, committé en parallèle).

alter table validation add column camp_id uuid references camp(id);
alter table validation add column signataire_id uuid not null references users(id);

alter table match add column entre_en_attente_validation_a timestamptz;
