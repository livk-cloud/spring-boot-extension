package com.livk.autoconfigure.mybatis.monitor.event;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * The type Monitor sql info.
 *
 * @author livk
 */
@Data
@AllArgsConstructor(staticName = "of")
public class MonitorSQLInfo {

    private String sql;

    private Long timeout;

    private Object result;
}
