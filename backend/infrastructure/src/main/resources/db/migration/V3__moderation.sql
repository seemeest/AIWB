create table if not exists complaints (
    id uuid primary key,
    author_id uuid not null references users(id),
    product_id uuid not null references products(id),
    reason text not null,
    status varchar(32) not null,
    created_at timestamp not null
);

create table if not exists moderation_actions (
    id uuid primary key,
    moderator_id uuid not null references users(id),
    target_type varchar(32) not null,
    target_id uuid not null,
    action varchar(64) not null,
    reason text not null,
    created_at timestamp not null
);

create table if not exists blocks (
    id uuid primary key,
    target_type varchar(32) not null,
    target_id uuid not null,
    reason text not null,
    created_at timestamp not null,
    until_at timestamp
);

create table if not exists appeals (
    id uuid primary key,
    block_id uuid not null references blocks(id),
    author_id uuid not null references users(id),
    reason text not null,
    status varchar(32) not null,
    created_at timestamp not null
);
