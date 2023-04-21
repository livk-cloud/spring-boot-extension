/*
 * Copyright 2021 spring-boot-extension the original author or authors.
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
 *
 */

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
