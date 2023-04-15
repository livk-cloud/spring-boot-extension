package com.livk.autoconfigure.mybatis.monitor;

import com.livk.commons.function.FieldFunc;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.time.temporal.ChronoUnit;
import java.util.Properties;

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

    public static String timeOutName() {
        return FieldFunc.getName(MybatisLogMonitorProperties::getTimeOut);
    }

    public static String unitName() {
        return FieldFunc.getName(MybatisLogMonitorProperties::getUnit);
    }

    public Properties properties() {
        Properties properties = new Properties();
        properties.put(timeOutName(), timeOut);
        properties.put(unitName(), unit);
        return properties;
    }
}
