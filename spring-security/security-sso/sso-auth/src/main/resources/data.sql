-- auto-generated definition
create table users
(
    id       bigint auto_increment
        primary key,
    username varchar(20) not null,
    password varchar(100) null,
    constraint username
        unique (username)
);

