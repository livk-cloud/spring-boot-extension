package com.livk.autoconfigure.dynamic.datasource;

import com.livk.autoconfigure.dynamic.annotation.EnableDynamicDatasource;
import com.livk.commons.spring.AbstractImportSelector;

/**
 * <p>
 * DynamicDatasourceImportSelector
 * </p>
 *
 * @author livk
 * @date 2022/12/2
 */
public class DynamicDatasourceImportSelector extends AbstractImportSelector<EnableDynamicDatasource> {

    @Override
    protected boolean isEnabled() {
        return getEnvironment().getProperty(DynamicDatasourceProperties.PREFIX + "enabled", Boolean.class, Boolean.TRUE);
    }
}
