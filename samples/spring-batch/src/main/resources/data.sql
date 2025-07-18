create table sys_user
(
    id          bigint auto_increment
        primary key,
    user_name   varchar(55)  null,
    sex         tinyint      null,
    age         int          null,
    address     varchar(255) null,
    status      tinyint      null,
    create_time datetime     null,
    update_time datetime     null
);

