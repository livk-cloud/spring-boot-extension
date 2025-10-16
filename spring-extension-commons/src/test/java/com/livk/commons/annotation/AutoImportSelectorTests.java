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

package com.livk.commons.annotation;

import com.livk.auto.service.annotation.SpringAutoService;
import org.junit.jupiter.api.Test;
import org.springframework.core.type.AnnotationMetadata;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author livk
 */
class AutoImportSelectorTests {

	final AutoImportSelector importSelector = new AutoImportSelector();

	@Test
	void test() {
		String[] imports = importSelector.selectImports(AnnotationMetadata.introspect(Config.class));
		String[] result = new String[] { ImportConfig.class.getName() };
		assertThat(imports).containsExactly(result);
	}

	@AutoImport
	@Target(ElementType.TYPE)
	@Retention(RetentionPolicy.RUNTIME)
	@interface AutoServiceImport {

	}

	@SpringAutoService(AutoServiceImport.class)
	static class ImportConfig {

	}

	@AutoServiceImport
	static class Config {

	}

}
