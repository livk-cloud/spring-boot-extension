package com.livk.autoconfigure.dynamic;

import com.livk.autoconfigure.dynamic.annotation.EnableDynamicDatasource;
import com.livk.autoconfigure.dynamic.datasource.DynamicDatasourceProperties;
import com.livk.commons.spring.AbstractImportSelector;

/**
 * <p>
 * DynamicDatasourceImportSelector
 * </p>
 *
 * @author livk
 */
public class DynamicDatasourceImportSelector extends AbstractImportSelector<EnableDynamicDatasource> {

    @Override
    protected boolean isEnabled() {
        return environment.getProperty(DynamicDatasourceProperties.PREFIX + "enabled", Boolean.class, Boolean.TRUE);
    }
}
