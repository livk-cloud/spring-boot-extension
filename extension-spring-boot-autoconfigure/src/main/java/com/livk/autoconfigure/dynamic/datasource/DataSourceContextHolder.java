package com.livk.autoconfigure.dynamic.datasource;

import org.springframework.util.StringUtils;

/**
 * <p>
 * DataSourceContextHolder
 * </p>
 *
 * @author livk
 */
public class DataSourceContextHolder {

    private static final ThreadLocal<String> datasourceContext = new ThreadLocal<>();
    private static final ThreadLocal<String> InheritableDatasourceContext = new InheritableThreadLocal<>();

    /**
     * Switch data source.
     *
     * @param datasource the datasource
     */
    public static void switchDataSource(String datasource) {
        datasourceContext.set(datasource);
        InheritableDatasourceContext.set(datasource);
    }

    /**
     * Get data source.
     *
     * @return the data source
     */
    public static String getDataSource() {
        String datasource = datasourceContext.get();
        if (!StringUtils.hasText(datasource)) {
            datasource = InheritableDatasourceContext.get();
        }
        return datasource;
    }

    /**
     * Clear.
     */
    public static void clear() {
        datasourceContext.remove();
        InheritableDatasourceContext.remove();
    }

}
