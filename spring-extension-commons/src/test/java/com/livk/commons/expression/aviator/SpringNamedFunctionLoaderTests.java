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

package com.livk.commons.expression.aviator;

import com.googlecode.aviator.AviatorEvaluator;
import com.googlecode.aviator.runtime.function.AbstractFunction;
import com.googlecode.aviator.runtime.type.AviatorFunction;
import com.googlecode.aviator.runtime.type.AviatorObject;
import com.googlecode.aviator.runtime.type.AviatorString;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.runner.ApplicationContextRunner;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

/**
 * @author livk
 */
class SpringNamedFunctionLoaderTests {

	@Test
	void registersLoaderAndResolvesFunctionByName() {
		AviatorFunction function = mock(AviatorFunction.class);
		given(function.getName()).willReturn("springEcho");

		new ApplicationContextRunner().withBean(AviatorFunction.class, () -> function)
			.withBean(SpringNamedFunctionLoader.class)
			.run(ctx -> {
				SpringNamedFunctionLoader loader = ctx.getBean(SpringNamedFunctionLoader.class);
				assertThat(loader.onFunctionNotFound("springEcho")).isSameAs(function);
				assertThat(loader.onFunctionNotFound("missing")).isNull();
			});
	}

	@Test
	void putIfAbsentKeepsFirstFunctionForDuplicateName() {
		AviatorFunction first = mock(AviatorFunction.class);
		AviatorFunction second = mock(AviatorFunction.class);
		given(first.getName()).willReturn("dup");
		given(second.getName()).willReturn("dup");

		new ApplicationContextRunner().withBean("first", AviatorFunction.class, () -> first)
			.withBean("second", AviatorFunction.class, () -> second)
			.withBean(SpringNamedFunctionLoader.class)
			.run(ctx -> assertThat(ctx.getBean(SpringNamedFunctionLoader.class).onFunctionNotFound("dup"))
				.isSameAs(first));
	}

	@Test
	void destroyRemovesFunctionLoader() {
		new ApplicationContextRunner().withBean(AviatorFunction.class, TempFn::new)
			.withBean(SpringNamedFunctionLoader.class)
			.run(ctx -> {
				SpringNamedFunctionLoader loader = ctx.getBean(SpringNamedFunctionLoader.class);
				assertThat(AviatorEvaluator.execute("tempFn()")).isEqualTo("ok");
				loader.destroy();
				assertThatThrownBy(() -> AviatorEvaluator.execute("tempFn()")).hasMessageContaining("tempFn");
			});
	}

	static final class TempFn extends AbstractFunction {

		@Override
		public String getName() {
			return "tempFn";
		}

		@Override
		public AviatorObject call(Map<String, Object> env) {
			return new AviatorString("ok");
		}

	}

}
