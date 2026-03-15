create table if not exists members (
    member_id uuid primary key,
    provider varchar(20) not null,
    provider_user_id varchar(120) not null,
    email varchar(255) not null,
    nickname varchar(20) not null,
    role varchar(20) not null,
    created_at timestamptz not null,
    constraint ck_members_provider check (provider in ('GOOGLE')),
    constraint ck_members_role check (role in ('USER', 'ADMIN')),
    constraint uq_members_provider_user unique (provider, provider_user_id),
    constraint uq_members_email unique (email)
);
