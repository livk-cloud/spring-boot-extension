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
import net.bytebuddy.build.AccessControllerPlugin;
import net.bytebuddy.build.HashCodeAndEqualsPlugin;
import net.bytebuddy.description.annotation.AnnotationValue;
import net.bytebuddy.description.field.FieldDescription;
import net.bytebuddy.description.method.MethodDescription;
import net.bytebuddy.description.method.ParameterDescription;
import net.bytebuddy.description.modifier.EnumerationState;
import net.bytebuddy.description.modifier.ModifierContributor;
import net.bytebuddy.description.modifier.Ownership;
import net.bytebuddy.description.modifier.TypeManifestation;
import net.bytebuddy.description.modifier.Visibility;
import net.bytebuddy.description.type.PackageDescription;
import net.bytebuddy.description.type.RecordComponentDescription;
import net.bytebuddy.description.type.TypeDefinition;
import net.bytebuddy.description.type.TypeDescription;
import net.bytebuddy.description.type.TypeList;
import net.bytebuddy.dynamic.ClassFileLocator;
import net.bytebuddy.dynamic.DynamicType;
import net.bytebuddy.dynamic.TargetType;
import net.bytebuddy.dynamic.Transformer;
import net.bytebuddy.dynamic.VisibilityBridgeStrategy;
import net.bytebuddy.dynamic.scaffold.InstrumentedType;
import net.bytebuddy.dynamic.scaffold.MethodGraph;
import net.bytebuddy.dynamic.scaffold.MethodRegistry;
import net.bytebuddy.dynamic.scaffold.TypeValidation;
import net.bytebuddy.dynamic.scaffold.inline.DecoratingDynamicTypeBuilder;
import net.bytebuddy.dynamic.scaffold.inline.MethodNameTransformer;
import net.bytebuddy.dynamic.scaffold.inline.RebaseDynamicTypeBuilder;
import net.bytebuddy.dynamic.scaffold.inline.RedefinitionDynamicTypeBuilder;
import net.bytebuddy.dynamic.scaffold.subclass.ConstructorStrategy;
import net.bytebuddy.dynamic.scaffold.subclass.SubclassDynamicTypeBuilder;
import net.bytebuddy.implementation.Implementation;
import net.bytebuddy.implementation.MethodCall;
import net.bytebuddy.implementation.SuperMethodCall;
import net.bytebuddy.implementation.attribute.AnnotationRetention;
import net.bytebuddy.implementation.attribute.AnnotationValueFilter;
import net.bytebuddy.implementation.attribute.MethodAttributeAppender;
import net.bytebuddy.implementation.auxiliary.AuxiliaryType;
import net.bytebuddy.implementation.bytecode.ByteCodeAppender;
import net.bytebuddy.implementation.bytecode.Duplication;
import net.bytebuddy.implementation.bytecode.StackManipulation;
import net.bytebuddy.implementation.bytecode.TypeCreation;
import net.bytebuddy.implementation.bytecode.assign.Assigner;
import net.bytebuddy.implementation.bytecode.assign.TypeCasting;
import net.bytebuddy.implementation.bytecode.collection.ArrayFactory;
import net.bytebuddy.implementation.bytecode.constant.IntegerConstant;
import net.bytebuddy.implementation.bytecode.constant.TextConstant;
import net.bytebuddy.implementation.bytecode.member.FieldAccess;
import net.bytebuddy.implementation.bytecode.member.MethodInvocation;
import net.bytebuddy.implementation.bytecode.member.MethodReturn;
import net.bytebuddy.implementation.bytecode.member.MethodVariableAccess;
import net.bytebuddy.jar.asm.MethodVisitor;
import net.bytebuddy.jar.asm.Opcodes;
import net.bytebuddy.matcher.ElementMatcher;
import net.bytebuddy.matcher.LatentMatcher;
import net.bytebuddy.utility.AsmClassReader;
import net.bytebuddy.utility.AsmClassWriter;
import net.bytebuddy.utility.CompoundList;
import net.bytebuddy.utility.GraalImageCode;
import net.bytebuddy.utility.JavaConstant;
import net.bytebuddy.utility.JavaType;
import net.bytebuddy.utility.RandomString;
import net.bytebuddy.utility.nullability.MaybeNull;
import net.bytebuddy.utility.privilege.GetSystemPropertyAction;
import org.jspecify.annotations.NonNull;

import java.lang.annotation.Annotation;
import java.lang.annotation.ElementType;
import java.lang.reflect.Type;
import java.security.PrivilegedAction;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

import static net.bytebuddy.matcher.ElementMatchers.isConstructor;
import static net.bytebuddy.matcher.ElementMatchers.isDefaultFinalizer;
import static net.bytebuddy.matcher.ElementMatchers.isEquals;
import static net.bytebuddy.matcher.ElementMatchers.isHashCode;
import static net.bytebuddy.matcher.ElementMatchers.isSynthetic;
import static net.bytebuddy.matcher.ElementMatchers.isToString;
import static net.bytebuddy.matcher.ElementMatchers.named;
import static net.bytebuddy.matcher.ElementMatchers.takesArguments;
import static net.bytebuddy.matcher.ElementMatchers.takesGenericArguments;
import static net.bytebuddy.matcher.ElementMatchers.targetsElement;

/**
 * 复制{@link net.bytebuddy.ByteBuddy} 调整，便于适配高版本JDK
 *
 * @author livk
 * @see net.bytebuddy.ByteBuddy
 */
@HashCodeAndEqualsPlugin.Enhance
@SuppressWarnings("unused")
public class GenericsByteBuddy {

	public static final String DEFAULT_NAMING_PROPERTY = "net.bytebuddy.naming";

	public static final String DEFAULT_VALIDATION_PROPERTY = "net.bytebuddy.validation";

	private static final String BYTE_BUDDY_DEFAULT_PREFIX = "ByteBuddy";

	private static final String BYTE_BUDDY_DEFAULT_SUFFIX = "auxiliary";

	private static final String BYTE_BUDDY_DEFAULT_CONTEXT_NAME = "synthetic";

	private static final TypeValidation DEFAULT_TYPE_VALIDATION;

	@MaybeNull
	private static final NamingStrategy DEFAULT_NAMING_STRATEGY;

	@MaybeNull
	private static final AuxiliaryType.NamingStrategy DEFAULT_AUXILIARY_NAMING_STRATEGY;

	@MaybeNull
	private static final Implementation.Context.Factory DEFAULT_IMPLEMENTATION_CONTEXT_FACTORY;

	static {
		String validation;
		try {
			validation = doPrivileged(new GetSystemPropertyAction(DEFAULT_VALIDATION_PROPERTY));
		}
		catch (Throwable ignored) {
			validation = null;
		}
		DEFAULT_TYPE_VALIDATION = validation == null || Boolean.parseBoolean(validation) ? TypeValidation.ENABLED
				: TypeValidation.DISABLED;
		String naming;
		try {
			naming = doPrivileged(new GetSystemPropertyAction(DEFAULT_NAMING_PROPERTY));
		}
		catch (Throwable ignored) {
			naming = null;
		}
		NamingStrategy namingStrategy;
		AuxiliaryType.NamingStrategy auxiliaryNamingStrategy;
		Implementation.Context.Factory implementationContextFactory;
		if (naming == null) {
			if (GraalImageCode.getCurrent().isDefined()) {
				namingStrategy = new NamingStrategy.Suffixing(BYTE_BUDDY_DEFAULT_PREFIX,
						new NamingStrategy.Suffixing.BaseNameResolver.WithCallerSuffix(
								NamingStrategy.Suffixing.BaseNameResolver.ForUnnamedType.INSTANCE),
						NamingStrategy.BYTE_BUDDY_RENAME_PACKAGE);
				auxiliaryNamingStrategy = new AuxiliaryType.NamingStrategy.Suffixing(BYTE_BUDDY_DEFAULT_SUFFIX);
				implementationContextFactory = new Implementation.Context.Default.Factory.WithFixedSuffix(
						BYTE_BUDDY_DEFAULT_CONTEXT_NAME);
			}
			else {
				namingStrategy = null;
				auxiliaryNamingStrategy = null;
				implementationContextFactory = null;
			}
		}
		else if (naming.equalsIgnoreCase("fixed")) {
			namingStrategy = new NamingStrategy.Suffixing(BYTE_BUDDY_DEFAULT_PREFIX,
					NamingStrategy.Suffixing.BaseNameResolver.ForUnnamedType.INSTANCE,
					NamingStrategy.BYTE_BUDDY_RENAME_PACKAGE);
			auxiliaryNamingStrategy = new AuxiliaryType.NamingStrategy.Suffixing(BYTE_BUDDY_DEFAULT_SUFFIX);
			implementationContextFactory = new Implementation.Context.Default.Factory.WithFixedSuffix(
					BYTE_BUDDY_DEFAULT_CONTEXT_NAME);
		}
		else if (naming.equalsIgnoreCase("caller")) {
			namingStrategy = new NamingStrategy.Suffixing(BYTE_BUDDY_DEFAULT_PREFIX,
					new NamingStrategy.Suffixing.BaseNameResolver.WithCallerSuffix(
							NamingStrategy.Suffixing.BaseNameResolver.ForUnnamedType.INSTANCE),
					NamingStrategy.BYTE_BUDDY_RENAME_PACKAGE);
			auxiliaryNamingStrategy = new AuxiliaryType.NamingStrategy.Suffixing(BYTE_BUDDY_DEFAULT_SUFFIX);
			implementationContextFactory = new Implementation.Context.Default.Factory.WithFixedSuffix(
					BYTE_BUDDY_DEFAULT_CONTEXT_NAME);
		}
		else {
			long seed;
			try {
				seed = Long.parseLong(naming);
			}
			catch (Exception ignored) {
				throw new IllegalStateException(
						"'net.bytebuddy.naming' is set to an unknown, non-numeric value: " + naming);
			}
			ThreadLocalRandom current = ThreadLocalRandom.current();
			current.setSeed(seed);
			namingStrategy = new NamingStrategy.SuffixingRandom(BYTE_BUDDY_DEFAULT_PREFIX,
					NamingStrategy.Suffixing.BaseNameResolver.ForUnnamedType.INSTANCE,
					NamingStrategy.BYTE_BUDDY_RENAME_PACKAGE, new RandomString(RandomString.DEFAULT_LENGTH, current));
			auxiliaryNamingStrategy = new AuxiliaryType.NamingStrategy.Suffixing(BYTE_BUDDY_DEFAULT_SUFFIX);
			implementationContextFactory = new Implementation.Context.Default.Factory.WithFixedSuffix(
					BYTE_BUDDY_DEFAULT_CONTEXT_NAME);
		}
		DEFAULT_NAMING_STRATEGY = namingStrategy;
		DEFAULT_AUXILIARY_NAMING_STRATEGY = auxiliaryNamingStrategy;
		DEFAULT_IMPLEMENTATION_CONTEXT_FACTORY = implementationContextFactory;
	}

	@MaybeNull
	@AccessControllerPlugin.Enhance
	private static <T> T doPrivileged(PrivilegedAction<T> action) {
		return action.run();
	}

	protected final ClassFileVersion classFileVersion;

	protected final NamingStrategy namingStrategy;

	protected final AuxiliaryType.NamingStrategy auxiliaryTypeNamingStrategy;

	protected final AnnotationValueFilter.Factory annotationValueFilterFactory;

	protected final AnnotationRetention annotationRetention;

	protected final Implementation.Context.Factory implementationContextFactory;

	protected final MethodGraph.Compiler methodGraphCompiler;

	protected final InstrumentedType.Factory instrumentedTypeFactory;

	protected final LatentMatcher<? super MethodDescription> ignoredMethods;

	protected final TypeValidation typeValidation;

	protected final VisibilityBridgeStrategy visibilityBridgeStrategy;

	protected final AsmClassReader.Factory classReaderFactory;

	protected final AsmClassWriter.Factory classWriterFactory;

	public GenericsByteBuddy() {
		this(ClassFileVersion.ofThisVm(ClassFileVersion.JAVA_V21));
	}

	public GenericsByteBuddy(ClassFileVersion classFileVersion) {
		this(classFileVersion,
				DEFAULT_NAMING_STRATEGY == null ? new NamingStrategy.SuffixingRandom(BYTE_BUDDY_DEFAULT_PREFIX)
						: DEFAULT_NAMING_STRATEGY,
				DEFAULT_AUXILIARY_NAMING_STRATEGY == null
						? new AuxiliaryType.NamingStrategy.SuffixingRandom(BYTE_BUDDY_DEFAULT_SUFFIX)
						: DEFAULT_AUXILIARY_NAMING_STRATEGY,
				AnnotationValueFilter.Default.APPEND_DEFAULTS, AnnotationRetention.ENABLED,
				DEFAULT_IMPLEMENTATION_CONTEXT_FACTORY == null ? Implementation.Context.Default.Factory.INSTANCE
						: DEFAULT_IMPLEMENTATION_CONTEXT_FACTORY,
				MethodGraph.Compiler.DEFAULT, InstrumentedType.Factory.Default.MODIFIABLE, DEFAULT_TYPE_VALIDATION,
				VisibilityBridgeStrategy.Default.ALWAYS, AsmClassReader.Factory.Default.IMPLICIT,
				AsmClassWriter.Factory.Default.IMPLICIT,
				new LatentMatcher.Resolved<>(isSynthetic().or(isDefaultFinalizer())));
	}

	protected GenericsByteBuddy(ClassFileVersion classFileVersion, NamingStrategy namingStrategy,
			AuxiliaryType.NamingStrategy auxiliaryTypeNamingStrategy,
			AnnotationValueFilter.Factory annotationValueFilterFactory, AnnotationRetention annotationRetention,
			Implementation.Context.Factory implementationContextFactory, MethodGraph.Compiler methodGraphCompiler,
			InstrumentedType.Factory instrumentedTypeFactory, TypeValidation typeValidation,
			VisibilityBridgeStrategy visibilityBridgeStrategy, AsmClassReader.Factory classReaderFactory,
			AsmClassWriter.Factory classWriterFactory, LatentMatcher<? super MethodDescription> ignoredMethods) {
		this.classFileVersion = classFileVersion;
		this.namingStrategy = namingStrategy;
		this.auxiliaryTypeNamingStrategy = auxiliaryTypeNamingStrategy;
		this.annotationValueFilterFactory = annotationValueFilterFactory;
		this.annotationRetention = annotationRetention;
		this.implementationContextFactory = implementationContextFactory;
		this.methodGraphCompiler = methodGraphCompiler;
		this.instrumentedTypeFactory = instrumentedTypeFactory;
		this.typeValidation = typeValidation;
		this.visibilityBridgeStrategy = visibilityBridgeStrategy;
		this.classReaderFactory = classReaderFactory;
		this.classWriterFactory = classWriterFactory;
		this.ignoredMethods = ignoredMethods;
	}

	public <T> DynamicType.Builder<T> subclass(Class<T> superType) {
		return subclass(TypeDescription.ForLoadedType.of(superType));
	}

	public <T> DynamicType.Builder<T> subclass(Class<T> superType, ConstructorStrategy constructorStrategy) {
		return subclass(TypeDescription.ForLoadedType.of(superType), constructorStrategy);
	}

	public <T> DynamicType.Builder<T> subclass(Type superType) {
		return subclass(TypeDefinition.Sort.describe(superType));
	}

	public <T> DynamicType.Builder<T> subclass(Type superType, ConstructorStrategy constructorStrategy) {
		return subclass(TypeDefinition.Sort.describe(superType), constructorStrategy);
	}

	public <T> DynamicType.Builder<T> subclass(TypeDefinition superType) {
		return subclass(superType, ConstructorStrategy.Default.IMITATE_SUPER_CLASS_OPENING);
	}

	public <T> DynamicType.Builder<T> subclass(TypeDefinition superType, ConstructorStrategy constructorStrategy) {
		TypeDescription.Generic actualSuperType;
		TypeList.Generic interfaceTypes;
		if (superType.isPrimitive() || superType.isArray() || superType.isFinal()) {
			throw new IllegalArgumentException("Cannot subclass primitive, array or final types: " + superType);
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

	public <T> DynamicType.Builder<T> makeInterface() {
		return makeInterface(Collections.<TypeDescription>emptyList());
	}

	public <T> DynamicType.Builder<T> makeInterface(Class<T> interfaceType) {
		return makeInterface(Collections.<Type>singletonList(interfaceType));
	}

	public <T> DynamicType.Builder<T> makeInterface(Type... interfaceType) {
		return makeInterface(Arrays.asList(interfaceType));
	}

	public <T> DynamicType.Builder<T> makeInterface(List<? extends Type> interfaceTypes) {
		return makeInterface(new TypeList.Generic.ForLoadedTypes(interfaceTypes));
	}

	public <T> DynamicType.Builder<T> makeInterface(TypeDefinition... interfaceType) {
		return makeInterface(Arrays.asList(interfaceType));
	}

	public <T> DynamicType.Builder<T> makeInterface(Collection<? extends TypeDefinition> interfaceTypes) {
		return this.<T>subclass(Object.class, ConstructorStrategy.Default.NO_CONSTRUCTORS)
			.implement(interfaceTypes)
			.modifiers(TypeManifestation.INTERFACE, Visibility.PUBLIC);
	}

	public <T> DynamicType.Builder<T> makePackage(String name) {
		return new SubclassDynamicTypeBuilder<>(
				instrumentedTypeFactory.subclass(name + "." + PackageDescription.PACKAGE_CLASS_NAME,
						PackageDescription.PACKAGE_MODIFIERS,
						TypeDescription.Generic.OfNonGenericType.ForLoadedType.of(Object.class)),
				classFileVersion, auxiliaryTypeNamingStrategy, annotationValueFilterFactory, annotationRetention,
				implementationContextFactory, methodGraphCompiler, typeValidation, visibilityBridgeStrategy,
				classReaderFactory, classWriterFactory, ignoredMethods, ConstructorStrategy.Default.NO_CONSTRUCTORS);
	}

	public <T> DynamicType.Builder<T> makeRecord() {
		TypeDescription.Generic record = InstrumentedType.Default
			.of(JavaType.RECORD.getTypeStub().getName(),
					TypeDescription.Generic.OfNonGenericType.ForLoadedType.of(Object.class), Visibility.PUBLIC)
			.withMethod(new MethodDescription.Token(Opcodes.ACC_PROTECTED))
			.withMethod(new MethodDescription.Token("hashCode", Opcodes.ACC_PUBLIC | Opcodes.ACC_ABSTRACT,
					TypeDescription.ForLoadedType.of(int.class).asGenericType()))
			.withMethod(new MethodDescription.Token("equals", Opcodes.ACC_PUBLIC | Opcodes.ACC_ABSTRACT,
					TypeDescription.ForLoadedType.of(boolean.class).asGenericType(),
					Collections.singletonList(TypeDescription.Generic.OfNonGenericType.ForLoadedType.of(Object.class))))
			.withMethod(new MethodDescription.Token("toString", Opcodes.ACC_PUBLIC | Opcodes.ACC_ABSTRACT,
					TypeDescription.ForLoadedType.of(String.class).asGenericType()))
			.asGenericType();
		return new SubclassDynamicTypeBuilder<T>(
				instrumentedTypeFactory
					.subclass(namingStrategy.subclass(record), Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, record)
					.withRecord(true),
				classFileVersion, auxiliaryTypeNamingStrategy, annotationValueFilterFactory, annotationRetention,
				implementationContextFactory, methodGraphCompiler, typeValidation, visibilityBridgeStrategy,
				classReaderFactory, classWriterFactory, ignoredMethods, RecordConstructorStrategy.INSTANCE)
			.method(isHashCode())
			.intercept(RecordObjectMethod.HASH_CODE)
			.method(isEquals())
			.intercept(RecordObjectMethod.EQUALS)
			.method(isToString())
			.intercept(RecordObjectMethod.TO_STRING);
	}

	public <T extends Annotation> DynamicType.Builder<T> makeAnnotation() {
		return new SubclassDynamicTypeBuilder<>(
				instrumentedTypeFactory
					.subclass(
							namingStrategy
								.subclass(TypeDescription.Generic.OfNonGenericType.ForLoadedType.of(Annotation.class)),
							ModifierContributor.Resolver.of(Visibility.PUBLIC, TypeManifestation.ANNOTATION).resolve(),
							TypeDescription.Generic.OfNonGenericType.ForLoadedType.of(Object.class))
					.withInterfaces(new TypeList.Generic.Explicit(
							TypeDescription.Generic.OfNonGenericType.ForLoadedType.of(Annotation.class))),
				classFileVersion, auxiliaryTypeNamingStrategy, annotationValueFilterFactory, annotationRetention,
				implementationContextFactory, methodGraphCompiler, typeValidation, visibilityBridgeStrategy,
				classReaderFactory, classWriterFactory, ignoredMethods, ConstructorStrategy.Default.NO_CONSTRUCTORS);
	}

	public <T extends Enum<T>> DynamicType.Builder<T> makeEnumeration(String... value) {
		return makeEnumeration(Arrays.asList(value));
	}

	public <T extends Enum<T>> DynamicType.Builder<T> makeEnumeration(Collection<? extends String> values) {
		if (values.isEmpty()) {
			throw new IllegalArgumentException("Require at least one enumeration constant");
		}
		TypeDescription.Generic enumType = TypeDescription.Generic.Builder
			.parameterizedType(Enum.class, TargetType.class)
			.build();
		return new SubclassDynamicTypeBuilder<T>(
				instrumentedTypeFactory.subclass(namingStrategy.subclass(enumType),
						ModifierContributor.Resolver
							.of(Visibility.PUBLIC, TypeManifestation.FINAL, EnumerationState.ENUMERATION)
							.resolve(),
						enumType),
				classFileVersion, auxiliaryTypeNamingStrategy, annotationValueFilterFactory, annotationRetention,
				implementationContextFactory, methodGraphCompiler, typeValidation, visibilityBridgeStrategy,
				classReaderFactory, classWriterFactory, ignoredMethods, ConstructorStrategy.Default.NO_CONSTRUCTORS)
			.defineConstructor(Visibility.PRIVATE)
			.withParameters(String.class, int.class)
			.intercept(SuperMethodCall.INSTANCE)
			.defineMethod(EnumerationImplementation.ENUM_VALUE_OF_METHOD_NAME, TargetType.class, Visibility.PUBLIC,
					Ownership.STATIC)
			.withParameters(String.class)
			.intercept(MethodCall
				.invoke(enumType.getDeclaredMethods()
					.filter(named(EnumerationImplementation.ENUM_VALUE_OF_METHOD_NAME)
						.and(takesArguments(Class.class, String.class)))
					.getOnly())
				.withOwnType()
				.withArgument(0)
				.withAssigner(Assigner.DEFAULT, Assigner.Typing.DYNAMIC))
			.defineMethod(EnumerationImplementation.ENUM_VALUES_METHOD_NAME, TargetType[].class, Visibility.PUBLIC,
					Ownership.STATIC)
			.intercept(new EnumerationImplementation(new ArrayList<>(values)));
	}

	public <T> DynamicType.Builder<T> redefine(Class<T> type) {
		return redefine(type, ClassFileLocator.ForClassLoader.of(type.getClassLoader()));
	}

	public <T> DynamicType.Builder<T> redefine(Class<T> type, ClassFileLocator classFileLocator) {
		return redefine(TypeDescription.ForLoadedType.of(type), classFileLocator);
	}

	public <T> DynamicType.Builder<T> redefine(TypeDescription type, ClassFileLocator classFileLocator) {
		if (type.isArray() || type.isPrimitive()) {
			throw new IllegalArgumentException("Cannot redefine array or primitive type: " + type);
		}
		return new RedefinitionDynamicTypeBuilder<>(instrumentedTypeFactory.represent(type), classFileVersion,
				auxiliaryTypeNamingStrategy, annotationValueFilterFactory, annotationRetention,
				implementationContextFactory, methodGraphCompiler, typeValidation, visibilityBridgeStrategy,
				classReaderFactory, classWriterFactory, ignoredMethods, type, classFileLocator);
	}

	public <T> DynamicType.Builder<T> rebase(Class<T> type) {
		return rebase(type, ClassFileLocator.ForClassLoader.of(type.getClassLoader()));
	}

	public <T> DynamicType.Builder<T> rebase(Class<T> type, ClassFileLocator classFileLocator) {
		return rebase(TypeDescription.ForLoadedType.of(type), classFileLocator);
	}

	public <T> DynamicType.Builder<T> rebase(Class<T> type, ClassFileLocator classFileLocator,
			MethodNameTransformer methodNameTransformer) {
		return rebase(TypeDescription.ForLoadedType.of(type), classFileLocator, methodNameTransformer);
	}

	public <T> DynamicType.Builder<T> rebase(TypeDescription type, ClassFileLocator classFileLocator) {
		return rebase(type, classFileLocator, MethodNameTransformer.Suffixing.withRandomSuffix());
	}

	public <T> DynamicType.Builder<T> rebase(TypeDescription type, ClassFileLocator classFileLocator,
			MethodNameTransformer methodNameTransformer) {
		if (type.isArray() || type.isPrimitive()) {
			throw new IllegalArgumentException("Cannot rebase array or primitive type: " + type);
		}
		return new RebaseDynamicTypeBuilder<>(instrumentedTypeFactory.represent(type), classFileVersion,
				auxiliaryTypeNamingStrategy, annotationValueFilterFactory, annotationRetention,
				implementationContextFactory, methodGraphCompiler, typeValidation, visibilityBridgeStrategy,
				classReaderFactory, classWriterFactory, ignoredMethods, type, classFileLocator, methodNameTransformer);
	}

	public <T> DynamicType.Builder<T> rebase(Package aPackage, ClassFileLocator classFileLocator) {
		return rebase(new PackageDescription.ForLoadedPackage(aPackage), classFileLocator);
	}

	public <T> DynamicType.Builder<T> rebase(PackageDescription aPackage, ClassFileLocator classFileLocator) {
		return rebase(new TypeDescription.ForPackageDescription(aPackage), classFileLocator);
	}

	public <T> DynamicType.Builder<T> decorate(Class<T> type) {
		return decorate(type, ClassFileLocator.ForClassLoader.of(type.getClassLoader()));
	}

	public <T> DynamicType.Builder<T> decorate(Class<T> type, ClassFileLocator classFileLocator) {
		return decorate(TypeDescription.ForLoadedType.of(type), classFileLocator);
	}

	public <T> DynamicType.Builder<T> decorate(TypeDescription type, ClassFileLocator classFileLocator) {
		if (type.isArray() || type.isPrimitive()) {
			throw new IllegalArgumentException("Cannot decorate array or primitive type: " + type);
		}
		return new DecoratingDynamicTypeBuilder<>(type, classFileVersion, auxiliaryTypeNamingStrategy,
				annotationValueFilterFactory, annotationRetention, implementationContextFactory, methodGraphCompiler,
				typeValidation, classReaderFactory, classWriterFactory, ignoredMethods, classFileLocator);
	}

	public GenericsByteBuddy with(ClassFileVersion classFileVersion) {
		return new GenericsByteBuddy(classFileVersion, namingStrategy, auxiliaryTypeNamingStrategy,
				annotationValueFilterFactory, annotationRetention, implementationContextFactory, methodGraphCompiler,
				instrumentedTypeFactory, typeValidation, visibilityBridgeStrategy, classReaderFactory,
				classWriterFactory, ignoredMethods);
	}

	public GenericsByteBuddy with(NamingStrategy namingStrategy) {
		return new GenericsByteBuddy(classFileVersion, namingStrategy, auxiliaryTypeNamingStrategy,
				annotationValueFilterFactory, annotationRetention, implementationContextFactory, methodGraphCompiler,
				instrumentedTypeFactory, typeValidation, visibilityBridgeStrategy, classReaderFactory,
				classWriterFactory, ignoredMethods);
	}

	public GenericsByteBuddy with(AuxiliaryType.NamingStrategy auxiliaryTypeNamingStrategy) {
		return new GenericsByteBuddy(classFileVersion, namingStrategy, auxiliaryTypeNamingStrategy,
				annotationValueFilterFactory, annotationRetention, implementationContextFactory, methodGraphCompiler,
				instrumentedTypeFactory, typeValidation, visibilityBridgeStrategy, classReaderFactory,
				classWriterFactory, ignoredMethods);
	}

	public GenericsByteBuddy with(AnnotationValueFilter.Factory annotationValueFilterFactory) {
		return new GenericsByteBuddy(classFileVersion, namingStrategy, auxiliaryTypeNamingStrategy,
				annotationValueFilterFactory, annotationRetention, implementationContextFactory, methodGraphCompiler,
				instrumentedTypeFactory, typeValidation, visibilityBridgeStrategy, classReaderFactory,
				classWriterFactory, ignoredMethods);
	}

	public GenericsByteBuddy with(AnnotationRetention annotationRetention) {
		return new GenericsByteBuddy(classFileVersion, namingStrategy, auxiliaryTypeNamingStrategy,
				annotationValueFilterFactory, annotationRetention, implementationContextFactory, methodGraphCompiler,
				instrumentedTypeFactory, typeValidation, visibilityBridgeStrategy, classReaderFactory,
				classWriterFactory, ignoredMethods);
	}

	public GenericsByteBuddy with(Implementation.Context.Factory implementationContextFactory) {
		return new GenericsByteBuddy(classFileVersion, namingStrategy, auxiliaryTypeNamingStrategy,
				annotationValueFilterFactory, annotationRetention, implementationContextFactory, methodGraphCompiler,
				instrumentedTypeFactory, typeValidation, visibilityBridgeStrategy, classReaderFactory,
				classWriterFactory, ignoredMethods);
	}

	public GenericsByteBuddy with(MethodGraph.Compiler methodGraphCompiler) {
		return new GenericsByteBuddy(classFileVersion, namingStrategy, auxiliaryTypeNamingStrategy,
				annotationValueFilterFactory, annotationRetention, implementationContextFactory, methodGraphCompiler,
				instrumentedTypeFactory, typeValidation, visibilityBridgeStrategy, classReaderFactory,
				classWriterFactory, ignoredMethods);
	}

	public GenericsByteBuddy with(InstrumentedType.Factory instrumentedTypeFactory) {
		return new GenericsByteBuddy(classFileVersion, namingStrategy, auxiliaryTypeNamingStrategy,
				annotationValueFilterFactory, annotationRetention, implementationContextFactory, methodGraphCompiler,
				instrumentedTypeFactory, typeValidation, visibilityBridgeStrategy, classReaderFactory,
				classWriterFactory, ignoredMethods);
	}

	public GenericsByteBuddy with(TypeValidation typeValidation) {
		return new GenericsByteBuddy(classFileVersion, namingStrategy, auxiliaryTypeNamingStrategy,
				annotationValueFilterFactory, annotationRetention, implementationContextFactory, methodGraphCompiler,
				instrumentedTypeFactory, typeValidation, visibilityBridgeStrategy, classReaderFactory,
				classWriterFactory, ignoredMethods);
	}

	public GenericsByteBuddy with(VisibilityBridgeStrategy visibilityBridgeStrategy) {
		return new GenericsByteBuddy(classFileVersion, namingStrategy, auxiliaryTypeNamingStrategy,
				annotationValueFilterFactory, annotationRetention, implementationContextFactory, methodGraphCompiler,
				instrumentedTypeFactory, typeValidation, visibilityBridgeStrategy, classReaderFactory,
				classWriterFactory, ignoredMethods);
	}

	public GenericsByteBuddy with(AsmClassReader.Factory classReaderFactory) {
		return new GenericsByteBuddy(classFileVersion, namingStrategy, auxiliaryTypeNamingStrategy,
				annotationValueFilterFactory, annotationRetention, implementationContextFactory, methodGraphCompiler,
				instrumentedTypeFactory, typeValidation, visibilityBridgeStrategy, classReaderFactory,
				classWriterFactory, ignoredMethods);
	}

	public GenericsByteBuddy with(AsmClassWriter.Factory classWriterFactory) {
		return new GenericsByteBuddy(classFileVersion, namingStrategy, auxiliaryTypeNamingStrategy,
				annotationValueFilterFactory, annotationRetention, implementationContextFactory, methodGraphCompiler,
				instrumentedTypeFactory, typeValidation, visibilityBridgeStrategy, classReaderFactory,
				classWriterFactory, ignoredMethods);
	}

	public GenericsByteBuddy withIgnoredClassReader() {
		if (classWriterFactory instanceof AsmClassWriter.Factory.Suppressing) {
			return this;
		}
		return new GenericsByteBuddy(classFileVersion, namingStrategy, auxiliaryTypeNamingStrategy,
				annotationValueFilterFactory, annotationRetention, implementationContextFactory, methodGraphCompiler,
				instrumentedTypeFactory, typeValidation, visibilityBridgeStrategy, classReaderFactory,
				new AsmClassWriter.Factory.Suppressing(classWriterFactory), ignoredMethods);
	}

	@SuppressWarnings("overloads")
	public GenericsByteBuddy ignore(ElementMatcher<? super MethodDescription> ignoredMethods) {
		return ignore(new LatentMatcher.Resolved<>(ignoredMethods));
	}

	@SuppressWarnings("overloads")
	public GenericsByteBuddy ignore(LatentMatcher<? super MethodDescription> ignoredMethods) {
		return new GenericsByteBuddy(classFileVersion, namingStrategy, auxiliaryTypeNamingStrategy,
				annotationValueFilterFactory, annotationRetention, implementationContextFactory, methodGraphCompiler,
				instrumentedTypeFactory, typeValidation, visibilityBridgeStrategy, classReaderFactory,
				classWriterFactory, ignoredMethods);
	}

	@HashCodeAndEqualsPlugin.Enhance
	protected static class EnumerationImplementation implements Implementation {

		protected static final String CLONE_METHOD_NAME = "clone";

		protected static final String ENUM_VALUE_OF_METHOD_NAME = "valueOf";

		protected static final String ENUM_VALUES_METHOD_NAME = "values";

		private static final int ENUM_FIELD_MODIFIERS = Opcodes.ACC_FINAL | Opcodes.ACC_STATIC | Opcodes.ACC_PUBLIC;

		private static final String ENUM_VALUES = "$VALUES";

		private final List<String> values;

		protected EnumerationImplementation(List<String> values) {
			this.values = values;
		}

		@NonNull public InstrumentedType prepare(@NonNull InstrumentedType instrumentedType) {
			for (String value : values) {
				instrumentedType = instrumentedType.withField(new FieldDescription.Token(value,
						ENUM_FIELD_MODIFIERS | Opcodes.ACC_ENUM, TargetType.DESCRIPTION.asGenericType()));
			}
			return instrumentedType
				.withField(new FieldDescription.Token(ENUM_VALUES, ENUM_FIELD_MODIFIERS | Opcodes.ACC_SYNTHETIC,
						TypeDescription.ArrayProjection.of(TargetType.DESCRIPTION).asGenericType()))
				.withInitializer(new InitializationAppender(values));
		}

		@NonNull public ByteCodeAppender appender(Target implementationTarget) {
			return new ValuesMethodAppender(implementationTarget.getInstrumentedType());
		}

		@HashCodeAndEqualsPlugin.Enhance
		protected static class ValuesMethodAppender implements ByteCodeAppender {

			private final TypeDescription instrumentedType;

			protected ValuesMethodAppender(TypeDescription instrumentedType) {
				this.instrumentedType = instrumentedType;
			}

			@NonNull public Size apply(@NonNull MethodVisitor methodVisitor, @NonNull Context implementationContext,
					@NonNull MethodDescription instrumentedMethod) {
				FieldDescription valuesField = instrumentedType.getDeclaredFields()
					.filter(named(ENUM_VALUES))
					.getOnly();
				MethodDescription cloneMethod = TypeDescription.Generic.OfNonGenericType.ForLoadedType.of(Object.class)
					.getDeclaredMethods()
					.filter(named(CLONE_METHOD_NAME))
					.getOnly();
				return new Size(new StackManipulation.Compound(FieldAccess.forField(valuesField).read(),
						MethodInvocation.invoke(cloneMethod).virtual(valuesField.getType().asErasure()),
						TypeCasting.to(valuesField.getType().asErasure()), MethodReturn.REFERENCE)
					.apply(methodVisitor, implementationContext)
					.getMaximalSize(), instrumentedMethod.getStackSize());
			}

		}

		@HashCodeAndEqualsPlugin.Enhance
		protected static class InitializationAppender implements ByteCodeAppender {

			private final List<String> values;

			protected InitializationAppender(List<String> values) {
				this.values = values;
			}

			@NonNull public Size apply(@NonNull MethodVisitor methodVisitor, @NonNull Context implementationContext,
					MethodDescription instrumentedMethod) {
				TypeDescription instrumentedType = instrumentedMethod.getDeclaringType().asErasure();
				MethodDescription enumConstructor = instrumentedType.getDeclaredMethods()
					.filter(isConstructor().and(takesArguments(String.class, int.class)))
					.getOnly();
				int ordinal = 0;
				StackManipulation stackManipulation = StackManipulation.Trivial.INSTANCE;
				List<FieldDescription> enumerationFields = new ArrayList<>(values.size());
				for (String value : values) {
					FieldDescription fieldDescription = instrumentedType.getDeclaredFields()
						.filter(named(value))
						.getOnly();
					stackManipulation = new StackManipulation.Compound(stackManipulation,
							TypeCreation.of(instrumentedType), Duplication.SINGLE, new TextConstant(value),
							IntegerConstant.forValue(ordinal++), MethodInvocation.invoke(enumConstructor),
							FieldAccess.forField(fieldDescription).write());
					enumerationFields.add(fieldDescription);
				}
				List<StackManipulation> fieldGetters = new ArrayList<>(values.size());
				for (FieldDescription fieldDescription : enumerationFields) {
					fieldGetters.add(FieldAccess.forField(fieldDescription).read());
				}
				stackManipulation = new StackManipulation.Compound(stackManipulation,
						ArrayFactory.forType(instrumentedType.asGenericType()).withValues(fieldGetters),
						FieldAccess.forField(instrumentedType.getDeclaredFields().filter(named(ENUM_VALUES)).getOnly())
							.write());
				return new Size(stackManipulation.apply(methodVisitor, implementationContext).getMaximalSize(),
						instrumentedMethod.getStackSize());
			}

		}

	}

	@HashCodeAndEqualsPlugin.Enhance
	protected enum RecordConstructorStrategy implements ConstructorStrategy, Implementation {

		INSTANCE;

		@NonNull public List<MethodDescription.Token> extractConstructors(TypeDescription instrumentedType) {
			List<ParameterDescription.Token> tokens = new ArrayList<>(instrumentedType.getRecordComponents().size());
			for (RecordComponentDescription.InDefinedShape recordComponent : instrumentedType.getRecordComponents()) {
				tokens.add(new ParameterDescription.Token(recordComponent.getType(),
						recordComponent.getDeclaredAnnotations().filter(targetsElement(ElementType.CONSTRUCTOR)),
						recordComponent.getActualName(), ModifierContributor.EMPTY_MASK));
			}
			return Collections.singletonList(new MethodDescription.Token(MethodDescription.CONSTRUCTOR_INTERNAL_NAME,
					Opcodes.ACC_PUBLIC, Collections.emptyList(),
					TypeDescription.Generic.OfNonGenericType.ForLoadedType.of(void.class), tokens,
					Collections.emptyList(), Collections.emptyList(), AnnotationValue.UNDEFINED,
					TypeDescription.Generic.UNDEFINED));
		}

		@NonNull public MethodRegistry inject(TypeDescription instrumentedType, MethodRegistry methodRegistry) {
			return methodRegistry.prepend(
					new LatentMatcher.Resolved<MethodDescription>(isConstructor()
						.and(takesGenericArguments(instrumentedType.getRecordComponents().asTypeList()))),
					new MethodRegistry.Handler.ForImplementation(this),
					MethodAttributeAppender.ForInstrumentedMethod.EXCLUDING_RECEIVER,
					Transformer.ForMethod.NoOp.<MethodDescription>make());
		}

		@NonNull public ByteCodeAppender appender(Target implementationTarget) {
			return new Appender(implementationTarget.getInstrumentedType());
		}

		@NonNull public InstrumentedType prepare(InstrumentedType instrumentedType) {
			for (RecordComponentDescription.InDefinedShape recordComponent : instrumentedType.getRecordComponents()) {
				instrumentedType = instrumentedType
					.withField(new FieldDescription.Token(recordComponent.getActualName(),
							Opcodes.ACC_PRIVATE | Opcodes.ACC_FINAL, recordComponent.getType(),
							recordComponent.getDeclaredAnnotations().filter(targetsElement(ElementType.FIELD))))
					.withMethod(new MethodDescription.Token(recordComponent.getActualName(), Opcodes.ACC_PUBLIC,
							Collections.emptyList(), recordComponent.getType(), Collections.emptyList(),
							Collections.emptyList(),
							recordComponent.getDeclaredAnnotations().filter(targetsElement(ElementType.METHOD)),
							AnnotationValue.UNDEFINED, TypeDescription.Generic.UNDEFINED));
			}
			return instrumentedType;
		}

		@HashCodeAndEqualsPlugin.Enhance
		protected static class Appender implements ByteCodeAppender {

			private final TypeDescription instrumentedType;

			protected Appender(TypeDescription instrumentedType) {
				this.instrumentedType = instrumentedType;
			}

			@NonNull public Size apply(@NonNull MethodVisitor methodVisitor, @NonNull Context implementationContext,
					MethodDescription instrumentedMethod) {
				if (instrumentedMethod.isMethod()) {
					return new Simple(MethodVariableAccess.loadThis(), FieldAccess.forField(
							instrumentedType.getDeclaredFields().filter(named(instrumentedMethod.getName())).getOnly())
						.read(), MethodReturn.of(instrumentedMethod.getReturnType()))
						.apply(methodVisitor, implementationContext, instrumentedMethod);
				}
				else {
					List<StackManipulation> stackManipulations = new ArrayList<>(
							instrumentedType.getRecordComponents().size() * 3 + 2);
					stackManipulations.add(MethodVariableAccess.loadThis());
					stackManipulations
						.add(MethodInvocation.invoke(new MethodDescription.Latent(JavaType.RECORD.getTypeStub(),
								new MethodDescription.Token(Opcodes.ACC_PUBLIC))));
					int offset = 1;
					for (RecordComponentDescription.InDefinedShape recordComponent : instrumentedType
						.getRecordComponents()) {
						stackManipulations.add(MethodVariableAccess.loadThis());
						stackManipulations.add(MethodVariableAccess.of(recordComponent.getType()).loadFrom(offset));
						stackManipulations.add(
								FieldAccess
									.forField(instrumentedType.getDeclaredFields()
										.filter(named(recordComponent.getActualName()))
										.getOnly())
									.write());
						offset += recordComponent.getType().getStackSize().getSize();
					}
					stackManipulations.add(MethodReturn.VOID);
					return new Simple(stackManipulations).apply(methodVisitor, implementationContext,
							instrumentedMethod);
				}
			}

		}

	}

	@HashCodeAndEqualsPlugin.Enhance
	protected enum RecordObjectMethod implements Implementation {

		HASH_CODE("hashCode", StackManipulation.Trivial.INSTANCE, int.class),

		EQUALS("equals", MethodVariableAccess.REFERENCE.loadFrom(1), boolean.class, Object.class),

		TO_STRING("toString", StackManipulation.Trivial.INSTANCE, String.class);

		private final String name;

		private final StackManipulation stackManipulation;

		private final TypeDescription returnType;

		private final List<? extends TypeDescription> arguments;

		RecordObjectMethod(String name, StackManipulation stackManipulation, Class<?> returnType,
				Class<?>... arguments) {
			this.name = name;
			this.stackManipulation = stackManipulation;
			this.returnType = TypeDescription.ForLoadedType.of(returnType);
			this.arguments = new TypeList.ForLoadedTypes(arguments);
		}

		@NonNull public ByteCodeAppender appender(Target implementationTarget) {
			StringBuilder stringBuilder = new StringBuilder();
			List<JavaConstant> methodHandles = new ArrayList<>(
					implementationTarget.getInstrumentedType().getRecordComponents().size());
			for (RecordComponentDescription.InDefinedShape recordComponent : implementationTarget.getInstrumentedType()
				.getRecordComponents()) {
				if (!stringBuilder.isEmpty()) {
					stringBuilder.append(";");
				}
				stringBuilder.append(recordComponent.getActualName());
				methodHandles.add(JavaConstant.MethodHandle.ofGetter(implementationTarget.getInstrumentedType()
					.getDeclaredFields()
					.filter(named(recordComponent.getActualName()))
					.getOnly()));
			}
			return new ByteCodeAppender.Simple(MethodVariableAccess.loadThis(), stackManipulation, MethodInvocation
				.invoke(new MethodDescription.Latent(JavaType.OBJECT_METHODS.getTypeStub(),
						new MethodDescription.Token("bootstrap", Opcodes.ACC_PUBLIC | Opcodes.ACC_STATIC,
								TypeDescription.Generic.OfNonGenericType.ForLoadedType.of(Object.class),
								Arrays.asList(JavaType.METHOD_HANDLES_LOOKUP.getTypeStub().asGenericType(),
										TypeDescription.ForLoadedType.of(String.class).asGenericType(),
										JavaType.TYPE_DESCRIPTOR.getTypeStub().asGenericType(),
										TypeDescription.ForLoadedType.of(Class.class).asGenericType(),
										TypeDescription.ForLoadedType.of(String.class).asGenericType(),
										TypeDescription.ArrayProjection.of(JavaType.METHOD_HANDLE.getTypeStub())
											.asGenericType()))))
				.dynamic(name, returnType, CompoundList.of(implementationTarget.getInstrumentedType(), arguments),
						CompoundList
							.of(Arrays.asList(JavaConstant.Simple.of(implementationTarget.getInstrumentedType()),
									JavaConstant.Simple.ofLoaded(stringBuilder.toString())), methodHandles)),
					MethodReturn.of(returnType));
		}

		@NonNull public InstrumentedType prepare(@NonNull InstrumentedType instrumentedType) {
			return instrumentedType;
		}

	}

	/**
	 * 替换{@link NamingStrategy.Suffixing.BaseNameResolver.WithCallerSuffix}
	 */
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
