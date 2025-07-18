create table if not exists `user`
(
    id          int not null auto_increment primary key,
    username    varchar(20),
    insert_time datetime,
    update_time datetime
);
