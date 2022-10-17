package com.livk.datasource;

import org.springframework.util.StringUtils;

/**
 * <p>
 * DataSourceContextHolder
 * </p>
 *
 * @author livk
 * @date 2022/3/23
 */
public class DataSourceContextHolder {

    private static final ThreadLocal<String> datasourceContext = new ThreadLocal<>();
    private static final ThreadLocal<String> InheritableDatasourceContext = new InheritableThreadLocal<>();

    public static void switchDataSource(String datasource) {
        datasourceContext.set(datasource);
        InheritableDatasourceContext.set(datasource);
    }

    public static String getDataSource() {
        String datasource = datasourceContext.get();
        if (!StringUtils.hasText(datasource)) {
            datasource = InheritableDatasourceContext.get();
        }
        return datasource;
    }

    public static void clear() {
        datasourceContext.remove();
        InheritableDatasourceContext.remove();
    }

}
