create table if not exists `user`
(
    `id`       bigint not null auto_increment primary key,
    `username` varchar(32),
    `password` varchar(64),
    `des`      json
);
