package com.livk.autoconfigure.dynamic;

import com.livk.auto.service.annotation.SpringAutoService;
import com.livk.autoconfigure.dynamic.annotation.EnableDynamicDatasource;
import com.livk.autoconfigure.dynamic.datasource.DynamicDatasource;
import com.livk.autoconfigure.dynamic.datasource.DynamicDatasourceProperties;
import com.livk.autoconfigure.dynamic.intercept.DataSourceInterceptor;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;

import java.util.Map;
import java.util.stream.Collectors;

/**
 * <p>
 * DynamicAutoConfiguration
 * </p>
 *
 * @author livk
 */
@SpringAutoService(EnableDynamicDatasource.class)
@AutoConfiguration(before = DataSourceAutoConfiguration.class)
@EnableConfigurationProperties(DynamicDatasourceProperties.class)
public class DynamicAutoConfiguration {

    /**
     * Dynamic datasource dynamic datasource.
     *
     * @param datasourceProperties the datasource properties
     * @return the dynamic datasource
     */
    @Bean
    public DynamicDatasource dynamicDatasource(DynamicDatasourceProperties datasourceProperties) {
        Map<Object, Object> datasourceMap = datasourceProperties.getDatasource()
                .entrySet()
                .stream()
                .collect(Collectors.toMap(Map.Entry::getKey, entry -> entry.getValue().initializeDataSourceBuilder().build()));
        DynamicDatasource dynamicDatasource = new DynamicDatasource();
        dynamicDatasource.setTargetDataSources(datasourceMap);
        dynamicDatasource.setDefaultTargetDataSource(datasourceMap.get(datasourceProperties.getPrimary()));
        return dynamicDatasource;
    }

    /**
     * Data source interceptor data source interceptor.
     *
     * @return the data source interceptor
     */
    @Bean
    public DataSourceInterceptor dataSourceInterceptor() {
        return new DataSourceInterceptor();
    }
}
