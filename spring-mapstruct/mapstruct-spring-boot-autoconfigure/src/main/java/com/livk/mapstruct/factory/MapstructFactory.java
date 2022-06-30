package com.livk.mapstruct.factory;

import com.livk.mapstruct.converter.MapstructRegistry;
import com.livk.mapstruct.support.GenericMapstructService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.ListableBeanFactory;

/**
 * <p>
 * MapstructFactory
 * </p>
 *
 * @author livk
 * @date 2022/5/11
 */
@RequiredArgsConstructor
public class MapstructFactory implements InitializingBean {

    private final MapstructRegistry registry;

    private final ListableBeanFactory beanFactory;

    @Override
    public void afterPropertiesSet() {
        GenericMapstructService.addBeans(registry, beanFactory);
    }

}
