package com.livk.datasource;

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

    public static void switchDataSource(String datasource) {
        datasourceContext.set(datasource);
    }

    public static String getDataSource() {
        return datasourceContext.get();
    }

    public static void clear() {
        datasourceContext.remove();
    }
}
