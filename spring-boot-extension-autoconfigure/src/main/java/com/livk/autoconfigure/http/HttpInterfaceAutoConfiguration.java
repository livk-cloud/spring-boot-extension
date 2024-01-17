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

package com.livk.autoconfigure.http;

import com.livk.auto.service.annotation.SpringAutoService;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.beans.factory.config.EmbeddedValueResolver;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;

/**
 * <p>
 * HttpAutoConfiguration
 * </p>
 *
 * @author livk
 */
@AutoConfiguration
@SpringAutoService
@Import(HttpServiceRegistrar.class)
@ConditionalOnClass(name = "com.livk.http.marker.HttpMarker")
public class HttpInterfaceAutoConfiguration {

	/**
	 * 添加SpringEL解析
	 * @param beanFactory bean factory
	 * @return HttpServiceProxyFactoryCustomizer
	 */
	@Bean
	public HttpServiceProxyFactoryCustomizer embeddedValueResolverCustomizer(ConfigurableBeanFactory beanFactory) {
		return builder -> builder.embeddedValueResolver(new EmbeddedValueResolver(beanFactory));
	}

}
