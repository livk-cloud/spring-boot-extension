/*
 * Copyright 2021 spring-boot-extension the original author or authors.
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
 *
 */

package com.livk.autoconfigure.redisson;

import com.google.common.collect.Sets;
import com.livk.commons.util.BeanUtils;
import com.livk.commons.util.ClassUtils;
import io.netty.channel.EventLoopGroup;
import net.bytebuddy.ByteBuddy;
import net.bytebuddy.ClassFileVersion;
import net.bytebuddy.description.modifier.ModifierContributor;
import net.bytebuddy.description.modifier.TypeManifestation;
import net.bytebuddy.description.modifier.Visibility;
import net.bytebuddy.description.type.TypeDefinition;
import net.bytebuddy.description.type.TypeDescription;
import net.bytebuddy.description.type.TypeList;
import net.bytebuddy.dynamic.DynamicType;
import net.bytebuddy.dynamic.scaffold.subclass.ConstructorStrategy;
import net.bytebuddy.dynamic.scaffold.subclass.SubclassDynamicTypeBuilder;
import net.bytebuddy.matcher.ElementMatchers;
import org.redisson.api.NameMapper;
import org.redisson.api.NatMapper;
import org.redisson.api.RedissonNodeInitializer;
import org.redisson.client.NettyHook;
import org.redisson.client.codec.Codec;
import org.redisson.config.CommandMapper;
import org.redisson.config.Config;
import org.redisson.config.CredentialsResolver;
import org.redisson.connection.AddressResolverGroupFactory;
import org.redisson.connection.ConnectionListener;
import org.redisson.connection.balancer.LoadBalancer;

import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.TrustManagerFactory;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.stream.Collectors;

/**
 * The type Byte buddy proxy support.
 *
 * @author livk
 */
final class ByteBuddySupport {

	private static final String PACKAGE_NAME = "com.livk.autoconfigure.redisson.";

	private static final Set<Class<?>> supportType = Sets.newHashSet(Config.class, AddressResolverGroupFactory.class,
			Codec.class, RedissonNodeInitializer.class, LoadBalancer.class, NatMapper.class, NameMapper.class,
			NettyHook.class, CredentialsResolver.class, EventLoopGroup.class, ConnectionListener.class,
			ExecutorService.class, KeyManagerFactory.class, TrustManagerFactory.class, CommandMapper.class);

	/**
	 * Proxy converters set.
	 * @return the set
	 */
	public static Set<ConfigBaseConverter<?>> makeConverters() {
		return supportType.stream().map(ByteBuddySupport::make).collect(Collectors.toSet());
	}

	private static ConfigBaseConverter<?> make(Class<?> type) {
		String name = PACKAGE_NAME + type.getSimpleName() + ConfigBaseConverter.class.getSimpleName() + "$Proxy";
		DynamicType.Builder.MethodDefinition.ReceiverTypeDefinition<ConfigBaseConverter<?>> typeDefinition = new GenericsByteBuddy(
				ClassFileVersion.JAVA_V17)
			.<ConfigBaseConverter<?>>sub(ConfigBaseConverter.class, type)
			.name(name)
			.method(ElementMatchers.none())
			.withoutCode();
		try (DynamicType.Unloaded<ConfigBaseConverter<?>> unloaded = typeDefinition.make()) {
			Class<? extends ConfigBaseConverter<?>> proxyType = unloaded.load(ClassUtils.getDefaultClassLoader())
				.getLoaded();
			return BeanUtils.instantiateClass(proxyType);
		}
	}

	private static final class GenericsByteBuddy extends ByteBuddy {

		/**
		 * Instantiates a new Generics byte buddy.
		 * @param classFileVersion the class file version
		 */
		public GenericsByteBuddy(ClassFileVersion classFileVersion) {
			super(classFileVersion);
		}

		/**
		 * Sub dynamic type . builder.
		 * @param <T> the type parameter
		 * @param type the type
		 * @param generics the generics
		 * @return the dynamic type . builder
		 */
		public <T> DynamicType.Builder<T> sub(Class<?> type, Class<?>... generics) {
			TypeDefinition description = TypeDescription.Generic.Builder.parameterizedType(type, generics).build();
			return sub(description);
		}

		/**
		 * Sub dynamic type . builder.
		 * @param <T> the type parameter
		 * @param superType the super type
		 * @return the dynamic type . builder
		 */
		public <T> DynamicType.Builder<T> sub(TypeDefinition superType) {
			return sub(superType, ConstructorStrategy.Default.IMITATE_SUPER_CLASS_OPENING);
		}

		/**
		 * Sub dynamic type . builder.
		 * @param <T> the type parameter
		 * @param superType the super type
		 * @param constructorStrategy the constructor strategy
		 * @return the dynamic type . builder
		 */
		public <T> DynamicType.Builder<T> sub(TypeDefinition superType, ConstructorStrategy constructorStrategy) {
			TypeDescription.Generic actualSuperType;
			TypeList.Generic interfaceTypes;
			if (superType.isPrimitive() || superType.isArray() || superType.isFinal()) {
				throw new IllegalArgumentException("Cannot sub primitive, array or final types: " + superType);
			}
			else if (superType.isInterface()) {
				actualSuperType = TypeDescription.Generic.OfNonGenericType.ForLoadedType.of(Object.class);
				interfaceTypes = new TypeList.Generic.Explicit(superType);
			}
			else {
				actualSuperType = superType.asGenericType();
				interfaceTypes = new TypeList.Generic.Empty();
			}
			return new SubclassDynamicTypeBuilder<>(
					instrumentedTypeFactory.subclass(namingStrategy.subclass(superType.asGenericType()),
							ModifierContributor.Resolver.of(Visibility.PUBLIC, TypeManifestation.PLAIN)
								.resolve(superType.getModifiers()),
							actualSuperType)
						.withInterfaces(interfaceTypes),
					classFileVersion, auxiliaryTypeNamingStrategy, annotationValueFilterFactory, annotationRetention,
					implementationContextFactory, methodGraphCompiler, typeValidation, visibilityBridgeStrategy,
					classWriterStrategy, ignoredMethods, constructorStrategy);
		}

	}

}
