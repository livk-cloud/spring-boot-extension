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

import com.livk.commons.SpringContextHolder;
import com.livk.commons.util.SqlParserUtils;
import com.livk.context.mybatis.event.MonitorSQLInfo;
import com.livk.context.mybatis.event.MonitorSQLTimeOutEvent;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.executor.statement.StatementHandler;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.plugin.Intercepts;
import org.apache.ibatis.plugin.Invocation;
import org.apache.ibatis.plugin.Signature;
import org.springframework.util.Assert;

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
		@Signature(type = StatementHandler.class, method = "prepare", args = { Connection.class, Integer.class }) })
public class MybatisSqlMonitor implements Interceptor {

	private Properties properties;

	@Override
	public Object intercept(Invocation invocation) throws Throwable {
		long start = System.currentTimeMillis();
		Object proceed = invocation.proceed();
		if (properties != null) {
			long time = System.currentTimeMillis() - start;
			String sql = SqlParserUtils.formatSql(((StatementHandler) invocation.getTarget()).getBoundSql().getSql());
			if (time > timeOut()) {
				log.warn("{SQL execution timeout SQL:[{}],Time:[{}ms]}", sql, time);
				MonitorSQLInfo monitorSQLInfo = new MonitorSQLInfo(sql, time, proceed);
				SpringContextHolder.publishEvent(new MonitorSQLTimeOutEvent(monitorSQLInfo));
			}
		}
		else {
			log.warn("MybatisLogMonitor properties is null. Monitor disabled");
		}
		return proceed;
	}

	@Override
	public void setProperties(Properties properties) {
		Assert.isTrue(properties.containsKey("timeOut"), "timeOut is required");
		Assert.isTrue(properties.containsKey("unit"), "unit is required");
		this.properties = properties;
	}

	private long timeOut() {
		return ((ChronoUnit) properties.get("unit")).getDuration().toMillis() * (Long) properties.get("timeOut");
	}

}
