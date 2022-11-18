create table "user"
(
    "id"       bigserial not null primary key,
    "username" varchar(32),
    "password" varchar(64),
    "des"      json
);
