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
 * @date 2023/1/4
 */
@Data
@ConfigurationProperties(MybatisLogMonitorProperties.PREFIX)
public class MybatisLogMonitorProperties {

    public static final String PREFIX = "mybatis.log.monitor";

    private long timeOut = 1L;

    private ChronoUnit unit = ChronoUnit.SECONDS;
}
