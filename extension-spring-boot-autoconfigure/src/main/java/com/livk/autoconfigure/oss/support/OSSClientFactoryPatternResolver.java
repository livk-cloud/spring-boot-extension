/*
 * Copyright 2021 spring-boot-extension the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package com.livk.autoconfigure.oss.support;

import com.livk.autoconfigure.oss.client.OSSClientFactory;
import com.livk.autoconfigure.oss.client.OSSClientFactoryLoader;
import com.livk.autoconfigure.oss.exception.OSSClientFactoryNotFoundException;
import com.livk.autoconfigure.oss.support.aliyun.AliyunClientFactory;
import com.livk.autoconfigure.oss.support.minio.MinioClientFactory;
import com.livk.commons.bean.util.ClassUtils;
import com.livk.commons.spi.Loader;
import com.livk.commons.spi.support.UniversalManager;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.ResolvableType;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * The type Oss client factory pattern resolver.
 *
 * @author livk
 */
@Slf4j
class OSSClientFactoryPatternResolver implements OSSClientFactoryLoader {

    private final Set<String> nameSet = new HashSet<>();

    private final List<OSSClientFactory<?>> factoryList = new ArrayList<>();

    /**
     * Instantiates a new Oss client factory pattern resolver.
     */
    public OSSClientFactoryPatternResolver() {
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        if (ClassUtils.isPresent("io.minio.MinioClient", classLoader)) {
            addChecked(new MinioClientFactory());
        }
        if (ClassUtils.isPresent("com.aliyun.oss.OSS", classLoader)) {
            addChecked(new AliyunClientFactory());
        }
        for (OSSClientFactory<?> factory : springFactoryLoader()) {
            addChecked(factory);
        }
    }

    private void addChecked(OSSClientFactory<?> factory) {
        if (nameSet.contains(factory.name())) {
            log.error("current factory contains oss:{}", factory.name());
        } else {
            factoryList.add(factory);
            nameSet.add(factory.name());
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T> OSSClientFactory<T> loader(String prefix) {
        for (OSSClientFactory<?> factory : factoryList) {
            if (factory.name().equals(prefix)) {
                return (OSSClientFactory<T>) factory;
            }
        }
        List<String> prefixList = factoryList.stream().map(OSSClientFactory::name).toList();
        throw new OSSClientFactoryNotFoundException(prefix + " oss factory匹配失败,当前可用oss factory :" + prefixList);
    }

    private List<OSSClientFactory<?>> springFactoryLoader() {
        ResolvableType resolvableType = ResolvableType.forClass(OSSClientFactory.class);
        return Loader.load(resolvableType, UniversalManager.SPRING_FACTORY);
    }

}
