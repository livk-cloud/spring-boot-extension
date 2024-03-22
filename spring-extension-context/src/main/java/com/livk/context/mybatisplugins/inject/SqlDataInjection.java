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

package com.livk.context.mybatisplugins.inject;

import com.livk.commons.util.BeanUtils;
import com.livk.commons.util.ReflectionUtils;
import com.livk.context.mybatisplugins.inject.annotation.SqlFunction;
import com.livk.context.mybatisplugins.inject.enums.SqlFill;
import org.apache.ibatis.binding.MapperMethod;
import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.SqlCommandType;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.plugin.Intercepts;
import org.apache.ibatis.plugin.Invocation;
import org.apache.ibatis.plugin.Signature;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 仅支持Mybatis
 * </p>
 *
 * @author livk
 */
@Intercepts({ @Signature(type = Executor.class, method = "update", args = { MappedStatement.class, Object.class }) })
public class SqlDataInjection implements Interceptor {

	@Override
	public Object intercept(Invocation invocation) throws Throwable {
		MappedStatement mappedStatement = (MappedStatement) invocation.getArgs()[0];
		SqlCommandType sqlCommandType = mappedStatement.getSqlCommandType();
		Object parameter = invocation.getArgs()[1];
		Map<Object, List<Field>> map = new HashMap<>();
		if (parameter instanceof MapperMethod.ParamMap<?> paramMap) {
			for (Object value : paramMap.values()) {
				map.putIfAbsent(value, ReflectionUtils.getAllFields(value.getClass()));
			}
		}
		else {
			map.putIfAbsent(parameter, ReflectionUtils.getAllFields(parameter.getClass()));
		}

		for (Map.Entry<Object, List<Field>> entry : map.entrySet()) {
			if (!SqlCommandType.DELETE.equals(sqlCommandType)) {
				for (Field field : entry.getValue()) {
					if (field.isAnnotationPresent(SqlFunction.class)) {
						SqlFunction sqlFunction = field.getAnnotation(SqlFunction.class);
						Object value = getValue(sqlFunction);
						if (value == null) {
							continue;
						}
						// insert或者update并且SqlFill.INSERT_UPDATE
						if (SqlCommandType.INSERT.equals(sqlCommandType)
								|| sqlFunction.fill().equals(SqlFill.INSERT_UPDATE)) {
							Method writeMethod = ReflectionUtils.getWriteMethod(entry.getKey().getClass(), field);
							writeMethod.invoke(entry.getKey(), value);
						}
					}
				}
			}
		}
		return invocation.proceed();
	}

	private Object getValue(SqlFunction sqlFunction) {
		Object value = sqlFunction.time().handler();
		return value != null ? value : BeanUtils.instantiateClass(sqlFunction.supplier()).handler();
	}

}
