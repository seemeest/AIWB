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
