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

package com.livk.commons.util;

import com.google.auto.service.AutoService;
import com.livk.auto.service.annotation.SpringFactories;
import org.junit.jupiter.api.Test;
import org.springframework.core.ResolvableType;

import static org.junit.jupiter.api.Assertions.assertInstanceOf;

/**
 * @author livk
 */
class ProviderLoaderTest {

	@Test
	public void loadTest() {
		assertInstanceOf(Dog.class, ProviderLoader.SPRING_FACTORY.load(Animal.class).get(0));
		assertInstanceOf(Cat.class, ProviderLoader.JDK_SERVICE.load(Animal.class).get(0));

		ResolvableType type = ResolvableType.forClass(Animal.class);
		assertInstanceOf(Dog.class, ProviderLoader.SPRING_FACTORY.load(type).get(0));
		assertInstanceOf(Cat.class, ProviderLoader.JDK_SERVICE.load(type).get(0));
	}

	public interface Animal {

	}

	@SpringFactories
	public static class Dog implements Animal {

	}

	@AutoService(Animal.class)
	public static class Cat implements Animal {

	}
}
