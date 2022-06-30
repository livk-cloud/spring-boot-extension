create table user
(
    `id`          int not null auto_increment primary key,
    `username`    varchar(20),
    `version`     int,
    `insert_time` datetime,
    `update_time` datetime
)
