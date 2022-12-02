package com.livk.autoconfigure.dynamic.datasource;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.util.Assert;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * DynamicDatasourceProperties
 * </p>
 *
 * @author livk
 * @date 2022/11/21
 */
@Data
@ConfigurationProperties(DynamicDatasourceProperties.PREFIX)
public class DynamicDatasourceProperties implements InitializingBean {

    public static final String PREFIX = "spring.dynamic";

    private Map<String, PrimaryProperties> datasource;

    private String primaryName;

    @Override
    public void afterPropertiesSet() {
        if (datasource.size() == 1) {
            primaryName = datasource.keySet()
                    .stream()
                    .findFirst()
                    .orElse(null);
        } else {
            List<Map.Entry<String, PrimaryProperties>> entries = datasource.entrySet()
                    .stream()
                    .filter(entry -> entry.getValue().isPrimary())
                    .toList();
            if (entries.size() == 1) {
                primaryName = entries.get(0).getKey();
            }
        }
        Assert.notNull(primaryName, "缺少primary || 包含多个primary");
    }

    @Data
    @EqualsAndHashCode(callSuper = true)
    public static class PrimaryProperties extends DataSourceProperties {

        private boolean primary;
    }
}
