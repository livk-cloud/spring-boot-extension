create table menu
(
    id   int auto_increment
        primary key,
    name varchar(32)   not null,
    pid  int default 0 not null
);

INSERT INTO menu (id, name, pid)
VALUES (1, '电影', 0),
       (2, '南方车站的聚会', 1),
       (3, '时速二十五', 1),
       (4, '游戏', 0),
       (5, '超级马里奥', 4),
       (6, '如龙', 4),
       (7, '导演', 2),
       (8, '演员', 2);
