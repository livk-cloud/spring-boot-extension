package com.livk.autoconfigure.mybatis.monitor;

import com.livk.autoconfigure.mybatis.util.SqlUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.executor.statement.StatementHandler;
import org.apache.ibatis.plugin.*;
import org.springframework.beans.factory.ObjectProvider;

import java.sql.Connection;

/**
 * <p>
 * MybatisLogMonitor
 * </p>
 *
 * @author livk
 * @date 2023/1/4
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

    private final MybatisLogMonitorProperties properties;

    private final ObjectProvider<SqlMonitor> monitorList;

    @Override
    public Object intercept(Invocation invocation) throws Throwable {
        long start = System.currentTimeMillis();
        Object proceed = invocation.proceed();
        long time = System.currentTimeMillis() - start;
        String sql = SqlUtils.formatSql(((StatementHandler) invocation.getTarget()).getBoundSql().getSql());
        long timeOut = properties.getUnit().getDuration().toMillis() * properties.getTimeOut();
        if (time > timeOut) {
            log.warn("{SQL超时 SQL:[{}],Time:[{}ms]}", sql, time);
            monitorList.orderedStream().forEach(sqlMonitor -> sqlMonitor.run(sql, time));
        }
        return proceed;
    }

    @Override
    public Object plugin(Object target) {
        return Plugin.wrap(target, this);
    }
}
