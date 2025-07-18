create table if not exists "user"
(
    id       bigserial
        primary key,
    username varchar(32),
    password varchar(64)
);
