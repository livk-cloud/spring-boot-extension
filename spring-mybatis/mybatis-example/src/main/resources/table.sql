DROP TABLE IF EXISTS `user`;

create table `user`
(
    `id`          int not null primary key,
    `username`    varchar(20),
    `version`     int,
    `insert_time` datetime,
    `update_time` datetime
)
