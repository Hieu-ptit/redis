create table employee
(
    id         serial8     not null
        constraint employee_pkey
            primary key,
    name       varchar(20) not null,
    metadata   jsonb,
    company    jsonb,
    boxes      jsonb,
    created_at timestamptz default CURRENT_TIMESTAMP,
    updated_at timestamptz default CURRENT_TIMESTAMP
);

create table company
(
    id   serial8     not null
        constraint company_pkey
            primary key,
    name varchar(20) not null
);

create table account
(
    id              serial8       not null
        constraint account_pkey
            primary key,
    name            varchar(100)  not null,
    email           varchar(100)  not null unique,
    password        varchar(1024) not null,
    activation_code varchar(50),
    deleted         boolean       not null default false,
    activated       boolean       not null default false,
    created_at      timestamptz            default CURRENT_TIMESTAMP,
    updated_at      timestamptz            default CURRENT_TIMESTAMP
);

create table config
(
    id              serial8 not null
        constraint config_pkey
            primary key,
    key varchar(30) not null,
    value jsonb,
    created_at      timestamptz default CURRENT_TIMESTAMP,
    updated_at      timestamptz default CURRENT_TIMESTAMP
);