create table if not exists users (
    id uuid primary key,
    email varchar(255) not null unique,
    password_hash varchar(255) not null,
    status varchar(32) not null,
    created_at timestamp not null
);

create table if not exists user_roles (
    user_id uuid not null references users(id) on delete cascade,
    role varchar(32) not null
);

create table if not exists refresh_tokens (
    token varchar(512) primary key,
    user_id uuid not null references users(id) on delete cascade,
    expires_at timestamp not null,
    created_at timestamp not null,
    revoked boolean not null
);

create table if not exists email_verification_tokens (
    token varchar(128) primary key,
    user_id uuid not null references users(id) on delete cascade,
    expires_at timestamp not null,
    created_at timestamp not null
);

create table if not exists products (
    id uuid primary key,
    seller_id uuid not null references users(id),
    title varchar(255) not null,
    description text,
    price numeric(12, 2) not null,
    quantity int not null,
    status varchar(32) not null,
    created_at timestamp not null,
    updated_at timestamp not null
);

create table if not exists product_images (
    id uuid primary key,
    product_id uuid not null references products(id) on delete cascade,
    path varchar(512) not null,
    sort_order int not null,
    created_at timestamp not null
);
