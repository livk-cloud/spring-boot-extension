package com.livk.datasource;

import com.google.common.collect.Maps;
import com.livk.constant.DataSourceConstant;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;
import java.util.Map;

/**
 * <p>
 * DynamicConfig
 * </p>
 *
 * @author livk
 * @date 2022/3/23
 */
@Configuration
public class DynamicConfig {

    @Bean
    public DynamicDatasource dynamicDatasource(Map<String, DataSource> dataSourceMap) {
        Map<Object, Object> map = Maps.newHashMap(dataSourceMap);
        DynamicDatasource dynamicDatasource = new DynamicDatasource();
        dynamicDatasource.setTargetDataSources(map);
        dynamicDatasource.setDefaultTargetDataSource(dataSourceMap.get(DataSourceConstant.DEFAULT_DATASOURCE_NAME));
        return dynamicDatasource;
    }

}
