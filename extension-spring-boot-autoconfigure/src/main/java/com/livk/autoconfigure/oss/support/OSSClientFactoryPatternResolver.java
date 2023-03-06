package com.livk.autoconfigure.oss.support;

import com.livk.autoconfigure.oss.client.OSSClientFactory;
import com.livk.autoconfigure.oss.client.OSSClientFactoryLoader;
import com.livk.autoconfigure.oss.exception.OSSClientFactoryNotFoundException;
import org.springframework.core.io.support.SpringFactoriesLoader;

import java.util.List;

/**
 * The type Oss client factory pattern resolver.
 *
 * @author livk
 */
class OSSClientFactoryPatternResolver implements OSSClientFactoryLoader {

    @SuppressWarnings({"rawtypes", "unchecked"})
    @Override
    public <T> OSSClientFactory<T> loader(String prefix) {
        List<OSSClientFactory> factories = SpringFactoriesLoader.loadFactories(OSSClientFactory.class, this.getClass().getClassLoader());
        for (OSSClientFactory factory : factories) {
            if (factory.prefix().equals(prefix)) {
                return (OSSClientFactory<T>) factory;
            }
        }
        List<String> prefixList = factories.stream().map(OSSClientFactory::prefix).toList();
        throw new OSSClientFactoryNotFoundException(prefix + " oss factory匹配失败,当前可用oss factory :" + prefixList);
    }
}
