/*
 * Copyright 2021 spring-boot-extension the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

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

    /**
     * Time out name string.
     *
     * @return the string
     */
    public static String timeOutName() {
        return FieldFunc.getName(MybatisLogMonitorProperties::getTimeOut);
    }

    /**
     * Unit name string.
     *
     * @return the string
     */
    public static String unitName() {
        return FieldFunc.getName(MybatisLogMonitorProperties::getUnit);
    }

    /**
     * Properties properties.
     *
     * @return the properties
     */
    public Properties properties() {
        Properties properties = new Properties();
        properties.put(timeOutName(), timeOut);
        properties.put(unitName(), unit);
        return properties;
    }
}
