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

package com.livk.ck.r2dbc.entity;

import com.google.common.base.CaseFormat;
import com.livk.commons.util.ReflectionUtils;
import io.r2dbc.spi.ColumnMetadata;
import io.r2dbc.spi.Row;
import io.r2dbc.spi.RowMetadata;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;
import org.springframework.format.annotation.DateTimeFormat;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.time.LocalDate;

/**
 * @author livk
 */
@Data
@Table("user")
public class User {

	@Id
	private Integer id;

	private String appId;

	private String version;

	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private LocalDate regTime;

	public static User collect(Row row, RowMetadata rowMetadata) {
		User user = new User();
		for (ColumnMetadata columnMetadata : rowMetadata.getColumnMetadatas()) {
			String name = columnMetadata.getName();
			String fieldName = CaseFormat.LOWER_UNDERSCORE.to(CaseFormat.LOWER_CAMEL, name);
			Field field = ReflectionUtils.findField(User.class, fieldName);
			if (field != null) {
				Method writeMethod = ReflectionUtils.getWriteMethod(User.class, field);
				try {
					writeMethod.invoke(user, row.get(name, field.getType()));
				}
				catch (InvocationTargetException | IllegalAccessException e) {
					throw new IllegalArgumentException(e);
				}
			}
		}
		return user;
	}

}
