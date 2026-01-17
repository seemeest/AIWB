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
