package com.livk.autoconfigure.mybatis.monitor;

/**
 * <p>
 * SqlMonitor
 * </p>
 *
 * @author livk
 * @date 2023/1/4
 */
public interface SqlMonitor {
    void run(String sql, long timestamp);
}
