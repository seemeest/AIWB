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
