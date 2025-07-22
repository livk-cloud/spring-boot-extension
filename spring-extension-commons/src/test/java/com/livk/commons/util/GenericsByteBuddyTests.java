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

package com.livk.commons.util;

import net.bytebuddy.ClassFileVersion;
import net.bytebuddy.NamingStrategy;
import net.bytebuddy.description.modifier.Visibility;
import net.bytebuddy.dynamic.DynamicType;
import net.bytebuddy.dynamic.TypeResolutionStrategy;
import net.bytebuddy.dynamic.loading.ByteArrayClassLoader;
import net.bytebuddy.dynamic.loading.ClassLoadingStrategy;
import net.bytebuddy.implementation.MethodDelegation;
import net.bytebuddy.implementation.StubMethod;
import net.bytebuddy.matcher.ElementMatchers;
import org.junit.jupiter.api.Test;

import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Collections;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

/**
 * @author livk
 */
class GenericsByteBuddyTests {

	@Test
	void testEnumWithoutValuesIsIllegal() {
		assertThatThrownBy(() -> new GenericsByteBuddy().makeEnumeration()).isInstanceOf(IllegalArgumentException.class)
			.hasMessage("Require at least one enumeration constant");
	}

	@Test
	void testEnumeration() throws Exception {
		try (DynamicType.Unloaded<?> unloaded = new GenericsByteBuddy().makeEnumeration("foo").make()) {
			Class<?> type = unloaded.load(getClass().getClassLoader(), ClassLoadingStrategy.Default.WRAPPER)
				.getLoaded();
			assertThat(type.getModifiers())
				.satisfiesAnyOf(modifiers -> assertThat(Modifier.isPublic(modifiers)).isTrue());
			assertThat(type.isEnum()).isTrue();
			assertThat(type).isNotInterface();
			assertThat(type).isNotAnnotation();
		}
	}

	@Test
	void testInterface() {
		try (DynamicType.Unloaded<Object> unloaded = new GenericsByteBuddy().makeInterface().make()) {
			Class<?> type = unloaded.load(getClass().getClassLoader(), ClassLoadingStrategy.Default.WRAPPER)
				.getLoaded();
			assertThat(type.getModifiers())
				.satisfiesAnyOf(modifiers -> assertThat(Modifier.isPublic(modifiers)).isTrue());
			assertThat(type.isEnum()).isFalse();
			assertThat(type).isInterface();
			assertThat(type).isNotAnnotation();
		}
	}

	@Test
	void testAnnotation() {
		try (DynamicType.Unloaded<Annotation> unloaded = new GenericsByteBuddy().makeAnnotation().make()) {
			Class<?> type = unloaded.load(getClass().getClassLoader(), ClassLoadingStrategy.Default.WRAPPER)
				.getLoaded();
			assertThat(type.getModifiers())
				.satisfiesAnyOf(modifiers -> assertThat(Modifier.isPublic(modifiers)).isTrue());
			assertThat(type.isEnum()).isFalse();
			assertThat(type).isInterface();
			assertThat(type).isAnnotation();
		}
	}

	@Test
	void testRecordWithoutMember() throws Exception {
		try (DynamicType.Unloaded<Object> unloaded = new GenericsByteBuddy().with(ClassFileVersion.JAVA_V21)
			.makeRecord()
			.make()) {
			Class<?> type = unloaded.load(getClass().getClassLoader(), ClassLoadingStrategy.Default.WRAPPER)
				.getLoaded();
			assertThat((Boolean) Class.class.getMethod("isRecord").invoke(type)).isTrue();
			Object record = type.getConstructor().newInstance();

			assertThat((Integer) type.getMethod("hashCode").invoke(record)).isZero();
			assertThat((Boolean) type.getMethod("equals", Object.class).invoke(record, new Object())).isFalse();
			assertThat((Boolean) type.getMethod("equals", Object.class).invoke(record, record)).isTrue();
			assertThat(type.getMethod("toString").invoke(record)).isEqualTo(type.getSimpleName() + "[]");
		}
	}

	@Test
	void testRecordWithMember() throws Exception {
		try (DynamicType.Unloaded<Object> unloaded = new GenericsByteBuddy().with(ClassFileVersion.JAVA_V21)
			.makeRecord()
			.defineRecordComponent("foo", String.class)
			.make()) {
			Class<?> type = unloaded.load(getClass().getClassLoader(), ClassLoadingStrategy.Default.WRAPPER)
				.getLoaded();
			assertThat((Boolean) Class.class.getMethod("isRecord").invoke(type)).isTrue();
			Object record = type.getConstructor(String.class).newInstance("bar");
			assertThat(type.getMethod("foo").invoke(record)).isEqualTo("bar");
			assertThat((Integer) type.getMethod("hashCode").invoke(record)).isEqualTo("bar".hashCode());
			assertThat((Boolean) type.getMethod("equals", Object.class).invoke(record, new Object())).isFalse();
			assertThat((Boolean) type.getMethod("equals", Object.class).invoke(record, record)).isTrue();
			assertThat(type.getMethod("toString").invoke(record)).isEqualTo(type.getSimpleName() + "[foo=bar]");
			Object[] parameters = (Object[]) Constructor.class.getMethod("getParameters")
				.invoke(type.getDeclaredConstructor(String.class));
			assertThat(parameters).hasSize(1);
			assertThat(Class.forName("java.lang.reflect.Parameter").getMethod("getName").invoke(parameters[0]))
				.isEqualTo("foo");
			assertThat(Class.forName("java.lang.reflect.Parameter").getMethod("getModifiers").invoke(parameters[0]))
				.isEqualTo(0);
		}
	}

	@Test
	void testTypeInitializerInstrumentation() throws Exception {
		Recorder recorder = new Recorder();
		try (DynamicType.Unloaded<Object> unloaded = new GenericsByteBuddy().subclass(Object.class)
			.invokable(ElementMatchers.isTypeInitializer())
			.intercept(MethodDelegation.to(recorder))
			.make(new TypeResolutionStrategy.Active())) {
			Class<?> type = unloaded.load(getClass().getClassLoader(), ClassLoadingStrategy.Default.WRAPPER)
				.getLoaded();
			assertThat(type.getDeclaredConstructor().newInstance()).isInstanceOf(type);
			assertThat(recorder.counter).isEqualTo(1);
		}
	}

	@Test
	void testImplicitStrategyBootstrap() {
		try (DynamicType.Unloaded<Object> unloaded = new GenericsByteBuddy().subclass(Object.class).make()) {
			Class<?> type = unloaded.load(ClassLoadingStrategy.BOOTSTRAP_LOADER).getLoaded();
			assertThat(type.getClassLoader()).isNotNull();
		}
	}

	@Test
	void testImplicitStrategyNonBootstrap() {
		ClassLoader classLoader = new URLClassLoader(new URL[0], ClassLoadingStrategy.BOOTSTRAP_LOADER);
		try (DynamicType.Unloaded<Object> unloaded = new GenericsByteBuddy().subclass(Object.class).make()) {
			Class<?> type = unloaded.load(classLoader).getLoaded();
			assertThat(type.getClassLoader()).isNotEqualTo(classLoader);
		}
	}

	@Test
	void testImplicitStrategyInjectable() {
		ClassLoader classLoader = new ByteArrayClassLoader(ClassLoadingStrategy.BOOTSTRAP_LOADER, false,
				Collections.emptyMap());
		try (DynamicType.Unloaded<Object> unloaded = new GenericsByteBuddy().subclass(Object.class).make()) {
			Class<?> type = unloaded.load(classLoader).getLoaded();
			assertThat(type.getClassLoader()).isEqualTo(classLoader);
		}
	}

	@Test
	void testClassWithManyMethods() {
		DynamicType.Builder<?> builder = new GenericsByteBuddy().subclass(Object.class);
		for (int index = 0; index < 1000; index++) {
			builder = builder.defineMethod("method" + index, void.class, Visibility.PUBLIC)
				.intercept(StubMethod.INSTANCE);
		}
		try (DynamicType.Unloaded<?> make = builder.make()) {
			Class<?> type = make.load(ClassLoadingStrategy.BOOTSTRAP_LOADER, ClassLoadingStrategy.Default.WRAPPER)
				.getLoaded();
			assertThat(type.getDeclaredMethods()).hasSize(1000);

			DynamicType.Builder<?> subclassBuilder = new GenericsByteBuddy().subclass(type);
			for (Method method : type.getDeclaredMethods()) {
				subclassBuilder = subclassBuilder.method(ElementMatchers.is(method)).intercept(StubMethod.INSTANCE);
			}
			try (DynamicType.Unloaded<?> unloaded = subclassBuilder.make()) {
				Class<?> subclass = unloaded.load(type.getClassLoader(), ClassLoadingStrategy.Default.WRAPPER)
					.getLoaded();
				assertThat(subclass.getDeclaredMethods()).hasSize(1000);
			}
		}
	}

	@Test
	void testClassCompiledToJsr14() throws Exception {
		try (DynamicType.Unloaded<?> unloaded = new GenericsByteBuddy()
			.redefine(Class.forName("com.livk.commons.util.GenericsByteBuddyTests$Jsr14Sample"))
			.make()) {
			assertThat(unloaded.load(ClassLoadingStrategy.BOOTSTRAP_LOADER, ClassLoadingStrategy.Default.WRAPPER)
				.getLoaded()
				.getConstructor()
				.newInstance()).isNotNull();
		}
	}

	@Test
	void testCallerSuffixNamingStrategy() {
		try (DynamicType.Unloaded<Object> unloaded = new GenericsByteBuddy()
			.with(new NamingStrategy.Suffixing("SuffixedName",
					new GenericsByteBuddy.WithCallerSuffix(
							new NamingStrategy.Suffixing.BaseNameResolver.ForFixedValue("foo.Bar"))))
			.subclass(Object.class)
			.make()) {
			Class<?> type = unloaded.load(ClassLoadingStrategy.BOOTSTRAP_LOADER, ClassLoadingStrategy.Default.WRAPPER)
				.getLoaded();
			String expectedName = "foo.Bar$" + GenericsByteBuddyTests.class.getName().replace('.', '$')
					+ "$testCallerSuffixNamingStrategy$SuffixedName";
			assertThat(type.getName()).isEqualTo(expectedName);
		}
	}

	@SuppressWarnings("unused")
	public static class Recorder {

		public int counter;

		public void instrument() {
			counter++;
		}

	}

	@SuppressWarnings("unused")
	public static class Jsr14Sample<TYPE> {

		public Jsr14Sample<TYPE> field;

		public Jsr14Sample<TYPE> method() {
			return null;
		}

	}

}
