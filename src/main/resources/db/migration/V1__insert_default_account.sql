create table IF NOT EXISTS account(
    id serial primary key,
    balance decimal NOT NULL,
    created_at timestamp,
    updated_at timestamp
);
insert into account(balance, created_at, updated_at) values(100000, now(), now());