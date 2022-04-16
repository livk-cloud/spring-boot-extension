package com.livk.spi;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.core.GenericTypeResolver;

import java.util.Map;
import java.util.ServiceLoader;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * <p>
 * AbstractServiceLoad
 * </p>
 *
 * @author livk
 * @date 2022/4/16
 */
@Slf4j
public abstract class AbstractServiceLoad<T> implements InitializingBean {

    protected Map<String, T> servicesMap;


    @SuppressWarnings("unchecked")
    private Class<T> getServiceClass(){
        return  (Class<T>) GenericTypeResolver.resolveTypeArgument(this.getClass(), AbstractServiceLoad.class);
    }

    @Override
    public void afterPropertiesSet() {
        servicesMap = ServiceLoader.load(getServiceClass()).stream()
                .map(ServiceLoader.Provider::get)
                .collect(Collectors.toMap(this::getKey,
                        Function.identity(),
                        (t1, t2) -> t2,
                        ConcurrentHashMap::new));
        log.info("data:{}", servicesMap);
    }

    protected abstract String getKey(T t);
}
