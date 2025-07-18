create table if not exists users
(
    id       bigint       null,
    username varchar(20)  null,
    password varchar(100) null
);

INSERT INTO users (id, username, password)
VALUES (1, 'livk', '$2a$10$LepUx6I/1y0Pc614ZqSK6eXvoMbNDjdjAKqV/GQ4C97b0pw/kiuBC');
