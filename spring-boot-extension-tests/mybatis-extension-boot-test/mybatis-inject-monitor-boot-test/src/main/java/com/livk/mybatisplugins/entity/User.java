/*
 * Copyright 2021-present the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.livk.mybatisplugins.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.livk.commons.util.Jsr310Utils;
import com.livk.context.mybatis.annotation.SqlInject;
import com.livk.context.mybatis.enums.SqlFill;
import com.livk.mybatisplugins.handler.VersionInject;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @author livk
 */
@Data
public class User implements Serializable {

	@Serial
	private static final long serialVersionUID = 1L;

	private Integer id;

	private String username;

	@JsonIgnore
	@SqlInject(fill = SqlFill.INSERT, supplier = VersionInject.class)
	private Integer version;

	@JsonFormat(pattern = Jsr310Utils.NORM_DATETIME_PATTERN, timezone = "GMT+8")
	@SqlInject(fill = SqlFill.INSERT)
	private LocalDateTime insertTime;

	@JsonFormat(pattern = Jsr310Utils.NORM_DATETIME_PATTERN, timezone = "GMT+8")
	@SqlInject(fill = SqlFill.INSERT_UPDATE)
	private LocalDateTime updateTime;

}
