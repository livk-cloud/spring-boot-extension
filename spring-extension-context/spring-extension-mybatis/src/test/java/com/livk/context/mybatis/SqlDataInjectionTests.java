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

package com.livk.context.mybatis;

import com.livk.context.mybatis.annotation.SqlInject;
import com.livk.context.mybatis.enums.SqlFill;
import com.livk.context.mybatis.handler.InjectHandle;
import lombok.Data;
import org.apache.ibatis.binding.MapperMethod;
import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.SqlCommandType;
import org.apache.ibatis.plugin.Invocation;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;
import static org.mockito.BDDMockito.given;

/**
 * @author livk
 */
@ExtendWith(MockitoExtension.class)
class SqlDataInjectionTests {

	@Mock
	private MappedStatement mappedStatement;

	@Mock
	private Executor executor;

	private SqlDataInjection sqlDataInjection;

	@BeforeEach
	void setUp() {
		sqlDataInjection = new SqlDataInjection();
	}

	@Test
	void testInterceptWithInsert() throws Throwable {
		given(mappedStatement.getSqlCommandType()).willReturn(SqlCommandType.INSERT);
		TestEntity entity = new TestEntity();
		entity.setName("test");
		Method method = Executor.class.getMethod("update", MappedStatement.class, Object.class);
		Invocation invocation = new Invocation(executor, method, new Object[] { mappedStatement, entity });
		sqlDataInjection.intercept(invocation);
		assertThat(entity.getInsertTime()).as("Insert time should be set").isNotNull();
		assertThat(entity.getUpdateTime()).as("Update time should be set").isNotNull();
	}

	@Test
	void testInterceptWithUpdate() throws Throwable {
		given(mappedStatement.getSqlCommandType()).willReturn(SqlCommandType.UPDATE);
		TestEntity entity = new TestEntity();
		entity.setName("test");
		LocalDateTime initialUpdateTime = entity.getUpdateTime();
		Method method = Executor.class.getMethod("update", MappedStatement.class, Object.class);
		Invocation invocation = new Invocation(executor, method, new Object[] { mappedStatement, entity });
		sqlDataInjection.intercept(invocation);
		assertThat(entity.getInsertTime()).as("Insert time should not be set for UPDATE").isNull();
		assertThat(entity.getUpdateTime()).as("Update time should be updated").isNotEqualTo(initialUpdateTime);
	}

	@Test
	void testInterceptWithDelete() throws Throwable {
		given(mappedStatement.getSqlCommandType()).willReturn(SqlCommandType.DELETE);
		TestEntity entity = new TestEntity();
		entity.setName("test");
		LocalDateTime initialInsertTime = entity.getInsertTime();
		LocalDateTime initialUpdateTime = entity.getUpdateTime();
		Method method = Executor.class.getMethod("update", MappedStatement.class, Object.class);
		Invocation invocation = new Invocation(executor, method, new Object[] { mappedStatement, entity });
		sqlDataInjection.intercept(invocation);
		assertThat(entity.getInsertTime()).as("Insert time should not change for DELETE").isEqualTo(initialInsertTime);
		assertThat(entity.getUpdateTime()).as("Update time should not change for DELETE").isEqualTo(initialUpdateTime);
	}

	@Test
	void testInterceptWithParamMap() throws Throwable {
		given(mappedStatement.getSqlCommandType()).willReturn(SqlCommandType.INSERT);
		TestEntity entity1 = new TestEntity();
		entity1.setName("test1");
		TestEntity entity2 = new TestEntity();
		entity2.setName("test2");
		MapperMethod.ParamMap<Object> paramMap = new MapperMethod.ParamMap<>();
		paramMap.put("param1", entity1);
		paramMap.put("param2", entity2);
		Method method = Executor.class.getMethod("update", MappedStatement.class, Object.class);
		Invocation invocation = new Invocation(executor, method, new Object[] { mappedStatement, paramMap });
		sqlDataInjection.intercept(invocation);
		assertThat(entity1.getInsertTime()).as("Entity1 insert time should be set").isNotNull();
		assertThat(entity1.getUpdateTime()).as("Entity1 update time should be set").isNotNull();
		assertThat(entity2.getInsertTime()).as("Entity2 insert time should be set").isNotNull();
		assertThat(entity2.getUpdateTime()).as("Entity2 update time should be set").isNotNull();
	}

	@Test
	void testGetValueWithCustomSupplier() throws Exception {
		Field field = null;
		try {
			field = TestEntity.class.getDeclaredField("customField");
		}
		catch (NoSuchFieldException ex) {
			fail("Test field not found");
		}
		SqlInject sqlInject = field.getAnnotation(SqlInject.class);
		Object value = getValue(sqlInject, field);
		assertThat(value).as("Custom supplier should provide 'custom-value'").isEqualTo("custom-value");
	}

	@Test
	void testGetValueWithInjectType() throws Exception {
		Field field = null;
		try {
			field = TestEntity.class.getDeclaredField("insertTime");
		}
		catch (NoSuchFieldException ex) {
			fail("Test field not found");
		}
		SqlInject sqlInject = field.getAnnotation(SqlInject.class);
		Object value = getValue(sqlInject, field);
		assertThat(value).as("InjectType handler should provide a non-null value")
			.isNotNull()
			.isInstanceOf(LocalDateTime.class);
	}

	Object getValue(SqlInject sqlInject, Field field) throws Exception {
		Method method = SqlDataInjection.class.getDeclaredMethod("getValue", SqlInject.class, Field.class);
		method.setAccessible(true);
		return method.invoke(sqlDataInjection, sqlInject, field);
	}

	static class CustomSupplier implements InjectHandle<String> {

		@Override
		public String handler() {
			return "custom-value";
		}

	}

	@Data
	private static final class TestEntity {

		private String name;

		@SqlInject(fill = SqlFill.INSERT)
		private LocalDateTime insertTime;

		@SqlInject(fill = SqlFill.INSERT_UPDATE)
		private LocalDateTime updateTime;

		@SqlInject(fill = SqlFill.INSERT_UPDATE, supplier = CustomSupplier.class)
		private String customField;

	}

}
