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
import org.springframework.core.ResolvableType;

import java.util.Map;

import org.springframework.context.ApplicationEvent;
import org.springframework.core.convert.converter.Converter;

import java.util.concurrent.atomic.AtomicReference;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

/**
 * @author livk
 */
class SpringContextHolderTests {

	final ApplicationContextRunner contextRunner = new ApplicationContextRunner()
		.withPropertyValues("spring.data.redis.host=livk.com")
		.withBean(SpringContextHolder.class, SpringContextHolder::new);

	@Test
	void getBean() {
		contextRunner.run(ctx -> {
			BeanTest bean = new BeanTest();
			SpringContextHolder.registerBean(bean, "test");
			assertThat(SpringContextHolder.<BeanTest>getBean("test")).isSameAs(bean);
			assertThat(SpringContextHolder.getBean(BeanTest.class)).isSameAs(bean);
			assertThat(SpringContextHolder.getBean("test", BeanTest.class)).isSameAs(bean);
		});
	}

	@Test
	void getBeanProvider() {
		contextRunner.run(ctx -> {
			BeanTest bean = new BeanTest();
			SpringContextHolder.registerBean(bean, "test");
			ResolvableType resolvableType = ResolvableType.forClass(BeanTest.class);
			assertThat(SpringContextHolder.getBeanProvider(BeanTest.class).getIfAvailable()).isSameAs(bean);
			assertThat(SpringContextHolder.getBeanProvider(resolvableType).getIfAvailable()).isSameAs(bean);
		});
	}

	@Test
	void getBeansOfType() {
		contextRunner.run(ctx -> {
			BeanTest bean = new BeanTest();
			SpringContextHolder.registerBean(bean, "test");
			assertThat(SpringContextHolder.getBeansOfType(BeanTest.class)).isEqualTo(Map.of("test", bean));
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

	@Test
	void registerBean() {
		contextRunner.run(ctx -> {
			BeanTest bean = new BeanTest();
			SpringContextHolder.registerBean(bean, "test1");
			RootBeanDefinition beanDefinition = new RootBeanDefinition();
			beanDefinition.setBeanClass(bean.getClass());
			beanDefinition.setInstanceSupplier(() -> bean);
			SpringContextHolder.registerBean(beanDefinition, "test2");
			assertThat(SpringContextHolder.getBeansOfType(BeanTest.class))
				.isEqualTo(Map.of("test1", bean, "test2", bean));
		});
	}

	@Test
	void fetchReturnsApplicationContext() {
		contextRunner.run(ctx -> assertThat(SpringContextHolder.fetch()).isSameAs(ctx.getSourceApplicationContext()));
	}

	@Test
	void publishEventDispatchesToListeners() {
		AtomicReference<ApplicationEvent> seen = new AtomicReference<>();
		contextRunner.withBean(EventCapture.class, () -> new EventCapture(seen)).run(ctx -> {
			DemoEvent event = new DemoEvent(this);
			SpringContextHolder.publishEvent(event);
			assertThat(seen.get()).isSameAs(event);
		});
	}

	@Test
	void registerBeanGeneratesNameWhenBlank() {
		contextRunner.run(ctx -> {
			BeanTest bean = new BeanTest();
			RootBeanDefinition beanDefinition = new RootBeanDefinition();
			beanDefinition.setBeanClass(BeanTest.class);
			beanDefinition.setInstanceSupplier(() -> bean);
			SpringContextHolder.registerBean(beanDefinition, "");
			assertThat(SpringContextHolder.getBeansOfType(BeanTest.class)).hasSize(1).containsValue(bean);
		});
	}

	@Test
	void binderAppliesCustomConverters() {
		contextRunner.withPropertyValues("demo.flag=yes").run(ctx -> {
			Converter<String, Boolean> yesConverter = new Converter<>() {
				@Override
				public Boolean convert(String source) {
					return "yes".equalsIgnoreCase(source);
				}
			};
			Binder binder = SpringContextHolder.binder(yesConverter);
			assertThat(binder.bind("demo.flag", Boolean.class).get()).isTrue();
		});
	}

	@Test
	void destroyClearsHolder() {
		contextRunner.run(ctx -> {
			assertThat(SpringContextHolder.fetch()).isNotNull();
			ctx.getBean(SpringContextHolder.class).destroy();
			assertThatThrownBy(SpringContextHolder::fetch).isInstanceOf(IllegalStateException.class)
				.hasMessageContaining("not initialized");
		});
	}

	@Data
	static class BeanTest {

		private final Long id = 1L;

	}

	static final class DemoEvent extends ApplicationEvent {

		DemoEvent(Object source) {
			super(source);
		}

	}

	static final class EventCapture implements org.springframework.context.ApplicationListener<DemoEvent> {

		private final AtomicReference<ApplicationEvent> seen;

		EventCapture(AtomicReference<ApplicationEvent> seen) {
			this.seen = seen;
		}

		@Override
		public void onApplicationEvent(DemoEvent event) {
			this.seen.set(event);
		}

	}

}
