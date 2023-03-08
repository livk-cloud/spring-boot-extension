package com.livk.spi;

import com.livk.commons.spi.DefaultLoaderType;
import com.livk.commons.spi.Loader;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.core.GenericTypeResolver;

import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * <p>
 * AbstractServiceLoad
 * </p>
 *
 * @author livk
 */
@Slf4j
public abstract class AbstractServiceLoad<T> implements InitializingBean {

    protected Map<String, T> servicesMap;

    @SuppressWarnings("unchecked")
    private Class<T> getServiceClass() {
        return (Class<T>) GenericTypeResolver.resolveTypeArgument(this.getClass(), AbstractServiceLoad.class);
    }

    @Override
    public void afterPropertiesSet() {
        servicesMap = Loader.load(getServiceClass(), DefaultLoaderType.JDK_SERVICE).stream()
                .collect(Collectors.toMap(this::getKey, Function.identity()));
        log.info("data:{}", servicesMap);
    }

    protected abstract String getKey(T t);

}
