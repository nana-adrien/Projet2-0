-- Schéma initial du projet 2-0.
-- Partie A : baseline reflétant les entités JPA déjà codées (server/user, server/settings, server/notifications).
-- Partie B : tables du domaine métier issues de uml/domain/commun/classe/modele-domaine.puml (28 classes UML).
--
-- Anomalies UML connues, délibérément NON corrigées ici (cf. uml/tracabilite.json) :
--   - evenement : aucune référence au joueur concerné (buteur/passeur/carton) ni au camp -> à ajouter au Jalon 3 (CU17).
--   - validation : aucune référence au camp ni au signataire (capitaine/vice-capitaine) -> Jalon 4 (CU19/CU20).
--   - vote : aucune référence au votant ni au candidat -> Jalon 5 (CU24).
--   - match : marqueur suppléant non modélisé (seul le marqueur principal a une relation) -> Jalon 3 (CU16).
--   - signalement : aucune référence polymorphe vers le contenu signalé (club/match/utilisateur) -> Jalon 6 (CU27).
-- Ces 5 lacunes seront proposées à l'utilisateur puis répercutées dans le diagramme de classes avant d'être
-- traduites en colonnes/migrations complémentaires (V2+).

-- =========================================================================
-- Partie A — baseline des entités déjà codées
-- =========================================================================

-- Utilisateur (RG-01 : un seul compte par personne, identifié par téléphone OU e-mail ;
--              RG-20 : suppression -> anonymisation, cf. colonne "supprime").
create table users (
    id uuid primary key default gen_random_uuid(),
    pseudo text not null unique,
    telephone text unique,
    email text unique,
    hashed_password text,
    nom text,
    prenom text,
    photo text,
    date_naissance date,
    poste_prefere text check (poste_prefere in ('GARDIEN', 'DEFENSEUR', 'MILIEU', 'ATTAQUANT')),
    pied_fort text check (pied_fort in ('GAUCHE', 'DROIT', 'AMBIDEXTRE')),
    cacher_nom_reel boolean not null default false,
    cacher_age boolean not null default false,
    is_verified boolean not null default false,
    supprime boolean not null default false,
    created_at timestamptz not null default now(),
    constraint users_contact_present check (supprime or telephone is not null or email is not null)
);

-- OTP unique pour inscription (CU1), connexion sans mot de passe (CU2-A1) et
-- réinitialisation de mot de passe (CU3) : même mécanisme, "purpose" distingue l'usage.
create table otp_codes (
    id uuid primary key default gen_random_uuid(),
    contact text not null,
    purpose varchar(20) not null check (purpose in ('SIGNUP', 'LOGIN', 'PASSWORD_RESET')),
    hashed_code text not null,
    attempts int not null default 0,
    expires_at timestamptz not null,
    locked_until timestamptz,
    created_at timestamptz not null default now()
);
create index idx_otp_codes_contact_purpose on otp_codes(contact, purpose);

create table refresh_tokens (
    id uuid primary key default gen_random_uuid(),
    user_id uuid not null references users(id) on delete cascade,
    hashed_token text not null,
    expires_at timestamptz not null
);

create table user_settings (
    user_id uuid primary key references users(id) on delete cascade,
    language text not null default 'en',
    theme text not null default 'system',
    notifications_enabled boolean not null default true
);

create table notifications (
    id uuid primary key default gen_random_uuid(),
    user_id uuid not null references users(id) on delete cascade,
    title text not null,
    body text not null,
    is_read boolean not null default false,
    created_at timestamptz not null default now()
);

-- =========================================================================
-- Partie B — domaine métier (uml/domain/commun/classe/modele-domaine.puml)
-- =========================================================================

-- Club (RG-02 : un joueur peut être membre d'un nombre illimité de clubs ;
--       RG-03 : un club peut être engagé dans un nombre illimité de 2-0 — pas de contrainte à poser, c'est l'absence de limite).
create table club (
    id uuid primary key default gen_random_uuid(),
    nom text not null,
    logo text,
    couleurs text,
    quartier text,
    description text,
    code_adhesion text not null unique,
    statut varchar(20) not null default 'ACTIF' check (statut in ('ACTIF', 'ARCHIVE')),
    acceptation_automatique boolean not null default false,
    delegation_capitaines boolean not null default false
);

-- AdhesionClub
create table adhesion_club (
    id uuid primary key default gen_random_uuid(),
    utilisateur_id uuid not null references users(id) on delete cascade,
    club_id uuid not null references club(id) on delete cascade,
    role varchar(20) not null default 'MEMBRE' check (role in ('MEMBRE', 'ADMINISTRATEUR')),
    statut varchar(20) not null default 'EN_ATTENTE' check (statut in ('EN_ATTENTE', 'ACCEPTEE', 'REFUSEE', 'QUITTEE')),
    date_demande timestamptz not null default now(),
    date_reponse timestamptz,
    unique (utilisateur_id, club_id)
);

-- DeuxZero (RG-04 : exactement 2 camps par 2-0 — enforcé en application, pas en DB ;
--           RG-10 : durée de mi-temps par défaut, modifiable par match).
create table deux_zero (
    id uuid primary key default gen_random_uuid(),
    nom text not null,
    lieu text,
    jours_habituels text[] not null default '{}',
    heure_habituelle time,
    duree_mi_temps_minutes int not null,
    nombre_mi_temps int not null,
    duree_pause_minutes int,
    nombre_max_joueurs_par_camp int not null,
    delai_validation_automatique_heures int not null,
    transferts_autorises_par_saison int,
    visibilite varchar(20) not null default 'PRIVE' check (visibilite in ('PUBLIC', 'PRIVE')),
    est_interne boolean not null default false,
    statut varchar(30) not null default 'CREATION'
        check (statut in ('CREATION', 'EN_ATTENTE_ENGAGEMENT', 'ACTIF', 'ARCHIVE')),
    organisateur_id uuid not null references users(id)
);

-- Camp (RG-05 : les 2 camps d'un 2-0 peuvent être portés par le même club -> pas d'unicité (deux_zero_id, club_id) ;
--      RG-06 : un joueur n'appartient qu'à un seul camp à la fois dans un même 2-0 -> enforcé en application sur effectif_engage).
create table camp (
    id uuid primary key default gen_random_uuid(),
    deux_zero_id uuid not null references deux_zero(id) on delete cascade,
    club_id uuid references club(id), -- nul jusqu'à l'engagement du club (CU9)
    libelle text not null,
    code_engagement text not null unique,
    capitaine_id uuid references users(id),
    vice_capitaine_id uuid references users(id)
);

-- EffectifEngage (RG-07/RG-08/RG-09 : voir note de la classe, enforcées en application).
create table effectif_engage (
    id uuid primary key default gen_random_uuid(),
    camp_id uuid not null references camp(id) on delete cascade,
    utilisateur_id uuid not null references users(id),
    numero_maillot int,
    date_entree date not null default current_date,
    date_sortie date
);

-- Saison (RG-17 : classement ; RG-18 : saison clôturée = figée).
create table saison (
    id uuid primary key default gen_random_uuid(),
    deux_zero_id uuid not null references deux_zero(id) on delete cascade,
    libelle text not null,
    date_debut date not null,
    date_fin date,
    statut varchar(20) not null default 'A_VENIR' check (statut in ('A_VENIR', 'EN_COURS', 'CLOTUREE')),
    camp_champion_id uuid references camp(id)
);

-- Match (RG-11 à RG-15, voir note de la classe).
-- Un match de 2-0 référence une saison ; un match amical référence le club organisateur (et éventuellement
-- nom_equipe_invitee si l'adversaire n'a pas de compte, RG-15/F-AMI-03). Les 2 camps qui s'affrontent dans un
-- match de 2-0 ne sont pas stockés ici : ce sont toujours les 2 camps du 2-0 (RG-04), résolus via saison -> deux_zero -> camp.
create table match (
    id uuid primary key default gen_random_uuid(),
    saison_id uuid references saison(id),
    club_organisateur_id uuid references club(id),
    type varchar(20) not null check (type in ('DEUX_ZERO', 'AMICAL')),
    date date not null,
    heure time not null,
    lieu text,
    statut varchar(30) not null default 'PROGRAMME'
        check (statut in ('PROGRAMME', 'EN_COURS', 'EN_ATTENTE_DE_VALIDATION', 'OFFICIEL', 'EN_LITIGE', 'REPORTE', 'ANNULE')),
    score_camp_a int,
    score_camp_b int,
    duree_mi_temps_appliquee_minutes int,
    nom_equipe_invitee text,
    marqueur_id uuid references users(id),
    check (
        (type = 'DEUX_ZERO' and saison_id is not null)
        or (type = 'AMICAL' and club_organisateur_id is not null)
    )
);

-- Convocation
create table convocation (
    id uuid primary key default gen_random_uuid(),
    match_id uuid not null references match(id) on delete cascade,
    utilisateur_id uuid not null references users(id),
    reponse varchar(20) not null default 'SANS_REPONSE'
        check (reponse in ('PRESENT', 'ABSENT', 'INCERTAIN', 'SANS_REPONSE')),
    date_reponse timestamptz,
    unique (match_id, utilisateur_id)
);

-- Composition
create table composition (
    id uuid primary key default gen_random_uuid(),
    match_id uuid not null references match(id) on delete cascade,
    utilisateur_id uuid not null references users(id),
    titulaire boolean not null default false,
    present boolean not null default false,
    unique (match_id, utilisateur_id)
);

-- Evenement — lacune UML connue (buteur/passeur/camp), voir en-tête de fichier.
create table evenement (
    id uuid primary key default gen_random_uuid(),
    match_id uuid not null references match(id) on delete cascade,
    type varchar(30) not null check (type in (
        'BUT', 'BUT_CONTRE_SON_CAMP', 'PENALTY_MARQUE', 'PENALTY_MANQUE',
        'ARRET_DECISIF', 'CARTON', 'REMPLACEMENT', 'DEBUT_PERIODE', 'FIN_PERIODE'
    )),
    minute int not null,
    periode int not null,
    auteur_saisie_id uuid not null references users(id),
    horodatage timestamptz not null default now(),
    annule boolean not null default false
);

-- Validation — lacune UML connue (camp/signataire), voir en-tête de fichier.
create table validation (
    id uuid primary key default gen_random_uuid(),
    match_id uuid not null references match(id) on delete cascade,
    decision varchar(20) not null check (decision in ('VALIDE', 'CONTESTE')),
    commentaire text,
    correction_proposee text,
    horodatage timestamptz not null default now()
);

-- Vote — lacune UML connue (votant/candidat), voir en-tête de fichier.
create table vote (
    id uuid primary key default gen_random_uuid(),
    match_id uuid not null references match(id) on delete cascade,
    horodatage timestamptz not null default now()
);

-- BadgeObtenu
create table badge_obtenu (
    id uuid primary key default gen_random_uuid(),
    utilisateur_id uuid not null references users(id) on delete cascade,
    type varchar(30) not null check (type in (
        'PREMIER_BUT', 'DOUBLE', 'TRIPLE', 'QUADRUPLE', 'PASSEUR_DE_GENIE',
        'FIDELE', 'MUR', 'INVAINCU', 'HOMME_FORT', 'TOUJOURS_LA', 'PERSONNALISE'
    )),
    contexte text,
    date timestamptz not null default now()
);

-- PreferenceNotification (une par couple utilisateur/2-0)
create table preference_notification (
    id uuid primary key default gen_random_uuid(),
    utilisateur_id uuid not null references users(id) on delete cascade,
    deux_zero_id uuid not null references deux_zero(id) on delete cascade,
    niveau varchar(30) not null default 'TOUT'
        check (niveau in ('TOUT', 'BUTS_ET_SCORE_FINAL', 'ME_CONCERNANT', 'AUCUNE')),
    ne_rien_deranger_nocturne boolean not null default false,
    unique (utilisateur_id, deux_zero_id)
);

-- Signalement — lacune UML connue (référence polymorphe au contenu signalé), voir en-tête de fichier.
create table signalement (
    id uuid primary key default gen_random_uuid(),
    auteur_id uuid not null references users(id),
    motif text not null,
    statut varchar(30) not null default 'EN_ATTENTE'
        check (statut in ('EN_ATTENTE', 'TRAITE', 'CLASSE_SANS_SUITE')),
    date_creation timestamptz not null default now(),
    date_traitement timestamptz,
    decision text
);

create index idx_adhesion_club_club on adhesion_club(club_id);
create index idx_camp_deux_zero on camp(deux_zero_id);
create index idx_effectif_engage_camp on effectif_engage(camp_id);
create index idx_effectif_engage_utilisateur on effectif_engage(utilisateur_id);
create index idx_saison_deux_zero on saison(deux_zero_id);
create index idx_match_saison on match(saison_id);
create index idx_evenement_match on evenement(match_id);
create index idx_convocation_match on convocation(match_id);
create index idx_composition_match on composition(match_id);
