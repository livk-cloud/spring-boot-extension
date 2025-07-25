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

package com.livk.commons;

import lombok.Data;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.support.RootBeanDefinition;
import org.springframework.boot.context.properties.bind.BindResult;
import org.springframework.boot.context.properties.bind.Binder;
import org.springframework.boot.test.context.runner.ApplicationContextRunner;
import org.springframework.context.support.GenericApplicationContext;
import org.springframework.core.ResolvableType;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author livk
 */
class SpringContextHolderTests {

	final ApplicationContextRunner contextRunner = new ApplicationContextRunner()
		.withPropertyValues("spring.data.redis.host=livk.com")
		.withBean(SpringContextHolder.class, SpringContextHolder::new);

	final BeanTest bean = new BeanTest();

	@Test
	void getBean() {
		contextRunner.run(ctx -> {
			SpringContextHolder.registerBean(bean, "test");
			assertThat(SpringContextHolder.<BeanTest>getBean("test")).isSameAs(bean);
			assertThat(SpringContextHolder.getBean(BeanTest.class)).isSameAs(bean);
			assertThat(SpringContextHolder.getBean("test", BeanTest.class)).isSameAs(bean);
			if (SpringContextHolder.fetch() instanceof GenericApplicationContext context) {
				context.removeBeanDefinition("test");
			}
		});
	}

	@Test
	void getBeanProvider() {
		contextRunner.run(ctx -> {
			SpringContextHolder.registerBean(bean, "test");
			ResolvableType resolvableType = ResolvableType.forClass(BeanTest.class);
			assertThat(SpringContextHolder.getBeanProvider(BeanTest.class).getIfAvailable()).isSameAs(bean);
			assertThat(SpringContextHolder.getBeanProvider(resolvableType).getIfAvailable()).isSameAs(bean);
			if (SpringContextHolder.fetch() instanceof GenericApplicationContext context) {
				context.removeBeanDefinition("test");
			}
		});
	}

	@Test
	void getBeansOfType() {
		contextRunner.run(ctx -> {
			SpringContextHolder.registerBean(bean, "test");
			assertThat(SpringContextHolder.getBeansOfType(BeanTest.class)).isEqualTo(Map.of("test", bean));
			if (SpringContextHolder.fetch() instanceof GenericApplicationContext context) {
				context.removeBeanDefinition("test");
			}
		});
	}

	@Test
	void getProperty() {
		contextRunner.run(ctx -> {
			assertThat(SpringContextHolder.getProperty("spring.data.redis.host")).isEqualTo("livk.com");
			assertThat(SpringContextHolder.getProperty("spring.data.redis.host", String.class)).isEqualTo("livk.com");
			assertThat(SpringContextHolder.getProperty("spring.data.redis.host", String.class, "livk.cn"))
				.isEqualTo("livk.com");
			assertThat(SpringContextHolder.getProperty("spring.data.redisson.host", String.class, "livk.cn"))
				.isEqualTo("livk.cn");
		});
	}

	@Test
	void resolvePlaceholders() {
		contextRunner.run(ctx -> assertThat(SpringContextHolder.resolvePlaceholders("${spring.data.redis.host}"))
			.isEqualTo("livk.com"));
	}

	@Test
	void binder() {
		contextRunner.run(ctx -> {
			Binder binder = SpringContextHolder.binder();
			BindResult<String> result = binder.bind("spring.data.redis.host", String.class);
			assertThat(result.isBound()).isTrue();
			assertThat(result.get()).isEqualTo("livk.com");
		});
	}

	@SuppressWarnings("unchecked")
	@Test
	void registerBean() {
		contextRunner.run(ctx -> {
			SpringContextHolder.registerBean(bean, "test1");
			RootBeanDefinition beanDefinition = new RootBeanDefinition((Class<BeanTest>) bean.getClass(), () -> bean);
			SpringContextHolder.registerBean(beanDefinition, "test2");
			assertThat(SpringContextHolder.getBeansOfType(BeanTest.class))
				.isEqualTo(Map.of("test1", bean, "test2", bean));
			if (SpringContextHolder.fetch() instanceof GenericApplicationContext context) {
				context.removeBeanDefinition("test1");
				context.removeBeanDefinition("test2");
			}
		});
	}

	@Data
	static class BeanTest {

		private final Long id = 1L;

	}

}
