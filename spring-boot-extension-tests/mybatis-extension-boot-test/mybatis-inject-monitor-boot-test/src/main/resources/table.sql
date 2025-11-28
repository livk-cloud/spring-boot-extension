create table if not exists `user`
(
    id          int not null primary key,
    username    varchar(20),
    version     int,
    insert_time timestamp,
    update_time timestamp
);
