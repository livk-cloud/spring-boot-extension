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

package com.livk.context.http.factory;

import com.livk.context.http.HttpConfig;
import com.livk.context.http.HttpService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author livk
 */
@SpringJUnitConfig(HttpConfig.class)
class HttpFactoryBeanTests {

	@Autowired
	BeanFactory beanFactory;

	@Test
	void test() {
		HttpFactoryBean factoryBean = new HttpFactoryBean();
		factoryBean.setType(HttpService.class);
		factoryBean.setAdapterFactory(new RestClientAdapterFactory());
		factoryBean.setBeanFactory(beanFactory);
		factoryBean.afterPropertiesSet();
		assertThat(factoryBean.getObject()).isNotNull();
	}

}
