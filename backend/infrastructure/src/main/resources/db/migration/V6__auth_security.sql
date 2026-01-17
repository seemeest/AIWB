alter table users
    add column if not exists token_version int not null default 0;

alter table refresh_tokens
    add column if not exists token_version int not null default 0;

alter table refresh_tokens
    add column if not exists user_agent varchar(512);

alter table refresh_tokens
    add column if not exists ip varchar(64);

alter table refresh_tokens
    add column if not exists device varchar(128);

alter table refresh_tokens
    add column if not exists browser varchar(128);

create table if not exists password_reset_tokens (
    token varchar(128) primary key,
    user_id uuid not null references users(id) on delete cascade,
    expires_at timestamp not null,
    created_at timestamp not null
);

create table if not exists login_audit (
    user_id uuid primary key references users(id) on delete cascade,
    last_login_at timestamp not null,
    user_agent varchar(512),
    ip varchar(64),
    device varchar(128),
    browser varchar(128),
    country varchar(64),
    region varchar(128),
    city varchar(128)
);
