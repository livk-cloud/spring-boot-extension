package com.livk.autoconfigure.mybatis.monitor.interceptor;

import com.livk.autoconfigure.mybatis.monitor.MybatisLogMonitorProperties;
import com.livk.autoconfigure.mybatis.monitor.event.MonitorSQLInfo;
import com.livk.autoconfigure.mybatis.monitor.event.MonitorSQLTimeOutEvent;
import com.livk.autoconfigure.mybatis.util.SqlUtils;
import com.livk.commons.spring.context.SpringContextHolder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.executor.statement.StatementHandler;
import org.apache.ibatis.plugin.*;

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
        String sql = SqlUtils.formatSql(((StatementHandler) invocation.getTarget()).getBoundSql().getSql());
        if (time > timeOut()) {
            log.warn("{SQL超时 SQL:[{}],Time:[{}ms]}", sql, time);
            MonitorSQLInfo monitorSQLInfo = MonitorSQLInfo.of(sql, time, proceed);
            SpringContextHolder.publishEvent(new MonitorSQLTimeOutEvent(monitorSQLInfo));
        }
        return proceed;
    }

    @Override
    public Object plugin(Object target) {
        return Plugin.wrap(target, this);
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
