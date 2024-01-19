/*
 * Copyright 2021-2024 spring-boot-extension the original author or authors.
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
 */

package com.livk.commons.cglib;

import java.lang.ref.WeakReference;
import java.security.ProtectionDomain;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.WeakHashMap;
import org.springframework.asm.ClassReader;
import org.springframework.cglib.core.ClassGenerator;
import org.springframework.cglib.core.ClassNameReader;
import org.springframework.cglib.core.CodeGenerationException;
import org.springframework.cglib.core.DefaultGeneratorStrategy;
import org.springframework.cglib.core.DefaultNamingPolicy;
import org.springframework.cglib.core.GeneratorStrategy;
import org.springframework.cglib.core.NamingPolicy;
import org.springframework.cglib.core.Predicate;
import org.springframework.cglib.core.ReflectUtils;
import org.springframework.cglib.core.internal.Function;
import org.springframework.cglib.core.internal.LoadingCache;

/**
 * The type Abstract class generator.
 *
 * @param <T> the type parameter
 * @author livk
 */
abstract class AbstractClassGenerator<T> implements ClassGenerator {

	private static final ThreadLocal<AbstractClassGenerator<?>> CURRENT = new ThreadLocal<>();

	private static final boolean DEFAULT_USE_CACHE = Boolean.parseBoolean(System.getProperty("cglib.useCache", "true"));

	// See
	// https://github.com/oracle/graal/blob/master/sdk/src/org.graalvm.nativeimage/src/org/graalvm/nativeimage/ImageInfo.java
	private static final boolean imageCode = (System.getProperty("org.graalvm.nativeimage.imagecode") != null);

	private static volatile Map<ClassLoader, ClassLoaderData> CACHE = new WeakHashMap<>();

	private final Source source;

	private GeneratorStrategy strategy = DefaultGeneratorStrategy.INSTANCE;

	private NamingPolicy namingPolicy = DefaultNamingPolicy.INSTANCE;

	private ClassLoader classLoader;

	private Class<?> contextClass;

	private String namePrefix;

	private Object key;

	private boolean useCache = DEFAULT_USE_CACHE;

	private String className;

	private boolean attemptLoad;

	/**
	 * Instantiates a new Abstract class generator.
	 * @param source the source
	 */
	protected AbstractClassGenerator(Source source) {
		this.source = source;
	}

	/**
	 * Used internally by CGLIB. Returns the <code>AbstractClassGenerator</code> that is
	 * being used to generate a class in the current thread.
	 * @return the current
	 */
	public static AbstractClassGenerator<?> getCurrent() {
		return CURRENT.get();
	}

	/**
	 * Wrap cached class t.
	 * @param klass the klass
	 * @return the t
	 */
	@SuppressWarnings("unchecked")
	protected T wrapCachedClass(Class<?> klass) {
		return (T) new WeakReference<>(klass);
	}

	/**
	 * Unwrap cached value object.
	 * @param cached the cached
	 * @return the object
	 */
	protected Object unwrapCachedValue(Object cached) {
		return ((WeakReference<?>) cached).get();
	}

	/**
	 * Sets name prefix.
	 * @param namePrefix the name prefix
	 */
	protected void setNamePrefix(String namePrefix) {
		this.namePrefix = namePrefix;
	}

	/**
	 * Gets class name.
	 * @return the class name
	 */
	final protected String getClassName() {
		return className;
	}

	private void setClassName(String className) {
		this.className = className;
	}

	private String generateClassName(Predicate nameTestPredicate) {
		return namingPolicy.getClassName(namePrefix, source.name, key, nameTestPredicate);
	}

	/**
	 * Sets context class.
	 * @param contextClass the context class
	 */
	// SPRING PATCH BEGIN
	public void setContextClass(Class<?> contextClass) {
		this.contextClass = contextClass;
	}

	/**
	 * Gets naming policy.
	 * @return the naming policy
	 * @see #setNamingPolicy #setNamingPolicy
	 */
	public NamingPolicy getNamingPolicy() {
		return namingPolicy;
	}

	/**
	 * Override the default naming policy.
	 * @param namingPolicy the custom policy, or null to use the default
	 * @see DefaultNamingPolicy
	 */
	public void setNamingPolicy(NamingPolicy namingPolicy) {
		if (namingPolicy == null) {
			namingPolicy = DefaultNamingPolicy.INSTANCE;
		}
		this.namingPolicy = namingPolicy;
	}
	// SPRING PATCH END

	/**
	 * Gets use cache.
	 * @return the use cache
	 * @see #setUseCache #setUseCache
	 */
	public boolean getUseCache() {
		return useCache;
	}

	/**
	 * Whether use and update the static cache of generated classes for a class with the
	 * same properties. Default is <code>true</code>.
	 * @param useCache the use cache
	 */
	public void setUseCache(boolean useCache) {
		this.useCache = useCache;
	}

	/**
	 * Gets attempt load.
	 * @return the attempt load
	 */
	public boolean getAttemptLoad() {
		return attemptLoad;
	}

	/**
	 * If set, CGLIB will attempt to load classes from the specified
	 * <code>ClassLoader</code> before generating them. Because generated class names are
	 * not guaranteed to be unique, the default is <code>false</code>.
	 * @param attemptLoad the attempt load
	 */
	public void setAttemptLoad(boolean attemptLoad) {
		this.attemptLoad = attemptLoad;
	}

	/**
	 * Gets strategy.
	 * @return the strategy
	 * @see #setStrategy #setStrategy
	 */
	public GeneratorStrategy getStrategy() {
		return strategy;
	}

	/**
	 * Set the strategy to use to create the bytecode from this generator. By default an
	 * instance of {@link DefaultGeneratorStrategy} is used.
	 * @param strategy the strategy
	 */
	public void setStrategy(GeneratorStrategy strategy) {
		if (strategy == null) {
			strategy = DefaultGeneratorStrategy.INSTANCE;
		}
		this.strategy = strategy;
	}

	/**
	 * Gets class loader.
	 * @return the class loader
	 */
	public ClassLoader getClassLoader() {
		ClassLoader t = classLoader;
		if (t == null) {
			t = getDefaultClassLoader();
		}
		if (t == null) {
			t = getClass().getClassLoader();
		}
		if (t == null) {
			t = Thread.currentThread().getContextClassLoader();
		}
		if (t == null) {
			throw new IllegalStateException("Cannot determine classloader");
		}
		return t;
	}

	/**
	 * Set the <code>ClassLoader</code> in which the class will be generated. Concrete
	 * subclasses of <code>AbstractClassGenerator</code> (such as <code>Enhancer</code>)
	 * will try to choose an appropriate default if this is unset.
	 * <p>
	 * Classes are cached per-<code>ClassLoader</code> using a <code>WeakHashMap</code>,
	 * to allow the generated classes to be removed when the associated loader is garbage
	 * collected.
	 * @param classLoader the loader to generate the new class with, or null to use the
	 * default
	 */
	public void setClassLoader(ClassLoader classLoader) {
		this.classLoader = classLoader;
	}

	/**
	 * Gets default class loader.
	 * @return the default class loader
	 */
	abstract protected ClassLoader getDefaultClassLoader();

	/**
	 * Returns the protection domain to use when defining the class.
	 * <p>
	 * Default implementation returns <code>null</code> for using a default protection
	 * domain. Sub-classes may override to use a more specific protection domain.
	 * </p>
	 * @return the protection domain (<code>null</code> for using a default)
	 */
	protected ProtectionDomain getProtectionDomain() {
		return null;
	}

	/**
	 * Create object.
	 * @param key the key
	 * @return the object
	 */
	protected Object create(Object key) {
		try {
			ClassLoader loader = getClassLoader();
			Map<ClassLoader, ClassLoaderData> cache = CACHE;
			ClassLoaderData data = cache.get(loader);
			if (data == null) {
				synchronized (org.springframework.cglib.core.AbstractClassGenerator.class) {
					cache = CACHE;
					data = cache.get(loader);
					if (data == null) {
						Map<ClassLoader, ClassLoaderData> newCache = new WeakHashMap<>(cache);
						data = new ClassLoaderData(loader);
						newCache.put(loader, data);
						CACHE = newCache;
					}
				}
			}
			this.key = key;
			Object obj = data.get(this, getUseCache());
			if (obj instanceof Class<?> clazz) {
				return firstInstance(clazz);
			}
			return nextInstance(obj);
		}
		catch (RuntimeException | Error ex) {
			throw ex;
		}
		catch (Exception ex) {
			throw new CodeGenerationException(ex);
		}
	}

	/**
	 * Generate class.
	 * @param data the data
	 * @return the class
	 */
	protected Class<?> generate(ClassLoaderData data) {
		Class<?> gen;
		AbstractClassGenerator<?> save = CURRENT.get();
		CURRENT.set(this);
		try {
			ClassLoader classLoader = data.getClassLoader();
			if (classLoader == null) {
				throw new IllegalStateException("ClassLoader is null while trying to define class " + getClassName()
						+ ". It seems that the loader has been expired from a weak reference somehow. "
						+ "Please file an issue at cglib's issue tracker.");
			}
			synchronized (classLoader) {
				String name = generateClassName(data.getUniqueNamePredicate());
				data.reserveName(name);
				this.setClassName(name);
			}
			if (attemptLoad) {
				try {
					// SPRING PATCH BEGIN
					synchronized (classLoader) { // just in case
						gen = ReflectUtils.loadClass(getClassName(), classLoader);
					}
					// SPRING PATCH END
					return gen;
				}
				catch (ClassNotFoundException e) {
					// ignore
				}
			}
			// SPRING PATCH BEGIN
			if (imageCode) {
				throw new UnsupportedOperationException("CGLIB runtime enhancement not supported on native image. "
						+ "Make sure to include a pre-generated class on the classpath instead: " + getClassName());
			}
			// SPRING PATCH END
			byte[] b = strategy.generate(this);
			String className = ClassNameReader.getClassName(new ClassReader(b));
			ProtectionDomain protectionDomain = getProtectionDomain();
			synchronized (classLoader) { // just in case
				// SPRING PATCH BEGIN
				gen = ReflectUtils.defineClass(className, b, classLoader, protectionDomain, contextClass);
				// SPRING PATCH END
			}
			return gen;
		}
		catch (RuntimeException | Error ex) {
			throw ex;
		}
		catch (Exception ex) {
			throw new CodeGenerationException(ex);
		}
		finally {
			CURRENT.set(save);
		}
	}

	/**
	 * First instance object.
	 * @param type the type
	 * @return the object
	 * @throws Exception the exception
	 */
	protected abstract Object firstInstance(Class<?> type) throws Exception;

	/**
	 * Next instance object.
	 * @param instance the instance
	 * @return the object
	 * @throws Exception the exception
	 */
	protected abstract Object nextInstance(Object instance) throws Exception;

	/**
	 * The type Class loader data.
	 */
	protected static class ClassLoaderData {

		private static final Function<AbstractClassGenerator<?>, Object> GET_KEY = gen -> gen.key;

		private final Set<String> reservedClassNames = new HashSet<>();

		/**
		 * {@link AbstractClassGenerator} here holds "cache key" (e.g.
		 * {@link org.springframework.cglib.proxy.Enhancer} configuration), and the value
		 * is the generated class plus some additional values (see
		 * {@link #unwrapCachedValue(Object)}.
		 * <p>
		 * The generated classes can be reused as long as their classloader is reachable.
		 * </p>
		 * <p>
		 * Note: the only way to access a class is to find it through generatedClasses
		 * cache, thus the key should not expire as long as the class itself is alive (its
		 * classloader is alive).
		 * </p>
		 */
		private final LoadingCache<AbstractClassGenerator<?>, Object, Object> generatedClasses;

		/**
		 * Note: ClassLoaderData object is stored as a value of
		 * {@code WeakHashMap<ClassLoader, ...>} thus this classLoader reference should be
		 * weak otherwise it would make classLoader strongly reachable and alive forever.
		 * Reference queue is not required since the cleanup is handled by
		 * {@link WeakHashMap}.
		 */
		private final WeakReference<ClassLoader> classLoader;

		private final Predicate uniqueNamePredicate = this.reservedClassNames::contains;

		/**
		 * Instantiates a new Class loader data.
		 * @param classLoader the class loader
		 */
		public ClassLoaderData(ClassLoader classLoader) {
			if (classLoader == null) {
				throw new IllegalArgumentException("classLoader == null is not yet supported");
			}
			this.classLoader = new WeakReference<>(classLoader);
			Function<AbstractClassGenerator<?>, Object> load = gen -> {
				Class<?> klass = gen.generate(ClassLoaderData.this);
				return gen.wrapCachedClass(klass);
			};
			generatedClasses = new LoadingCache<>(GET_KEY, load);
		}

		/**
		 * Gets class loader.
		 * @return the class loader
		 */
		public ClassLoader getClassLoader() {
			return classLoader.get();
		}

		/**
		 * Reserve name.
		 * @param name the name
		 */
		public void reserveName(String name) {
			reservedClassNames.add(name);
		}

		/**
		 * Gets unique name predicate.
		 * @return the unique name predicate
		 */
		public Predicate getUniqueNamePredicate() {
			return uniqueNamePredicate;
		}

		/**
		 * Get object.
		 * @param gen the gen
		 * @param useCache the use cache
		 * @return the object
		 */
		public Object get(AbstractClassGenerator<?> gen, boolean useCache) {
			if (!useCache) {
				return gen.generate(ClassLoaderData.this);
			}
			else {
				Object cachedValue = generatedClasses.get(gen);
				return gen.unwrapCachedValue(cachedValue);
			}
		}

	}

	/**
	 * The type Source.
	 */
	protected static class Source {

		/**
		 * The Name.
		 */
		String name;

		/**
		 * Instantiates a new Source.
		 * @param name the name
		 */
		public Source(String name) {
			this.name = name;
		}

	}

}
