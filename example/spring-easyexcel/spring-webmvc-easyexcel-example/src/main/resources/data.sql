create table if not exists info
(
    `id`    bigint(16) not null auto_increment comment 'id',
    `phone` char(11) not null,
    primary key (`id`)
);
