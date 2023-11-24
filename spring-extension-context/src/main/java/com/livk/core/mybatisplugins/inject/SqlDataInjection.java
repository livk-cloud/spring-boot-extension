/*
 * Copyright 2021 spring-boot-extension the original author or authors.
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
 *
 */

package com.livk.core.mybatisplugins.inject;

import com.livk.commons.util.BeanUtils;
import com.livk.commons.util.ReflectionUtils;
import com.livk.core.mybatisplugins.inject.annotation.SqlFunction;
import com.livk.core.mybatisplugins.inject.enums.SqlFill;
import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.SqlCommandType;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.plugin.Intercepts;
import org.apache.ibatis.plugin.Invocation;
import org.apache.ibatis.plugin.Signature;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.List;

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
		List<Field> declaredFields = ReflectionUtils.getAllFields(parameter.getClass());
		if (!SqlCommandType.DELETE.equals(sqlCommandType)) {
			for (Field field : declaredFields) {
				if (field.isAnnotationPresent(SqlFunction.class)) {
					SqlFunction sqlFunction = field.getAnnotation(SqlFunction.class);
					Object value = getValue(sqlFunction);
					if (value == null) {
						continue;
					}
					// insert或者update并且SqlFill.INSERT_UPDATE
					if (SqlCommandType.INSERT.equals(sqlCommandType)
							|| sqlFunction.fill().equals(SqlFill.INSERT_UPDATE)) {
						Method writeMethod = ReflectionUtils.getWriteMethod(parameter.getClass(), field);
						writeMethod.invoke(parameter, value);
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
