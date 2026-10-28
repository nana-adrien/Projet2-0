
create table  utilisateur_fcm_tokens (
    id uuid primary key default gen_random_uuid(),
    user_id UUID NOT NULL REFERENCES users(id) ON DELETE CASCADE,
    token text not null UNIQUE ,
    device_id text not null UNIQUE ,
    platform text not null  ,
    app_version text not null  ,
    timezone text not null  ,
    last_used_at timestamptz not null default now()
);