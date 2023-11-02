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

package com.livk.autoconfigure.mybatisplugins.monitor.interceptor;

import com.livk.autoconfigure.mybatisplugins.monitor.MybatisLogMonitorProperties;
import com.livk.autoconfigure.mybatisplugins.monitor.event.MonitorSQLInfo;
import com.livk.autoconfigure.mybatisplugins.monitor.event.MonitorSQLTimeOutEvent;
import com.livk.commons.spring.context.SpringContextHolder;
import com.livk.commons.util.SqlParserUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.executor.statement.StatementHandler;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.plugin.Intercepts;
import org.apache.ibatis.plugin.Invocation;
import org.apache.ibatis.plugin.Signature;

import java.sql.Connection;
import java.time.temporal.ChronoUnit;
import java.util.Properties;

/**
 * <p>
 * MybatisLogMonitor
 * </p>
 *
 * @author livk
 */
@Slf4j
@Intercepts({
	@Signature(
		type = StatementHandler.class,
		method = "prepare",
		args = {Connection.class, Integer.class}
	)
})
@RequiredArgsConstructor
public class MybatisLogMonitor implements Interceptor {

	private Properties properties;

	@Override
	public Object intercept(Invocation invocation) throws Throwable {
		long start = System.currentTimeMillis();
		Object proceed = invocation.proceed();
		long time = System.currentTimeMillis() - start;
		String sql = SqlParserUtils.formatSql(((StatementHandler) invocation.getTarget()).getBoundSql().getSql());
		if (time > timeOut()) {
			log.warn("{SQL超时 SQL:[{}],Time:[{}ms]}", sql, time);
			MonitorSQLInfo monitorSQLInfo = new MonitorSQLInfo(sql, time, proceed);
			SpringContextHolder.publishEvent(new MonitorSQLTimeOutEvent(monitorSQLInfo));
		}
		return proceed;
	}

	@Override
	public void setProperties(Properties properties) {
		this.properties = properties;
	}

	private long timeOut() {
		return ((ChronoUnit) properties.get(MybatisLogMonitorProperties.unitName()))
				   .getDuration()
				   .toMillis() *
			   (Long) properties.get(MybatisLogMonitorProperties.timeOutName());
	}
}
