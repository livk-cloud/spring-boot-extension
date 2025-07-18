create table if not exists user
(
    id       bigint auto_increment
        primary key,
    username varchar(32) null,
    password varchar(64) null
);

