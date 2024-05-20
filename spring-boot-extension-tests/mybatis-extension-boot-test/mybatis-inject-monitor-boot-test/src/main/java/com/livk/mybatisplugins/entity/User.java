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

package com.livk.mybatisplugins.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.livk.commons.util.DateUtils;
import com.livk.context.mybatisplugins.inject.annotation.SqlFunction;
import com.livk.context.mybatisplugins.inject.enums.FunctionType;
import com.livk.context.mybatisplugins.inject.enums.SqlFill;
import com.livk.mybatisplugins.handler.VersionFunction;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 * User
 * </p>
 *
 * @author livk
 */
@Data
public class User implements Serializable {

	@Serial
	private static final long serialVersionUID = 1L;

	private Integer id;

	private String username;

	@JsonIgnore
	@SqlFunction(fill = SqlFill.INSERT, supplier = VersionFunction.class)
	private Integer version;

	@JsonFormat(pattern = DateUtils.YMD_HMS, timezone = "GMT+8")
	@SqlFunction(fill = SqlFill.INSERT, time = FunctionType.DATE)
	private Date insertTime;

	@JsonFormat(pattern = DateUtils.YMD_HMS, timezone = "GMT+8")
	@SqlFunction(fill = SqlFill.INSERT_UPDATE, time = FunctionType.DATE)
	private Date updateTime;

}
