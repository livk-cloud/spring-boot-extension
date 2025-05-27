package com.livk.auto.service.processor;

import com.livk.auto.service.processor.factories.aot.AotRuntimeHintsRegistrar;
import org.junit.jupiter.api.Test;
import org.springframework.aot.hint.RuntimeHintsRegistrar;
import org.springframework.core.io.UrlResource;
import org.springframework.core.test.tools.SourceFile;
import org.springframework.core.test.tools.TestCompiler;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Collections;
import java.util.Enumeration;
import java.util.Properties;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * @author livk
 */
class AotFactoriesProcessorTest {

	@Test
	void test() {
		compile(AotRuntimeHintsRegistrar.class, RuntimeHintsRegistrar.class);
	}

	private void compile(Class<?> type, Class<?> factoryClass) {
		TestCompiler.forSystem()
			.withProcessors(new AotFactoriesProcessor())
			.withSources(SourceFile.forTestClass(type))
			.compile(compiled -> {
				try {
					Enumeration<URL> resources = compiled.getClassLoader()
						.getResources(AotFactoriesProcessor.AOT_LOCATION);
					Properties pro = new Properties();
					for (URL url : Collections.list(resources)) {
						InputStream inputStream = new UrlResource(url).getInputStream();
						Properties properties = new Properties();
						properties.load(inputStream);
						pro.putAll(properties);
					}
					assertTrue(pro.containsKey(factoryClass.getName()));
					assertEquals(type.getName(), pro.get(factoryClass.getName()));
				}
				catch (IOException e) {
					throw new CompilationException(e, SourceFile.forTestClass(type));
				}
			});
	}

}
