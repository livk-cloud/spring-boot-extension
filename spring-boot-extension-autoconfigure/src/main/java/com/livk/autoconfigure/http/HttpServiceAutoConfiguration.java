/*
 * Copyright 2021-present the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.livk.autoconfigure.http;

import com.livk.auto.service.annotation.SpringAutoService;
import com.livk.context.http.HttpServiceRegistrar;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.AutoConfigurationPackages;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.Import;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.util.StringUtils;

import java.util.List;

/**
 * <p>
 * HttpAutoConfiguration
 * </p>
 *
 * @author livk
 */
@AutoConfiguration
@SpringAutoService
@Import(HttpServiceAutoConfiguration.AutoConfiguredHttpServiceRegistrar.class)
@ConditionalOnClass(name = "com.livk.http.marker.HttpMarker")
public class HttpServiceAutoConfiguration {

	@RequiredArgsConstructor
	static class AutoConfiguredHttpServiceRegistrar extends HttpServiceRegistrar {

		private final BeanFactory beanFactory;

		@Override
		protected String[] getBasePackages(AnnotationMetadata metadata) {
			List<String> packages = AutoConfigurationPackages.get(this.beanFactory);
			return StringUtils.toStringArray(packages);
		}

	}

}
