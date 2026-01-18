create table if not exists users (
    id uuid primary key,
    email varchar(255) not null unique,
    password_hash varchar(255) not null,
    full_name varchar(255) not null,
    birth_date date not null,
    status varchar(32) not null,
    created_at timestamp not null,
    token_version int not null default 0
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
    revoked boolean not null,
    token_version int not null default 0,
    user_agent varchar(512),
    ip varchar(64),
    device varchar(128),
    browser varchar(128)
);

create table if not exists email_verification_tokens (
    token varchar(128) primary key,
    user_id uuid not null references users(id) on delete cascade,
    expires_at timestamp not null,
    created_at timestamp not null
);

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

create table if not exists orders (
    id uuid primary key,
    buyer_id uuid not null references users(id),
    status varchar(32) not null,
    total_amount numeric(12, 2) not null,
    created_at timestamp not null,
    updated_at timestamp not null
);

create table if not exists order_items (
    id uuid primary key,
    order_id uuid not null references orders(id) on delete cascade,
    product_id uuid not null references products(id),
    seller_id uuid not null references users(id),
    quantity int not null,
    price numeric(12, 2) not null
);

create table if not exists payments (
    id uuid primary key,
    order_id uuid not null references orders(id),
    provider varchar(32) not null,
    external_id varchar(128) not null,
    status varchar(32) not null,
    amount numeric(12, 2) not null,
    created_at timestamp not null
);

create table if not exists shipments (
    id uuid primary key,
    order_id uuid not null references orders(id),
    current_status varchar(32) not null,
    created_at timestamp not null
);

create table if not exists shipment_status_history (
    id uuid primary key,
    order_id uuid not null references orders(id),
    status varchar(32) not null,
    created_at timestamp not null
);

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

create table if not exists notifications (
    id uuid primary key,
    user_id uuid not null,
    channel varchar(32) not null,
    type varchar(64) not null,
    title varchar(255) not null,
    message varchar(1024) not null,
    link varchar(512),
    status varchar(32) not null,
    created_at timestamp with time zone not null,
    sent_at timestamp with time zone,
    read_at timestamp with time zone
);

create index if not exists idx_notifications_user_created on notifications (user_id, created_at desc);

create table if not exists product_views (
    id uuid primary key,
    product_id uuid not null references products(id),
    seller_id uuid not null references users(id),
    viewer_id uuid,
    session_id varchar(128),
    viewed_at timestamp with time zone not null
);

create index if not exists idx_product_views_seller_time on product_views (seller_id, viewed_at desc);
create index if not exists idx_product_views_product_time on product_views (product_id, viewed_at desc);

create table if not exists search_impressions (
    id uuid primary key,
    product_id uuid not null references products(id),
    seller_id uuid not null references users(id),
    query varchar(255) not null,
    position int not null,
    shown_at timestamp with time zone not null
);

create index if not exists idx_search_impressions_seller_time on search_impressions (seller_id, shown_at desc);
create index if not exists idx_search_impressions_query on search_impressions (query);
