package com.livk.autoconfigure.dynamic;

import com.livk.autoconfigure.dynamic.aspect.DataSourceAspect;
import com.livk.autoconfigure.dynamic.datasource.DynamicDatasource;
import com.livk.autoconfigure.dynamic.datasource.DynamicDatasourceProperties;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.AutoConfigureOrder;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.core.Ordered;

import java.util.Map;
import java.util.stream.Collectors;

/**
 * <p>
 * DynamicAutoConfiguration
 * </p>
 *
 * @author livk
 * @date 2022/3/23
 */
//@AutoConfigureOrder(Ordered.HIGHEST_PRECEDENCE)
@AutoConfiguration(before = DataSourceAutoConfiguration.class)
@EnableConfigurationProperties(DynamicDatasourceProperties.class)
public class DynamicAutoConfiguration {

    @Bean
    public DynamicDatasource dynamicDatasource(DynamicDatasourceProperties datasourceProperties) {
        Map<Object, Object> datasourceMap = datasourceProperties.getDatasource()
                .entrySet()
                .stream()
                .collect(Collectors.toMap(Map.Entry::getKey, entry -> entry.getValue().initializeDataSourceBuilder().build()));
        DynamicDatasource dynamicDatasource = new DynamicDatasource();
        dynamicDatasource.setTargetDataSources(datasourceMap);
        dynamicDatasource.setDefaultTargetDataSource(datasourceMap.get(datasourceProperties.getPrimaryName()));
        return dynamicDatasource;
    }

    @Bean
    public DataSourceAspect dataSourceAspect() {
        return new DataSourceAspect();
    }
}
