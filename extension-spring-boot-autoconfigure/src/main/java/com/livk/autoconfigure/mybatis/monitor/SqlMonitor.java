package com.livk.autoconfigure.mybatis.monitor;

/**
 * <p>
 * SqlMonitor
 * </p>
 *
 * @author livk
 */
public interface SqlMonitor {
    /**
     * Run.
     *
     * @param sql       the sql
     * @param timestamp the timestamp
     */
    void run(String sql, long timestamp);
}
