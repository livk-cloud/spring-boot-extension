package com.livk.datasource;

import com.livk.aspect.DataSourceAspect;
import org.springframework.boot.autoconfigure.AutoConfiguration;
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
 * @date 2022/3/23
 */
@AutoConfiguration
@EnableConfigurationProperties(DynamicDatasourceProperties.class)
public class DynamicAutoConfiguration {

    @Bean
    public DynamicDatasource dynamicDatasource(DynamicDatasourceProperties datasourceProperties) {
        Map<Object, Object> datasourceMap = datasourceProperties.getDynamic()
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
