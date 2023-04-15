package com.livk.mybatis.example;

import com.livk.autoconfigure.mybatis.monitor.event.MonitorSQLInfo;
import com.livk.autoconfigure.mybatis.monitor.event.MonitorSQLTimeOutEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

/**
 * <p>
 * SqlTimeOutHandler
 * </p>
 *
 * @author livk
 */
@Slf4j
@Component
public class SqlTimeOutHandler implements ApplicationListener<MonitorSQLTimeOutEvent> {

    @Override
    public void onApplicationEvent(MonitorSQLTimeOutEvent event) {
        MonitorSQLInfo info = event.info();
        log.error("{SQL超时 SQL:[{}],Time:[{}ms],result:[{}]}", info.sql(), info.timeout(), info.result());
    }
}
