package com.livk.mybatis.example;

import com.livk.autoconfigure.mybatis.monitor.SqlMonitor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * <p>
 * SqlTimeOutHandler
 * </p>
 *
 * @author livk
 * @date 2023/1/4
 */
@Slf4j
@Component
public class SqlTimeOutHandler implements SqlMonitor {
    @Override
    public void run(String sql, long timestamp) {
        log.error("{SQL超时 SQL:[{}],Time:[{}ms]}", sql, timestamp);
    }
}
