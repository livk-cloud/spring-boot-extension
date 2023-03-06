package com.livk.autoconfigure.oss.support;

import com.livk.autoconfigure.oss.client.OSSClientFactory;
import com.livk.autoconfigure.oss.client.OSSClientFactoryLoader;
import org.springframework.core.io.support.SpringFactoriesLoader;

/**
 * The type Oss client factory pattern resolver.
 *
 * @author livk
 */
class OSSClientFactoryPatternResolver implements OSSClientFactoryLoader {

    @SuppressWarnings({"rawtypes", "unchecked"})
    @Override
    public <T> OSSClientFactory<T> loader(String prefix) {
        for (OSSClientFactory factory : SpringFactoriesLoader.loadFactories(OSSClientFactory.class, this.getClass().getClassLoader())) {
            if (factory.prefix().equals(prefix)) {
                return (OSSClientFactory<T>) factory;
            }
        }
        throw new RuntimeException("oss factory匹配失败");
    }
}
