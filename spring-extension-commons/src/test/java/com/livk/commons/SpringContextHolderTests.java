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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

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
			assertEquals(bean, SpringContextHolder.getBean("test"));
			assertEquals(bean, SpringContextHolder.getBean(BeanTest.class));
			assertEquals(bean, SpringContextHolder.getBean("test", BeanTest.class));
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
			assertEquals(bean, SpringContextHolder.getBeanProvider(BeanTest.class).getIfAvailable());
			assertEquals(bean, SpringContextHolder.getBeanProvider(resolvableType).getIfAvailable());
			if (SpringContextHolder.fetch() instanceof GenericApplicationContext context) {
				context.removeBeanDefinition("test");
			}
		});
	}

	@Test
	void getBeansOfType() {
		contextRunner.run(ctx -> {
			SpringContextHolder.registerBean(bean, "test");
			assertEquals(Map.of("test", bean), SpringContextHolder.getBeansOfType(BeanTest.class));
			if (SpringContextHolder.fetch() instanceof GenericApplicationContext context) {
				context.removeBeanDefinition("test");
			}
		});
	}

	@Test
	void getProperty() {
		contextRunner.run(ctx -> {
			assertEquals("livk.com", SpringContextHolder.getProperty("spring.data.redis.host"));
			assertEquals("livk.com", SpringContextHolder.getProperty("spring.data.redis.host", String.class));
			assertEquals("livk.com",
					SpringContextHolder.getProperty("spring.data.redis.host", String.class, "livk.cn"));
			assertEquals("livk.cn",
					SpringContextHolder.getProperty("spring.data.redisson.host", String.class, "livk.cn"));
		});
	}

	@Test
	void resolvePlaceholders() {
		contextRunner
			.run(ctx -> assertEquals("livk.com", SpringContextHolder.resolvePlaceholders("${spring.data.redis.host}")));
	}

	@Test
	void binder() {
		contextRunner.run(ctx -> {
			Binder binder = SpringContextHolder.binder();
			BindResult<String> result = binder.bind("spring.data.redis.host", String.class);
			assertTrue(result.isBound());
			assertEquals("livk.com", result.get());
		});
	}

	@Test
	@SuppressWarnings("unchecked")
	void registerBean() {
		contextRunner.run(ctx -> {
			SpringContextHolder.registerBean(bean, "test1");
			RootBeanDefinition beanDefinition = new RootBeanDefinition((Class<BeanTest>) bean.getClass(), () -> bean);
			SpringContextHolder.registerBean(beanDefinition, "test2");
			assertEquals(Map.of("test1", bean, "test2", bean), SpringContextHolder.getBeansOfType(BeanTest.class));
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
