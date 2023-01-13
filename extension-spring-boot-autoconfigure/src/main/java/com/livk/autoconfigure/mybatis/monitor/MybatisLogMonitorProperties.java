package com.livk.autoconfigure.mybatis.monitor;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.time.temporal.ChronoUnit;

/**
 * <p>
 * MybatisLogMonitorProperties
 * </p>
 *
 * @author livk
 */
@Data
@ConfigurationProperties(MybatisLogMonitorProperties.PREFIX)
public class MybatisLogMonitorProperties {

    /**
     * The constant PREFIX.
     */
    public static final String PREFIX = "mybatis.log.monitor";

    private long timeOut = 1L;

    private ChronoUnit unit = ChronoUnit.SECONDS;
}
