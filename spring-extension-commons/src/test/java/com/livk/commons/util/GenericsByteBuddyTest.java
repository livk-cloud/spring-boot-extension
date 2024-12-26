/*
 * Copyright 2021-2024 spring-boot-extension the original author or authors.
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *       https://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.livk.commons.util;

import net.bytebuddy.ClassFileVersion;
import net.bytebuddy.NamingStrategy;
import net.bytebuddy.build.HashCodeAndEqualsPlugin;
import net.bytebuddy.description.modifier.Visibility;
import net.bytebuddy.description.type.TypeDescription;
import net.bytebuddy.dynamic.DynamicType;
import net.bytebuddy.dynamic.TypeResolutionStrategy;
import net.bytebuddy.dynamic.loading.ByteArrayClassLoader;
import net.bytebuddy.dynamic.loading.ClassLoadingStrategy;
import net.bytebuddy.implementation.MethodDelegation;
import net.bytebuddy.implementation.StubMethod;
import net.bytebuddy.matcher.ElementMatchers;
import org.junit.jupiter.api.Test;
import org.springframework.lang.NonNull;

import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Collections;

import static net.bytebuddy.matcher.ElementMatchers.isTypeInitializer;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * @author livk
 */
class GenericsByteBuddyTest {

	@Test
	void testEnumWithoutValuesIsIllegal() {
		IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
				() -> new GenericsByteBuddy().makeEnumeration());
		assertEquals("Require at least one enumeration constant", exception.getMessage());
	}

	@Test
	void testEnumeration() {
		try (DynamicType.Unloaded<?> unloaded = new GenericsByteBuddy().makeEnumeration("foo").make()) {
			Class<?> type = unloaded.load(getClass().getClassLoader(), ClassLoadingStrategy.Default.WRAPPER)
				.getLoaded();
			assertTrue(Modifier.isPublic(type.getModifiers()));
			assertTrue(type.isEnum());
			assertFalse(type.isInterface());
			assertFalse(type.isAnnotation());
		}
	}

	@Test
	void testInterface() {
		try (DynamicType.Unloaded<Object> unloaded = new GenericsByteBuddy().makeInterface().make()) {
			Class<?> type = unloaded.load(getClass().getClassLoader(), ClassLoadingStrategy.Default.WRAPPER)
				.getLoaded();
			assertTrue(Modifier.isPublic(type.getModifiers()));
			assertFalse(type.isEnum());
			assertTrue(type.isInterface());
			assertFalse(type.isAnnotation());
		}
	}

	@Test
	void testAnnotation() {
		try (DynamicType.Unloaded<Annotation> unloaded = new GenericsByteBuddy().makeAnnotation().make()) {
			Class<?> type = unloaded.load(getClass().getClassLoader(), ClassLoadingStrategy.Default.WRAPPER)
				.getLoaded();
			assertTrue(Modifier.isPublic(type.getModifiers()));
			assertFalse(type.isEnum());
			assertTrue(type.isInterface());
			assertTrue(type.isAnnotation());
		}
	}

	@Test
	void testRecordWithoutMember() throws Exception {
		try (DynamicType.Unloaded<Object> unloaded = new GenericsByteBuddy().with(ClassFileVersion.JAVA_V21)
			.makeRecord()
			.make()) {
			Class<?> type = unloaded.load(getClass().getClassLoader(), ClassLoadingStrategy.Default.WRAPPER)
				.getLoaded();
			assertEquals(true, Class.class.getMethod("isRecord").invoke(type));
			Object record = type.getConstructor().newInstance();

			assertEquals(0, type.getMethod("hashCode").invoke(record));
			assertEquals(false, type.getMethod("equals", Object.class).invoke(record, new Object()));
			assertEquals(true, type.getMethod("equals", Object.class).invoke(record, record));
			assertEquals(type.getSimpleName() + "[]", type.getMethod("toString").invoke(record));
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
			assertEquals(true, Class.class.getMethod("isRecord").invoke(type));
			Object record = type.getConstructor(String.class).newInstance("bar");
			assertEquals("bar", type.getMethod("foo").invoke(record));
			assertEquals("bar".hashCode(), type.getMethod("hashCode").invoke(record));
			assertEquals(false, type.getMethod("equals", Object.class).invoke(record, new Object()));
			assertEquals(true, type.getMethod("equals", Object.class).invoke(record, record));
			assertEquals(type.getSimpleName() + "[foo=bar]", type.getMethod("toString").invoke(record));
			Object[] parameter = (Object[]) Constructor.class.getMethod("getParameters")
				.invoke(type.getDeclaredConstructor(String.class));
			assertEquals(1, parameter.length);
			assertEquals("foo", Class.forName("java.lang.reflect.Parameter").getMethod("getName").invoke(parameter[0]));
			assertEquals(0,
					Class.forName("java.lang.reflect.Parameter").getMethod("getModifiers").invoke(parameter[0]));
		}
	}

	@Test
	void testTypeInitializerInstrumentation() throws Exception {
		Recorder recorder = new Recorder();
		try (DynamicType.Unloaded<Object> unloaded = new GenericsByteBuddy().subclass(Object.class)
			.invokable(isTypeInitializer())
			.intercept(MethodDelegation.to(recorder))
			.make(new TypeResolutionStrategy.Active())) {
			Class<?> type = unloaded.load(getClass().getClassLoader(), ClassLoadingStrategy.Default.WRAPPER)
				.getLoaded();
			assertInstanceOf(type, type.getDeclaredConstructor().newInstance());
			assertEquals(1, recorder.counter);
		}
	}

	@Test
	void testImplicitStrategyBootstrap() {
		try (DynamicType.Unloaded<Object> unloaded = new GenericsByteBuddy().subclass(Object.class).make()) {
			Class<?> type = unloaded.load(ClassLoadingStrategy.BOOTSTRAP_LOADER).getLoaded();
			assertNotNull(type.getClassLoader());
		}
	}

	@Test
	void testImplicitStrategyNonBootstrap() {
		ClassLoader classLoader = new URLClassLoader(new URL[0], ClassLoadingStrategy.BOOTSTRAP_LOADER);
		try (DynamicType.Unloaded<Object> unloaded = new GenericsByteBuddy().subclass(Object.class).make()) {
			Class<?> type = unloaded.load(classLoader).getLoaded();
			assertNotEquals(classLoader, type.getClassLoader());
		}
	}

	@Test
	void testImplicitStrategyInjectable() {
		ClassLoader classLoader = new ByteArrayClassLoader(ClassLoadingStrategy.BOOTSTRAP_LOADER, false,
				Collections.emptyMap());
		try (DynamicType.Unloaded<Object> unloaded = new GenericsByteBuddy().subclass(Object.class).make()) {
			Class<?> type = unloaded.load(classLoader).getLoaded();
			assertEquals(type.getClassLoader(), classLoader);
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
			assertEquals(1000, type.getDeclaredMethods().length);
			DynamicType.Builder<?> subclassBuilder = new GenericsByteBuddy().subclass(type);
			for (Method method : type.getDeclaredMethods()) {
				subclassBuilder = subclassBuilder.method(ElementMatchers.is(method)).intercept(StubMethod.INSTANCE);
			}
			try (DynamicType.Unloaded<?> unloaded = subclassBuilder.make()) {
				Class<?> subclass = unloaded.load(type.getClassLoader(), ClassLoadingStrategy.Default.WRAPPER)
					.getLoaded();
				assertEquals(1000, subclass.getDeclaredMethods().length);
			}
		}
	}

	@Test
	void testClassCompiledToJsr14() throws Exception {
		try (DynamicType.Unloaded<?> unloaded = new GenericsByteBuddy()
			.redefine(Class.forName("com.livk.commons.util.GenericsByteBuddyTest$Jsr14Sample"))
			.make()) {
			assertNotNull(unloaded.load(ClassLoadingStrategy.BOOTSTRAP_LOADER, ClassLoadingStrategy.Default.WRAPPER)
				.getLoaded()
				.getConstructor()
				.newInstance());
		}
	}

	@Test
	void testCallerSuffixNamingStrategy() {
		try (DynamicType.Unloaded<Object> unloaded = new GenericsByteBuddy()
			.with(new NamingStrategy.Suffixing("SuffixedName",
					new WithCallerSuffix(new NamingStrategy.Suffixing.BaseNameResolver.ForFixedValue("foo.Bar"))))
			.subclass(Object.class)
			.make()) {
			Class<?> type = unloaded.load(ClassLoadingStrategy.BOOTSTRAP_LOADER, ClassLoadingStrategy.Default.WRAPPER)
				.getLoaded();
			assertEquals("foo.Bar$" + GenericsByteBuddyTest.class.getName().replace('.', '$')
					+ "$testCallerSuffixNamingStrategy$SuffixedName", type.getName());
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

	@HashCodeAndEqualsPlugin.Enhance
	public static class WithCallerSuffix implements NamingStrategy.Suffixing.BaseNameResolver {

		private final NamingStrategy.Suffixing.BaseNameResolver delegate;

		public WithCallerSuffix(NamingStrategy.Suffixing.BaseNameResolver delegate) {
			this.delegate = delegate;
		}

		@NonNull
		@Override
		public String resolve(@NonNull TypeDescription typeDescription) {
			boolean matched = false;
			String caller = null;

			for (StackTraceElement stackTraceElement : (new Throwable()).getStackTrace()) {
				if (stackTraceElement.getClassName().equals(GenericsByteBuddy.class.getName())) {
					matched = true;
				}
				else if (matched) {
					caller = stackTraceElement.getClassName() + "." + stackTraceElement.getMethodName();
					break;
				}
			}

			if (caller == null) {
				throw new IllegalStateException("Base name resolver not invoked via " + GenericsByteBuddy.class);
			}
			else {
				return this.delegate.resolve(typeDescription) + "$" + caller.replace('.', '$');
			}
		}

	}

}
