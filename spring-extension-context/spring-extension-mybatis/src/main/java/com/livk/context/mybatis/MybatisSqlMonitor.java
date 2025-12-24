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

import com.livk.context.mybatis.event.MonitorSQLInfo;
import com.livk.context.mybatis.event.MonitorSQLTimeOutEvent;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import net.sf.jsqlparser.parser.CCJSqlParserUtil;
import org.apache.ibatis.executor.statement.StatementHandler;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.ParameterMapping;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.plugin.Intercepts;
import org.apache.ibatis.plugin.Invocation;
import org.apache.ibatis.plugin.Signature;
import org.apache.ibatis.reflection.MetaObject;
import org.apache.ibatis.reflection.SystemMetaObject;
import org.springframework.context.ApplicationContext;
import org.springframework.format.annotation.DurationFormat;
import org.springframework.format.datetime.standard.DurationFormatterUtils;

import java.sql.Connection;
import java.time.Duration;
import java.time.temporal.Temporal;
import java.util.Date;
import java.util.List;
import java.util.Properties;

/**
 * @author livk
 */
@Slf4j
@Intercepts({
		@Signature(type = StatementHandler.class, method = "prepare", args = { Connection.class, Integer.class }) })
@RequiredArgsConstructor
public class MybatisSqlMonitor implements Interceptor {

	private final ApplicationContext applicationContext;

	@Setter
	private Properties properties;

	@Override
	public Object intercept(Invocation invocation) throws Throwable {
		long start = System.currentTimeMillis();
		Object proceed = invocation.proceed();
		if (properties != null) {
			long time = System.currentTimeMillis() - start;
			StatementHandler handler = (StatementHandler) invocation.getTarget();
			BoundSql boundSql = handler.getBoundSql();
			String sql = getCompleteSql(boundSql);
			if (time > timeOut()) {
				log.warn("{SQL execution timeout SQL:[{}],Time:[{}ms]}", sql, time);
				MonitorSQLInfo monitorSQLInfo = new MonitorSQLInfo(sql, time);
				applicationContext.publishEvent(new MonitorSQLTimeOutEvent(monitorSQLInfo));
			}
		}
		else {
			log.warn("MybatisLogMonitor properties is null. Monitor disabled");
		}
		return proceed;
	}

	private long timeOut() {
		Object raw = properties.get("timeOut");
		if (raw instanceof Duration d) {
			return d.toMillis();
		}
		if (raw instanceof Number n) {
			return n.longValue();
		}
		if (raw instanceof String s) {
			return parseStringTimeout(s);
		}
		throw new IllegalArgumentException("Unsupported timeOut type: " + raw.getClass().getName());
	}

	private long parseStringTimeout(String value) {
		String v = value.trim();
		try {
			return Long.parseLong(v);
		}
		catch (NumberFormatException ignored) {
		}

		try {
			return DurationFormatterUtils.parse(v, DurationFormat.Style.COMPOSITE).toMillis();
		}
		catch (Exception ignored) {
		}
		try {
			return Duration.parse(v).toMillis();
		}
		catch (Exception ignored) {
		}
		throw new IllegalArgumentException("Invalid timeOut value: " + value);
	}

	@SneakyThrows
	private String formatSql(String sql) {
		return CCJSqlParserUtil.parse(sql).toString();
	}

	private String getCompleteSql(BoundSql boundSql) {
		String sql = boundSql.getSql().replaceAll("\\s+", " ");

		Object parameterObject = boundSql.getParameterObject();
		List<ParameterMapping> parameterMappings = boundSql.getParameterMappings();

		if (parameterMappings == null || parameterMappings.isEmpty()) {
			return sql;
		}

		MetaObject metaObject = parameterObject == null ? null : SystemMetaObject.forObject(parameterObject);

		for (ParameterMapping mapping : parameterMappings) {
			String propertyName = mapping.getProperty();
			Object value;

			if (boundSql.hasAdditionalParameter(propertyName)) {
				value = boundSql.getAdditionalParameter(propertyName);
			}
			else if (metaObject != null && metaObject.hasGetter(propertyName)) {
				value = metaObject.getValue(propertyName);
			}
			else {
				value = null;
			}

			sql = sql.replaceFirst("\\?", formatParameter(value));
		}

		return formatSql(sql);
	}

	private String formatParameter(Object value) {
		return switch (value) {
			case null -> "NULL";
			case String str -> "'" + str.replace("'", "''") + "'";
			case Date date -> "'" + date + "'";
			case Temporal temporal -> "'" + temporal + "'";
			default -> value.toString();
		};

	}

}
