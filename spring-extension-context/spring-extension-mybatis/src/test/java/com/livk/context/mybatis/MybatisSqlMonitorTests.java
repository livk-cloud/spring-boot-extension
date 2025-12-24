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

import com.livk.context.mybatis.event.MonitorSQLTimeOutEvent;
import org.apache.ibatis.executor.statement.StatementHandler;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.ParameterMapping;
import org.apache.ibatis.plugin.Invocation;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.event.ApplicationEvents;
import org.springframework.test.context.event.RecordApplicationEvents;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

import org.apache.ibatis.type.TypeHandler;
import org.apache.ibatis.type.TypeHandlerRegistry;

import java.time.Duration;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

/**
 * @author livk
 */
@SpringJUnitConfig
@RecordApplicationEvents
class MybatisSqlMonitorTests {

	@Autowired
	MybatisSqlMonitor monitor;

	@Autowired
	ApplicationEvents events;

	Invocation invocation;

	StatementHandler statementHandler;

	BoundSql boundSql;

	@BeforeEach
	void init() {
		invocation = mock(Invocation.class);
		statementHandler = mock(StatementHandler.class);
		boundSql = mock(BoundSql.class);
	}

	@Test
	void testInterceptWithTimeout() throws Throwable {
		Properties properties = new Properties();
		properties.setProperty("timeOut", "10ms");
		monitor.setProperties(properties);

		given(invocation.getTarget()).willReturn(statementHandler);
		given(statementHandler.getBoundSql()).willReturn(boundSql);
		given(boundSql.getSql()).willReturn("select * from user");
		given(invocation.proceed()).willAnswer(invocation -> {
			Thread.sleep(50);
			return null;
		});

		monitor.intercept(invocation);

		assertThat(events.stream(MonitorSQLTimeOutEvent.class)).hasSize(1);
	}

	@Test
	void testInterceptWithoutTimeout() throws Throwable {
		Properties properties = new Properties();
		properties.setProperty("timeOut", "100ms");
		monitor.setProperties(properties);

		given(invocation.proceed()).willReturn(null);
		given(statementHandler.getBoundSql()).willReturn(boundSql);
		given(invocation.getTarget()).willReturn(statementHandler);
		given(boundSql.getSql()).willReturn("select * from user");

		monitor.intercept(invocation);

		assertThat(events.stream(MonitorSQLTimeOutEvent.class)).isEmpty();
	}

	@Test
	void testInterceptWithoutProperties() throws Throwable {
		monitor.setProperties(null);
		monitor.intercept(invocation);
		assertThat(events.stream(MonitorSQLTimeOutEvent.class)).isEmpty();
	}

	@Test
	void testTimeOutWithDuration() throws Throwable {
		Properties properties = new Properties();
		properties.put("timeOut", Duration.ofMillis(1));
		monitor.setProperties(properties);

		given(invocation.getTarget()).willReturn(statementHandler);
		given(statementHandler.getBoundSql()).willReturn(boundSql);
		given(boundSql.getSql()).willReturn("select 1");
		given(invocation.proceed()).willAnswer(invocation -> {
			Thread.sleep(10);
			return null;
		});

		monitor.intercept(invocation);

		assertThat(events.stream(MonitorSQLTimeOutEvent.class)).hasSize(1);
	}

	@Test
	void testTimeOutWithNumber() throws Throwable {
		Properties properties = new Properties();
		properties.put("timeOut", 1L);
		monitor.setProperties(properties);

		given(invocation.getTarget()).willReturn(statementHandler);
		given(statementHandler.getBoundSql()).willReturn(boundSql);
		given(boundSql.getSql()).willReturn("select 1");
		given(invocation.proceed()).willAnswer(invocation -> {
			Thread.sleep(10);
			return null;
		});

		monitor.intercept(invocation);

		assertThat(events.stream(MonitorSQLTimeOutEvent.class)).hasSize(1);
	}

	@Test
	void testTimeOutWithStringNumber() throws Throwable {
		Properties properties = new Properties();
		properties.setProperty("timeOut", "1");
		monitor.setProperties(properties);

		given(invocation.getTarget()).willReturn(statementHandler);
		given(statementHandler.getBoundSql()).willReturn(boundSql);
		given(boundSql.getSql()).willReturn("select 1");
		given(invocation.proceed()).willAnswer(invocation -> {
			Thread.sleep(10);
			return null;
		});

		monitor.intercept(invocation);

		assertThat(events.stream(MonitorSQLTimeOutEvent.class)).hasSize(1);
	}

	@Test
	@SuppressWarnings("unchecked")
	void testSqlParameterReplacement() throws Throwable {
		Properties properties = new Properties();
		properties.setProperty("timeOut", "0");
		monitor.setProperties(properties);

		String sql = "select * from user where id = ? and name = ? and date = ? and null_val = ? and extra = ?";
		given(invocation.getTarget()).willReturn(statementHandler);
		given(statementHandler.getBoundSql()).willReturn(boundSql);
		given(boundSql.getSql()).willReturn(sql);
		given(invocation.proceed()).willAnswer(invocation -> {
			Thread.sleep(10);
			return null;
		});

		org.apache.ibatis.session.Configuration configuration = mock(org.apache.ibatis.session.Configuration.class);
		TypeHandlerRegistry typeHandlerRegistry = mock(TypeHandlerRegistry.class);
		given(configuration.getTypeHandlerRegistry()).willReturn(typeHandlerRegistry);
		given(typeHandlerRegistry.getTypeHandler(any(Class.class), any())).willReturn(mock(TypeHandler.class));

		List<ParameterMapping> mappings = List.of(
				new ParameterMapping.Builder(configuration, "id", Object.class).build(),
				new ParameterMapping.Builder(configuration, "name", Object.class).build(),
				new ParameterMapping.Builder(configuration, "date", Object.class).build(),
				new ParameterMapping.Builder(configuration, "nullVal", Object.class).build(),
				new ParameterMapping.Builder(configuration, "extra", Object.class).build());
		given(boundSql.getParameterMappings()).willReturn(mappings);

		Map<String, Object> params = new HashMap<>();
		params.put("id", 1L);
		params.put("name", "livk");
		params.put("date", new Date());
		params.put("nullVal", null);

		given(boundSql.getParameterObject()).willReturn(params);
		given(boundSql.hasAdditionalParameter("extra")).willReturn(true);
		given(boundSql.getAdditionalParameter("extra")).willReturn("extraValue");

		monitor.intercept(invocation);

		MonitorSQLTimeOutEvent event = events.stream(MonitorSQLTimeOutEvent.class).findFirst().orElseThrow();
		String executedSql = event.getSource().sql();
		assertThat(executedSql).contains("id = 1");
		assertThat(executedSql).contains("name = 'livk'");
		assertThat(executedSql).contains("date = '");
		assertThat(executedSql).contains("null_val = NULL");
		assertThat(executedSql).contains("extra = 'extraValue'");
	}

	@Test
	void testInvalidTimeOut() {
		Properties properties = new Properties();
		properties.setProperty("timeOut", "invalid");
		monitor.setProperties(properties);

		given(invocation.getTarget()).willReturn(statementHandler);
		given(statementHandler.getBoundSql()).willReturn(boundSql);
		given(boundSql.getSql()).willReturn("select 1");

		assertThatThrownBy(() -> monitor.intercept(invocation)).isInstanceOf(IllegalArgumentException.class);
	}

	@Configuration
	@Import(MybatisSqlMonitor.class)
	static class Config {

	}

}
