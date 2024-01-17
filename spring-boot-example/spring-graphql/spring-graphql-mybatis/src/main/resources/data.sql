/*
 * Copyright 2021-2024 spring-boot-extension the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

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
