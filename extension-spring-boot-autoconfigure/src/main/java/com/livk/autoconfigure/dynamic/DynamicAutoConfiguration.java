package com.livk.autoconfigure.dynamic;

import com.livk.auto.service.annotation.SpringAutoService;
import com.livk.autoconfigure.dynamic.annotation.EnableDynamicDatasource;
import com.livk.autoconfigure.dynamic.aspect.DataSourceAspect;
import com.livk.autoconfigure.dynamic.datasource.DynamicDatasource;
import com.livk.autoconfigure.dynamic.datasource.DynamicDatasourceProperties;
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
@SpringAutoService(auto = EnableDynamicDatasource.class)
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
     * Data source aspect data source aspect.
     *
     * @return the data source aspect
     */
    @Bean
    public DataSourceAspect dataSourceAspect() {
        return new DataSourceAspect();
    }
}
