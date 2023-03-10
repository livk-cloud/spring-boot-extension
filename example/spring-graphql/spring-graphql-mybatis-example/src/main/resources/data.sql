create table if not exists author
(
    id         bigint      not null auto_increment primary key,
    id_card_no varchar(32) not null,
    name       varchar(16),
    age        int default 0
);


create table if not exists book
(
    id                bigint      not null auto_increment primary key,
    isbn              varchar(32) not null,
    title             varchar(32),
    pages             int,
    author_id_card_no varchar(32) not null
);
