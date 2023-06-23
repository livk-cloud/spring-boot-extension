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
import com.livk.commons.bean.GenericWrapper;
import com.livk.commons.bean.Wrapper;

/**
 * The type Mapper factory.
 *
 * @author livk
 */
public enum MapperFactory {

	/**
	 * Json jackson type.
	 */
	JSON,

	/**
	 * Yaml jackson type.
	 */
	YAML,

	/**
	 * Xml jackson type.
	 */
	XML;

	/**
	 * 根据{@link MapperFactory}构建 {@link MapperBuilder}的子类
	 *
	 * @param <M>     {@link ObjectMapper} 子类
	 * @param <B>     {@link MapperBuilder} 子类
	 * @param factory the format
	 * @return MapperBuilder
	 */
	@SuppressWarnings("unchecked")
	public static <M extends ObjectMapper, B extends MapperBuilder<M, B>> B builder(MapperFactory factory) {
		Wrapper wrapper = switch (factory) {
			case JSON -> new JsonBuilderWrapper();
			case YAML -> new YamlBuilderWrapper();
			case XML -> new XmlBuilderWrapper();
		};
		return (B) wrapper.unwrap(MapperBuilder.class);
	}

	/**
	 * 通用{@link MapperBuilder} 包装器,避免静态加载触发 {@link ClassNotFoundException}
	 *
	 * @param <M> {@link ObjectMapper} 子类
	 * @param <B> {@link MapperBuilder} 子类
	 * @see com.livk.commons.bean.GenericWrapper
	 * @see JsonMapper
	 * @see YAMLMapper
	 * @see XmlMapper
	 */
	private static abstract class BuilderWrapper<M extends ObjectMapper, B extends MapperBuilder<M, B>> implements GenericWrapper<MapperBuilder<M, B>> {

	}

	private static class JsonBuilderWrapper extends BuilderWrapper<JsonMapper, JsonMapper.Builder> implements Wrapper {

		@Override
		public MapperBuilder<JsonMapper, JsonMapper.Builder> unwrap() {
			return JsonMapper.builder();
		}
	}

	private static class YamlBuilderWrapper extends BuilderWrapper<YAMLMapper, YAMLMapper.Builder> implements Wrapper {

		@Override
		public MapperBuilder<YAMLMapper, YAMLMapper.Builder> unwrap() {
			return YAMLMapper.builder();
		}
	}

	private static class XmlBuilderWrapper extends BuilderWrapper<XmlMapper, XmlMapper.Builder> implements Wrapper {

		@Override
		public MapperBuilder<XmlMapper, XmlMapper.Builder> unwrap() {
			return XmlMapper.builder();
		}
	}
}
