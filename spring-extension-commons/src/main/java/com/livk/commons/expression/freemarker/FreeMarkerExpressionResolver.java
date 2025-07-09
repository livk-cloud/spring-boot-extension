/*
 * Copyright 2021-2024 spring-boot-extension the original author or authors.
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
 */

package com.livk.commons.expression.freemarker;

import com.livk.commons.expression.CacheExpressionResolver;
import com.livk.commons.expression.Context;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;

import java.io.IOException;
import java.io.StringWriter;
import java.util.Map;

/**
 * 使用FreeMarker实现的表达式解析器
 *
 * @author livk
 */
@RequiredArgsConstructor
public class FreeMarkerExpressionResolver extends CacheExpressionResolver<Map<String, Object>, Template> {

	private static final Configuration DEFAULT_CONFIG = new Configuration(
			Configuration.DEFAULT_INCOMPATIBLE_IMPROVEMENTS);

	private static final String TEMPLATE_NAME = "template";

	private final Configuration configuration;

	/**
	 * Instantiates a new Free marker expression resolver.
	 */
	public FreeMarkerExpressionResolver() {
		this(DEFAULT_CONFIG);
	}

	@SneakyThrows
	@Override
	protected Template compile(String value) {
		return new Template(TEMPLATE_NAME, value, configuration);
	}

	@Override
	protected Map<String, Object> transform(Context context) {
		return context.asMap();
	}

	@Override
	protected <T> T calculate(Template template, Map<String, Object> context, Class<T> returnType) {
		if (returnType.isAssignableFrom(String.class)) {
			StringWriter result = new StringWriter(1024);
			try {
				template.process(context, result);
				return returnType.cast(result.toString());
			}
			catch (IOException | TemplateException e) {
				throw new IllegalArgumentException(e);
			}
		}
		throw new UnsupportedOperationException("Classes other than String and its parent class are not supported");
	}

}
