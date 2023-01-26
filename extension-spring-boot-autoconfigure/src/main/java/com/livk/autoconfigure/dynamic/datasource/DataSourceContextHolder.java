package com.livk.autoconfigure.dynamic.datasource;

import org.springframework.core.NamedInheritableThreadLocal;
import org.springframework.core.NamedThreadLocal;
import org.springframework.util.StringUtils;
import org.springframework.web.context.request.RequestAttributes;

/**
 * <p>
 * DataSourceContextHolder
 * </p>
 *
 * @author livk
 */
public class DataSourceContextHolder {

    private static final ThreadLocal<String> datasourceHolder =
            new NamedThreadLocal<>("datasource context");

    private static final ThreadLocal<String> inheritableDatasourceHolder =
            new NamedInheritableThreadLocal<>("inheritable datasource context");

    /**
     * Switch data source.
     *
     * @param datasource the datasource
     */
    public static void switchDataSource(String datasource) {
        datasourceHolder.set(datasource);
        inheritableDatasourceHolder.set(datasource);
    }

    /**
     * Get data source.
     *
     * @return the data source
     */
    public static String getDataSource() {
        String datasource = datasourceHolder.get();
        return StringUtils.hasText(datasource) ? datasource : inheritableDatasourceHolder.get();
    }

    /**
     * Clear.
     */
    public static void clear() {
        datasourceHolder.remove();
        inheritableDatasourceHolder.remove();
    }

}
