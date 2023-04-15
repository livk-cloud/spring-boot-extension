package com.livk.autoconfigure.mybatis.monitor.event;

/**
 * The type Monitor sql info.
 *
 * @author livk
 */
public record MonitorSQLInfo(String sql, Long timeout, Object result) {

}
