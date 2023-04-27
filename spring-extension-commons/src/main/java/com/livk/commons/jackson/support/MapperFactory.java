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

package com.livk.commons.jackson.support;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.cfg.MapperBuilder;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLMapper;
import com.livk.commons.bean.Wrapper;

/**
 * The type Mapper factory.
 *
 * @author livk
 */
public class MapperFactory {

    /**
     * Builder b.
     *
     * @param <M>    the type parameter
     * @param <B>    the type parameter
     * @param format the format
     * @return the b
     */
    @SuppressWarnings("unchecked")
    public static <M extends ObjectMapper, B extends MapperBuilder<M, B>> B builder(JacksonFormat format) {
        Wrapper wrapper = switch (format) {
            case JSON -> new JsonBuilderWrapper();
            case YAML -> new YamlBuilderWrapper();
            case XML -> new XmlBuilderWrapper();
        };
        return (B) wrapper.unwrap(MapperBuilder.class);
    }


    private static abstract class BuilderWrapper implements Wrapper {

        @Override
        public boolean isWrapperFor(Class<?> type) {
            return MapperBuilder.class.isAssignableFrom(type);
        }
    }

    private static class JsonBuilderWrapper extends BuilderWrapper implements Wrapper {

        @Override
        public <T> T unwrap(Class<T> type) {
            return type.cast(JsonMapper.builder());
        }
    }

    private static class YamlBuilderWrapper extends BuilderWrapper implements Wrapper {

        @Override
        public <T> T unwrap(Class<T> type) {
            return type.cast(YAMLMapper.builder());
        }
    }

    private static class XmlBuilderWrapper extends BuilderWrapper implements Wrapper {

        @Override
        public <T> T unwrap(Class<T> type) {
            return type.cast(XmlMapper.builder());
        }
    }
}
