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

import net.bytebuddy.ByteBuddy;
import net.bytebuddy.description.modifier.ModifierContributor;
import net.bytebuddy.description.modifier.TypeManifestation;
import net.bytebuddy.description.modifier.Visibility;
import net.bytebuddy.description.type.TypeDefinition;
import net.bytebuddy.description.type.TypeDescription;
import net.bytebuddy.description.type.TypeList;
import net.bytebuddy.dynamic.DynamicType;
import net.bytebuddy.dynamic.scaffold.subclass.ConstructorStrategy;
import net.bytebuddy.dynamic.scaffold.subclass.SubclassDynamicTypeBuilder;

/**
 * @author livk
 */
public final class GenericsByteBuddy extends ByteBuddy {

	/**
	 * Sub dynamic type . builder.
	 * @param <T> the type parameter
	 * @param type the type
	 * @param generics the generics
	 * @return the dynamic type . builder
	 */
	public <T> DynamicType.Builder<T> subType(Class<?> type, Class<?>... generics) {
		TypeDefinition description = TypeDescription.Generic.Builder.parameterizedType(type, generics).build();
		return subType(description);
	}

	/**
	 * Sub dynamic type . builder.
	 * @param <T> the type parameter
	 * @param superType the super type
	 * @return the dynamic type . builder
	 */
	public <T> DynamicType.Builder<T> subType(TypeDefinition superType) {
		return subType(superType, ConstructorStrategy.Default.IMITATE_SUPER_CLASS_OPENING);
	}

	/**
	 * Sub dynamic type . builder.
	 * @param <T> the type parameter
	 * @param superType the super type
	 * @param constructorStrategy the constructor strategy
	 * @return the dynamic type . builder
	 */
	public <T> DynamicType.Builder<T> subType(TypeDefinition superType, ConstructorStrategy constructorStrategy) {
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
				classReaderFactory, classWriterFactory, ignoredMethods, constructorStrategy);
	}

}
