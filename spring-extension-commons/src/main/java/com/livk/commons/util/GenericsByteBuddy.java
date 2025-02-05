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
import net.bytebuddy.build.AccessControllerPlugin.Enhance;
import net.bytebuddy.build.HashCodeAndEqualsPlugin;
import net.bytebuddy.description.annotation.AnnotationValue;
import net.bytebuddy.description.field.FieldDescription;
import net.bytebuddy.description.field.FieldList;
import net.bytebuddy.description.method.MethodDescription;
import net.bytebuddy.description.method.MethodList;
import net.bytebuddy.description.method.ParameterDescription;
import net.bytebuddy.description.modifier.*;
import net.bytebuddy.description.type.*;
import net.bytebuddy.dynamic.*;
import net.bytebuddy.dynamic.scaffold.*;
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
import net.bytebuddy.matcher.ElementMatcher;
import net.bytebuddy.matcher.ElementMatchers;
import net.bytebuddy.matcher.LatentMatcher;
import net.bytebuddy.utility.*;
import net.bytebuddy.utility.AsmClassReader.Factory;
import net.bytebuddy.utility.nullability.MaybeNull;
import net.bytebuddy.utility.privilege.GetSystemPropertyAction;
import org.springframework.lang.NonNull;

import java.lang.annotation.Annotation;
import java.lang.annotation.ElementType;
import java.lang.reflect.Type;
import java.security.PrivilegedAction;
import java.util.*;

import static net.bytebuddy.description.type.TypeDescription.*;
import static net.bytebuddy.dynamic.scaffold.subclass.ConstructorStrategy.Default;

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

	private static final TypeValidation DEFAULT_TYPE_VALIDATION;

	@MaybeNull
	private static final NamingStrategy DEFAULT_NAMING_STRATEGY;

	@MaybeNull
	private static final AuxiliaryType.NamingStrategy DEFAULT_AUXILIARY_NAMING_STRATEGY;

	@MaybeNull
	private static final Implementation.Context.Factory DEFAULT_IMPLEMENTATION_CONTEXT_FACTORY;

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

	@MaybeNull
	@Enhance
	private static <T> T doPrivileged(PrivilegedAction<T> action) {
		return action.run();
	}

	public GenericsByteBuddy() {
		this(ClassFileVersion.ofThisVm(ClassFileVersion.JAVA_V5));
	}

	public GenericsByteBuddy(ClassFileVersion classFileVersion) {
		this(classFileVersion,
				DEFAULT_NAMING_STRATEGY == null ? new NamingStrategy.SuffixingRandom("ByteBuddy")
						: DEFAULT_NAMING_STRATEGY,
				DEFAULT_AUXILIARY_NAMING_STRATEGY == null
						? new AuxiliaryType.NamingStrategy.SuffixingRandom("auxiliary")
						: DEFAULT_AUXILIARY_NAMING_STRATEGY,
				AnnotationValueFilter.Default.APPEND_DEFAULTS, AnnotationRetention.ENABLED,
				DEFAULT_IMPLEMENTATION_CONTEXT_FACTORY == null ? Implementation.Context.Default.Factory.INSTANCE
						: DEFAULT_IMPLEMENTATION_CONTEXT_FACTORY,
				MethodGraph.Compiler.DEFAULT, InstrumentedType.Factory.Default.MODIFIABLE, DEFAULT_TYPE_VALIDATION,
				VisibilityBridgeStrategy.Default.ALWAYS, Factory.Default.IMPLICIT,
				AsmClassWriter.Factory.Default.IMPLICIT,
				new LatentMatcher.Resolved<>(ElementMatchers.isSynthetic().or(ElementMatchers.isDefaultFinalizer())));
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
		return this.subclass(ForLoadedType.of(superType));
	}

	public <T> DynamicType.Builder<T> subclass(Class<T> superType, ConstructorStrategy constructorStrategy) {
		return this.subclass(ForLoadedType.of(superType), constructorStrategy);
	}

	public <T> DynamicType.Builder<T> subclass(Type superType) {
		return this.subclass(TypeDefinition.Sort.describe(superType));
	}

	public <T> DynamicType.Builder<T> subclass(Type superType, ConstructorStrategy constructorStrategy) {
		return this.subclass(TypeDefinition.Sort.describe(superType), constructorStrategy);
	}

	public <T> DynamicType.Builder<T> subclass(TypeDefinition superType) {
		return this.subclass(superType, Default.IMITATE_SUPER_CLASS_OPENING);
	}

	public <T> DynamicType.Builder<T> subclass(TypeDefinition superType, ConstructorStrategy constructorStrategy) {
		if (!superType.isPrimitive() && !superType.isArray() && !superType.isFinal()) {
			Generic actualSuperType;
			TypeList.Generic interfaceTypes;
			if (superType.isInterface()) {
				actualSuperType = Generic.OfNonGenericType.ForLoadedType.of(Object.class);
				interfaceTypes = new TypeList.Generic.Explicit(superType);
			}
			else {
				actualSuperType = superType.asGenericType();
				interfaceTypes = new TypeList.Generic.Empty();
			}

			return new SubclassDynamicTypeBuilder<>(
					this.instrumentedTypeFactory.subclass(this.namingStrategy.subclass(superType.asGenericType()),
							ModifierContributor.Resolver
								.of(new ModifierContributor.ForType[] { Visibility.PUBLIC, TypeManifestation.PLAIN })
								.resolve(superType.getModifiers()),
							actualSuperType)
						.withInterfaces(interfaceTypes),
					this.classFileVersion, this.auxiliaryTypeNamingStrategy, this.annotationValueFilterFactory,
					this.annotationRetention, this.implementationContextFactory, this.methodGraphCompiler,
					this.typeValidation, this.visibilityBridgeStrategy, this.classReaderFactory,
					this.classWriterFactory, this.ignoredMethods, constructorStrategy);
		}
		else {
			throw new IllegalArgumentException("Cannot subclass primitive, array or final types: " + superType);
		}
	}

	public <T> DynamicType.Builder<T> makeInterface() {
		return this.makeInterface(Collections.<Type>emptyList());
	}

	public <T> DynamicType.Builder<T> makeInterface(Class<T> interfaceType) {
		return this.makeInterface(Collections.singletonList(interfaceType));
	}

	public <T> DynamicType.Builder<T> makeInterface(Type... interfaceType) {
		return this.makeInterface(Arrays.asList(interfaceType));
	}

	public <T> DynamicType.Builder<T> makeInterface(List<? extends Type> interfaceTypes) {
		return this.makeInterface(new TypeList.Generic.ForLoadedTypes(interfaceTypes));
	}

	public <T> DynamicType.Builder<T> makeInterface(TypeDefinition... interfaceType) {
		return this.makeInterface(Arrays.asList(interfaceType));
	}

	public <T> DynamicType.Builder<T> makeInterface(Collection<? extends TypeDefinition> interfaceTypes) {
		return this.<T>subclass(Object.class, Default.NO_CONSTRUCTORS)
			.implement(interfaceTypes)
			.modifiers(TypeManifestation.INTERFACE, Visibility.PUBLIC);
	}

	public <T> DynamicType.Builder<T> makePackage(String name) {
		return new SubclassDynamicTypeBuilder<>(
				this.instrumentedTypeFactory.subclass(name + "." + "package-info", 5632,
						Generic.OfNonGenericType.ForLoadedType.of(Object.class)),
				this.classFileVersion, this.auxiliaryTypeNamingStrategy, this.annotationValueFilterFactory,
				this.annotationRetention, this.implementationContextFactory, this.methodGraphCompiler,
				this.typeValidation, this.visibilityBridgeStrategy, this.classReaderFactory, this.classWriterFactory,
				this.ignoredMethods, Default.NO_CONSTRUCTORS);
	}

	public <T> DynamicType.Builder<T> makeRecord() {
		Generic record = InstrumentedType.Default
			.of(JavaType.RECORD.getTypeStub().getName(), Generic.OfNonGenericType.ForLoadedType.of(Object.class),
					Visibility.PUBLIC)
			.withMethod(new MethodDescription.Token(4))
			.withMethod(new MethodDescription.Token("hashCode", 1025, ForLoadedType.of(Integer.TYPE).asGenericType()))
			.withMethod(new MethodDescription.Token("equals", 1025, ForLoadedType.of(Boolean.TYPE).asGenericType(),
					Collections.singletonList(Generic.OfNonGenericType.ForLoadedType.of(Object.class))))
			.withMethod(new MethodDescription.Token("toString", 1025, ForLoadedType.of(String.class).asGenericType()))
			.asGenericType();
		return (new SubclassDynamicTypeBuilder<T>(
				this.instrumentedTypeFactory.subclass(this.namingStrategy.subclass(record), 17, record)
					.withRecord(true),
				this.classFileVersion, this.auxiliaryTypeNamingStrategy, this.annotationValueFilterFactory,
				this.annotationRetention, this.implementationContextFactory, this.methodGraphCompiler,
				this.typeValidation, this.visibilityBridgeStrategy, this.classReaderFactory, this.classWriterFactory,
				this.ignoredMethods, GenericsByteBuddy.RecordConstructorStrategy.INSTANCE))
			.method(ElementMatchers.isHashCode())
			.intercept(GenericsByteBuddy.RecordObjectMethod.HASH_CODE)
			.method(ElementMatchers.isEquals())
			.intercept(GenericsByteBuddy.RecordObjectMethod.EQUALS)
			.method(ElementMatchers.isToString())
			.intercept(GenericsByteBuddy.RecordObjectMethod.TO_STRING);
	}

	public <T extends Annotation> DynamicType.Builder<T> makeAnnotation() {
		return new SubclassDynamicTypeBuilder<>(
				this.instrumentedTypeFactory
					.subclass(this.namingStrategy.subclass(Generic.OfNonGenericType.ForLoadedType.of(Annotation.class)),
							ModifierContributor.Resolver
								.of(new ModifierContributor.ForType[] {
										Visibility.PUBLIC, TypeManifestation.ANNOTATION })
								.resolve(),
							Generic.OfNonGenericType.ForLoadedType.of(Object.class))
					.withInterfaces(
							new TypeList.Generic.Explicit(Generic.OfNonGenericType.ForLoadedType.of(Annotation.class))),
				this.classFileVersion, this.auxiliaryTypeNamingStrategy, this.annotationValueFilterFactory,
				this.annotationRetention, this.implementationContextFactory, this.methodGraphCompiler,
				this.typeValidation, this.visibilityBridgeStrategy, this.classReaderFactory, this.classWriterFactory,
				this.ignoredMethods, Default.NO_CONSTRUCTORS);
	}

	public <T extends Enum<T>> DynamicType.Builder<T> makeEnumeration(String... value) {
		return this.makeEnumeration(Arrays.asList(value));
	}

	public <T extends Enum<T>> DynamicType.Builder<T> makeEnumeration(Collection<? extends String> values) {
		if (values.isEmpty()) {
			throw new IllegalArgumentException("Require at least one enumeration constant");
		}
		else {
			Generic enumType = Generic.Builder.parameterizedType(Enum.class, new Type[] { TargetType.class }).build();
			return (new SubclassDynamicTypeBuilder<T>(
					this.instrumentedTypeFactory.subclass(this.namingStrategy.subclass(enumType),
							ModifierContributor.Resolver.of(new ModifierContributor.ForType[] { Visibility.PUBLIC,
									TypeManifestation.FINAL, EnumerationState.ENUMERATION })
								.resolve(),
							enumType),
					this.classFileVersion, this.auxiliaryTypeNamingStrategy, this.annotationValueFilterFactory,
					this.annotationRetention, this.implementationContextFactory, this.methodGraphCompiler,
					this.typeValidation, this.visibilityBridgeStrategy, this.classReaderFactory,
					this.classWriterFactory, this.ignoredMethods, Default.NO_CONSTRUCTORS))
				.defineConstructor(new ModifierContributor.ForMethod[] { Visibility.PRIVATE })
				.withParameters(new Type[] { String.class, Integer.TYPE })
				.intercept(SuperMethodCall.INSTANCE)
				.defineMethod("valueOf", TargetType.class,
						new ModifierContributor.ForMethod[] { Visibility.PUBLIC, Ownership.STATIC })
				.withParameters(new Type[] { String.class })
				.intercept(MethodCall
					.invoke(((MethodList<?>) enumType.getDeclaredMethods()
						.filter(ElementMatchers.named("valueOf")
							.and(ElementMatchers.takesArguments(Class.class, String.class))))
						.getOnly())
					.withOwnType()
					.withArgument(0)
					.withAssigner(Assigner.DEFAULT, Assigner.Typing.DYNAMIC))
				.defineMethod("values", TargetType[].class,
						new ModifierContributor.ForMethod[] { Visibility.PUBLIC, Ownership.STATIC })
				.intercept(new GenericsByteBuddy.EnumerationImplementation(new ArrayList<>(values)));
		}
	}

	public <T> DynamicType.Builder<T> redefine(Class<T> type) {
		return this.redefine(type, ClassFileLocator.ForClassLoader.of(type.getClassLoader()));
	}

	public <T> DynamicType.Builder<T> redefine(Class<T> type, ClassFileLocator classFileLocator) {
		return this.redefine(ForLoadedType.of(type), classFileLocator);
	}

	public <T> DynamicType.Builder<T> redefine(TypeDescription type, ClassFileLocator classFileLocator) {
		if (!type.isArray() && !type.isPrimitive()) {
			return new RedefinitionDynamicTypeBuilder<>(this.instrumentedTypeFactory.represent(type),
					this.classFileVersion, this.auxiliaryTypeNamingStrategy, this.annotationValueFilterFactory,
					this.annotationRetention, this.implementationContextFactory, this.methodGraphCompiler,
					this.typeValidation, this.visibilityBridgeStrategy, this.classReaderFactory,
					this.classWriterFactory, this.ignoredMethods, type, classFileLocator);
		}
		else {
			throw new IllegalArgumentException("Cannot redefine array or primitive type: " + type);
		}
	}

	public <T> DynamicType.Builder<T> rebase(Class<T> type) {
		return this.rebase(type, ClassFileLocator.ForClassLoader.of(type.getClassLoader()));
	}

	public <T> DynamicType.Builder<T> rebase(Class<T> type, ClassFileLocator classFileLocator) {
		return this.rebase(ForLoadedType.of(type), classFileLocator);
	}

	public <T> DynamicType.Builder<T> rebase(Class<T> type, ClassFileLocator classFileLocator,
			MethodNameTransformer methodNameTransformer) {
		return this.rebase(ForLoadedType.of(type), classFileLocator, methodNameTransformer);
	}

	public <T> DynamicType.Builder<T> rebase(TypeDescription type, ClassFileLocator classFileLocator) {
		return this.rebase(type, classFileLocator, MethodNameTransformer.Suffixing.withRandomSuffix());
	}

	public <T> DynamicType.Builder<T> rebase(TypeDescription type, ClassFileLocator classFileLocator,
			MethodNameTransformer methodNameTransformer) {
		if (!type.isArray() && !type.isPrimitive()) {
			return new RebaseDynamicTypeBuilder<>(this.instrumentedTypeFactory.represent(type), this.classFileVersion,
					this.auxiliaryTypeNamingStrategy, this.annotationValueFilterFactory, this.annotationRetention,
					this.implementationContextFactory, this.methodGraphCompiler, this.typeValidation,
					this.visibilityBridgeStrategy, this.classReaderFactory, this.classWriterFactory,
					this.ignoredMethods, type, classFileLocator, methodNameTransformer);
		}
		else {
			throw new IllegalArgumentException("Cannot rebase array or primitive type: " + type);
		}
	}

	public <T> DynamicType.Builder<T> rebase(Package aPackage, ClassFileLocator classFileLocator) {
		return this.rebase(new PackageDescription.ForLoadedPackage(aPackage), classFileLocator);
	}

	public <T> DynamicType.Builder<T> rebase(PackageDescription aPackage, ClassFileLocator classFileLocator) {
		return this.rebase(new ForPackageDescription(aPackage), classFileLocator);
	}

	public <T> DynamicType.Builder<T> decorate(Class<T> type) {
		return this.decorate(type, ClassFileLocator.ForClassLoader.of(type.getClassLoader()));
	}

	public <T> DynamicType.Builder<T> decorate(Class<T> type, ClassFileLocator classFileLocator) {
		return this.decorate(ForLoadedType.of(type), classFileLocator);
	}

	public <T> DynamicType.Builder<T> decorate(TypeDescription type, ClassFileLocator classFileLocator) {
		if (!type.isArray() && !type.isPrimitive()) {
			return new DecoratingDynamicTypeBuilder<>(type, this.classFileVersion, this.auxiliaryTypeNamingStrategy,
					this.annotationValueFilterFactory, this.annotationRetention, this.implementationContextFactory,
					this.methodGraphCompiler, this.typeValidation, this.classReaderFactory, this.classWriterFactory,
					this.ignoredMethods, classFileLocator);
		}
		else {
			throw new IllegalArgumentException("Cannot decorate array or primitive type: " + type);
		}
	}

	public GenericsByteBuddy with(ClassFileVersion classFileVersion) {
		return new GenericsByteBuddy(classFileVersion, this.namingStrategy, this.auxiliaryTypeNamingStrategy,
				this.annotationValueFilterFactory, this.annotationRetention, this.implementationContextFactory,
				this.methodGraphCompiler, this.instrumentedTypeFactory, this.typeValidation,
				this.visibilityBridgeStrategy, this.classReaderFactory, this.classWriterFactory, this.ignoredMethods);
	}

	public GenericsByteBuddy with(NamingStrategy namingStrategy) {
		return new GenericsByteBuddy(this.classFileVersion, namingStrategy, this.auxiliaryTypeNamingStrategy,
				this.annotationValueFilterFactory, this.annotationRetention, this.implementationContextFactory,
				this.methodGraphCompiler, this.instrumentedTypeFactory, this.typeValidation,
				this.visibilityBridgeStrategy, this.classReaderFactory, this.classWriterFactory, this.ignoredMethods);
	}

	public GenericsByteBuddy with(AuxiliaryType.NamingStrategy auxiliaryTypeNamingStrategy) {
		return new GenericsByteBuddy(this.classFileVersion, this.namingStrategy, auxiliaryTypeNamingStrategy,
				this.annotationValueFilterFactory, this.annotationRetention, this.implementationContextFactory,
				this.methodGraphCompiler, this.instrumentedTypeFactory, this.typeValidation,
				this.visibilityBridgeStrategy, this.classReaderFactory, this.classWriterFactory, this.ignoredMethods);
	}

	public GenericsByteBuddy with(AnnotationValueFilter.Factory annotationValueFilterFactory) {
		return new GenericsByteBuddy(this.classFileVersion, this.namingStrategy, this.auxiliaryTypeNamingStrategy,
				annotationValueFilterFactory, this.annotationRetention, this.implementationContextFactory,
				this.methodGraphCompiler, this.instrumentedTypeFactory, this.typeValidation,
				this.visibilityBridgeStrategy, this.classReaderFactory, this.classWriterFactory, this.ignoredMethods);
	}

	public GenericsByteBuddy with(AnnotationRetention annotationRetention) {
		return new GenericsByteBuddy(this.classFileVersion, this.namingStrategy, this.auxiliaryTypeNamingStrategy,
				this.annotationValueFilterFactory, annotationRetention, this.implementationContextFactory,
				this.methodGraphCompiler, this.instrumentedTypeFactory, this.typeValidation,
				this.visibilityBridgeStrategy, this.classReaderFactory, this.classWriterFactory, this.ignoredMethods);
	}

	public GenericsByteBuddy with(Implementation.Context.Factory implementationContextFactory) {
		return new GenericsByteBuddy(this.classFileVersion, this.namingStrategy, this.auxiliaryTypeNamingStrategy,
				this.annotationValueFilterFactory, this.annotationRetention, implementationContextFactory,
				this.methodGraphCompiler, this.instrumentedTypeFactory, this.typeValidation,
				this.visibilityBridgeStrategy, this.classReaderFactory, this.classWriterFactory, this.ignoredMethods);
	}

	public GenericsByteBuddy with(MethodGraph.Compiler methodGraphCompiler) {
		return new GenericsByteBuddy(this.classFileVersion, this.namingStrategy, this.auxiliaryTypeNamingStrategy,
				this.annotationValueFilterFactory, this.annotationRetention, this.implementationContextFactory,
				methodGraphCompiler, this.instrumentedTypeFactory, this.typeValidation, this.visibilityBridgeStrategy,
				this.classReaderFactory, this.classWriterFactory, this.ignoredMethods);
	}

	public GenericsByteBuddy with(InstrumentedType.Factory instrumentedTypeFactory) {
		return new GenericsByteBuddy(this.classFileVersion, this.namingStrategy, this.auxiliaryTypeNamingStrategy,
				this.annotationValueFilterFactory, this.annotationRetention, this.implementationContextFactory,
				this.methodGraphCompiler, instrumentedTypeFactory, this.typeValidation, this.visibilityBridgeStrategy,
				this.classReaderFactory, this.classWriterFactory, this.ignoredMethods);
	}

	public GenericsByteBuddy with(TypeValidation typeValidation) {
		return new GenericsByteBuddy(this.classFileVersion, this.namingStrategy, this.auxiliaryTypeNamingStrategy,
				this.annotationValueFilterFactory, this.annotationRetention, this.implementationContextFactory,
				this.methodGraphCompiler, this.instrumentedTypeFactory, typeValidation, this.visibilityBridgeStrategy,
				this.classReaderFactory, this.classWriterFactory, this.ignoredMethods);
	}

	public GenericsByteBuddy with(VisibilityBridgeStrategy visibilityBridgeStrategy) {
		return new GenericsByteBuddy(this.classFileVersion, this.namingStrategy, this.auxiliaryTypeNamingStrategy,
				this.annotationValueFilterFactory, this.annotationRetention, this.implementationContextFactory,
				this.methodGraphCompiler, this.instrumentedTypeFactory, this.typeValidation, visibilityBridgeStrategy,
				this.classReaderFactory, this.classWriterFactory, this.ignoredMethods);
	}

	/**
	 * @deprecated
	 */
	@Deprecated
	public GenericsByteBuddy with(ClassWriterStrategy classWriterStrategy) {
		return new GenericsByteBuddy(this.classFileVersion, this.namingStrategy, this.auxiliaryTypeNamingStrategy,
				this.annotationValueFilterFactory, this.annotationRetention, this.implementationContextFactory,
				this.methodGraphCompiler, this.instrumentedTypeFactory, this.typeValidation,
				this.visibilityBridgeStrategy, this.classReaderFactory,
				new ClassWriterStrategy.Delegating(classWriterStrategy), this.ignoredMethods);
	}

	public GenericsByteBuddy with(AsmClassReader.Factory classReaderFactory) {
		return new GenericsByteBuddy(this.classFileVersion, this.namingStrategy, this.auxiliaryTypeNamingStrategy,
				this.annotationValueFilterFactory, this.annotationRetention, this.implementationContextFactory,
				this.methodGraphCompiler, this.instrumentedTypeFactory, this.typeValidation,
				this.visibilityBridgeStrategy, classReaderFactory, this.classWriterFactory, this.ignoredMethods);
	}

	public GenericsByteBuddy with(AsmClassWriter.Factory classWriterFactory) {
		return new GenericsByteBuddy(this.classFileVersion, this.namingStrategy, this.auxiliaryTypeNamingStrategy,
				this.annotationValueFilterFactory, this.annotationRetention, this.implementationContextFactory,
				this.methodGraphCompiler, this.instrumentedTypeFactory, this.typeValidation,
				this.visibilityBridgeStrategy, this.classReaderFactory, classWriterFactory, this.ignoredMethods);
	}

	public GenericsByteBuddy withIgnoredClassReader() {
		return this.classWriterFactory instanceof AsmClassWriter.Factory.Suppressing ? this
				: new GenericsByteBuddy(this.classFileVersion, this.namingStrategy, this.auxiliaryTypeNamingStrategy,
						this.annotationValueFilterFactory, this.annotationRetention, this.implementationContextFactory,
						this.methodGraphCompiler, this.instrumentedTypeFactory, this.typeValidation,
						this.visibilityBridgeStrategy, this.classReaderFactory,
						new AsmClassWriter.Factory.Suppressing(this.classWriterFactory), this.ignoredMethods);
	}

	public GenericsByteBuddy ignore(ElementMatcher<? super MethodDescription> ignoredMethods) {
		return this.ignore(new LatentMatcher.Resolved<>(ignoredMethods));
	}

	public GenericsByteBuddy ignore(LatentMatcher<? super MethodDescription> ignoredMethods) {
		return new GenericsByteBuddy(this.classFileVersion, this.namingStrategy, this.auxiliaryTypeNamingStrategy,
				this.annotationValueFilterFactory, this.annotationRetention, this.implementationContextFactory,
				this.methodGraphCompiler, this.instrumentedTypeFactory, this.typeValidation,
				this.visibilityBridgeStrategy, this.classReaderFactory, this.classWriterFactory, ignoredMethods);
	}

	static {
		String validation;
		try {
			validation = doPrivileged(new GetSystemPropertyAction(DEFAULT_NAMING_PROPERTY));
		}
		catch (Throwable var10) {
			validation = null;
		}

		DEFAULT_TYPE_VALIDATION = validation != null && !Boolean.parseBoolean(validation) ? TypeValidation.DISABLED
				: TypeValidation.ENABLED;

		String naming;
		try {
			naming = doPrivileged(new GetSystemPropertyAction("net.bytebuddy.naming"));
		}
		catch (Throwable var9) {
			naming = null;
		}

		NamingStrategy namingStrategy;
		AuxiliaryType.NamingStrategy auxiliaryNamingStrategy;
		Implementation.Context.Factory implementationContextFactory;
		if (naming == null) {
			if (GraalImageCode.getCurrent().isDefined()) {
				namingStrategy = new NamingStrategy.Suffixing("ByteBuddy",
						new WithCallerSuffix(NamingStrategy.Suffixing.BaseNameResolver.ForUnnamedType.INSTANCE),
						"net.bytebuddy.renamed");
				auxiliaryNamingStrategy = new AuxiliaryType.NamingStrategy.Suffixing("auxiliary");
				implementationContextFactory = new Implementation.Context.Default.Factory.WithFixedSuffix("synthetic");
			}
			else {
				namingStrategy = null;
				auxiliaryNamingStrategy = null;
				implementationContextFactory = null;
			}
		}
		else if (naming.equalsIgnoreCase("fixed")) {
			namingStrategy = new NamingStrategy.Suffixing("ByteBuddy",
					NamingStrategy.Suffixing.BaseNameResolver.ForUnnamedType.INSTANCE, "net.bytebuddy.renamed");
			auxiliaryNamingStrategy = new AuxiliaryType.NamingStrategy.Suffixing("auxiliary");
			implementationContextFactory = new Implementation.Context.Default.Factory.WithFixedSuffix("synthetic");
		}
		else if (naming.equalsIgnoreCase("caller")) {
			namingStrategy = new NamingStrategy.Suffixing("ByteBuddy",
					new WithCallerSuffix(NamingStrategy.Suffixing.BaseNameResolver.ForUnnamedType.INSTANCE),
					"net.bytebuddy.renamed");
			auxiliaryNamingStrategy = new AuxiliaryType.NamingStrategy.Suffixing("auxiliary");
			implementationContextFactory = new Implementation.Context.Default.Factory.WithFixedSuffix("synthetic");
		}
		else {
			long seed;
			try {
				seed = Long.parseLong(naming);
			}
			catch (Exception var8) {
				throw new IllegalStateException(
						"'net.bytebuddy.naming' is set to an unknown, non-numeric value: " + naming);
			}

			namingStrategy = new NamingStrategy.SuffixingRandom("ByteBuddy",
					NamingStrategy.Suffixing.BaseNameResolver.ForUnnamedType.INSTANCE, "net.bytebuddy.renamed",
					new RandomString(8, new Random(seed)));
			auxiliaryNamingStrategy = new AuxiliaryType.NamingStrategy.Suffixing("auxiliary");
			implementationContextFactory = new Implementation.Context.Default.Factory.WithFixedSuffix("synthetic");
		}

		DEFAULT_NAMING_STRATEGY = namingStrategy;
		DEFAULT_AUXILIARY_NAMING_STRATEGY = auxiliaryNamingStrategy;
		DEFAULT_IMPLEMENTATION_CONTEXT_FACTORY = implementationContextFactory;
	}

	@HashCodeAndEqualsPlugin.Enhance
	protected static class EnumerationImplementation implements Implementation {

		protected static final String CLONE_METHOD_NAME = "clone";

		protected static final String ENUM_VALUE_OF_METHOD_NAME = "valueOf";

		protected static final String ENUM_VALUES_METHOD_NAME = "values";

		private static final int ENUM_FIELD_MODIFIERS = 25;

		private static final String ENUM_VALUES = "$VALUES";

		private final List<String> values;

		protected EnumerationImplementation(List<String> values) {
			this.values = values;
		}

		@NonNull
		public InstrumentedType prepare(@NonNull InstrumentedType instrumentedType) {
			for (String value : this.values) {
				instrumentedType = instrumentedType
					.withField(new FieldDescription.Token(value, 16409, TargetType.DESCRIPTION.asGenericType()));
			}

			return instrumentedType
				.withField(new FieldDescription.Token("$VALUES", 4121,
						ArrayProjection.of(TargetType.DESCRIPTION).asGenericType()))
				.withInitializer(new GenericsByteBuddy.EnumerationImplementation.InitializationAppender(this.values));
		}

		@NonNull
		public ByteCodeAppender appender(Implementation.Target implementationTarget) {
			return new GenericsByteBuddy.EnumerationImplementation.ValuesMethodAppender(
					implementationTarget.getInstrumentedType());
		}

		@HashCodeAndEqualsPlugin.Enhance
		protected static class ValuesMethodAppender implements ByteCodeAppender {

			private final TypeDescription instrumentedType;

			protected ValuesMethodAppender(TypeDescription instrumentedType) {
				this.instrumentedType = instrumentedType;
			}

			@NonNull
			public ByteCodeAppender.Size apply(@NonNull MethodVisitor methodVisitor,
					@NonNull Implementation.Context implementationContext, MethodDescription instrumentedMethod) {
				FieldDescription valuesField = ((FieldList<?>) this.instrumentedType.getDeclaredFields()
					.filter(ElementMatchers.named("$VALUES"))).getOnly();
				MethodDescription cloneMethod = ((MethodList<?>) Generic.OfNonGenericType.ForLoadedType.of(Object.class)
					.getDeclaredMethods()
					.filter(ElementMatchers.named("clone"))).getOnly();
				return new ByteCodeAppender.Size((new StackManipulation.Compound(
						new StackManipulation[] { FieldAccess.forField(valuesField).read(),
								MethodInvocation.invoke(cloneMethod).virtual(valuesField.getType().asErasure()),
								TypeCasting.to(valuesField.getType().asErasure()), MethodReturn.REFERENCE }))
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

			@NonNull
			public ByteCodeAppender.Size apply(@NonNull MethodVisitor methodVisitor,
					@NonNull Implementation.Context implementationContext, MethodDescription instrumentedMethod) {
				TypeDescription instrumentedType = instrumentedMethod.getDeclaringType().asErasure();
				MethodDescription enumConstructor = ((MethodList<?>) instrumentedType.getDeclaredMethods()
					.filter(ElementMatchers.isConstructor()
						.and(ElementMatchers.takesArguments(String.class, Integer.TYPE))))
					.getOnly();
				int ordinal = 0;
				StackManipulation stackManipulation = StackManipulation.Trivial.INSTANCE;
				List<FieldDescription> enumerationFields = new ArrayList<>(this.values.size());

				for (String value : this.values) {
					FieldDescription fieldDescription = ((FieldList<?>) instrumentedType.getDeclaredFields()
						.filter(ElementMatchers.named(value))).getOnly();
					stackManipulation = new StackManipulation.Compound(stackManipulation,
							TypeCreation.of(instrumentedType), Duplication.SINGLE, new TextConstant(value),
							IntegerConstant.forValue(ordinal++), MethodInvocation.invoke(enumConstructor),
							FieldAccess.forField(fieldDescription).write());
					enumerationFields.add(fieldDescription);
				}

				List<StackManipulation> fieldGetters = new ArrayList<>(this.values.size());

				for (FieldDescription fieldDescription : enumerationFields) {
					fieldGetters.add(FieldAccess.forField(fieldDescription).read());
				}

				StackManipulation var12 = new StackManipulation.Compound(stackManipulation,
						ArrayFactory.forType(instrumentedType.asGenericType()).withValues(fieldGetters),
						FieldAccess
							.forField((FieldDescription.InDefinedShape) ((FieldList<?>) instrumentedType
								.getDeclaredFields()
								.filter(ElementMatchers.named("$VALUES"))).getOnly())
							.write());
				return new ByteCodeAppender.Size(var12.apply(methodVisitor, implementationContext).getMaximalSize(),
						instrumentedMethod.getStackSize());
			}

		}

	}

	@HashCodeAndEqualsPlugin.Enhance
	protected enum RecordConstructorStrategy implements ConstructorStrategy, Implementation {

		INSTANCE;

		RecordConstructorStrategy() {
		}

		@NonNull
		public List<MethodDescription.Token> extractConstructors(TypeDescription instrumentedType) {
			List<ParameterDescription.Token> tokens = new ArrayList<>(instrumentedType.getRecordComponents().size());

			for (RecordComponentDescription.InDefinedShape recordComponent : instrumentedType.getRecordComponents()) {
				tokens.add(new ParameterDescription.Token(recordComponent.getType(),
						recordComponent.getDeclaredAnnotations()
							.filter(ElementMatchers.targetsElement(ElementType.CONSTRUCTOR)),
						recordComponent.getActualName(), 0));
			}

			return Collections.singletonList(new MethodDescription.Token("<init>", 1, Collections.emptyList(),
					Generic.OfNonGenericType.ForLoadedType.of(Void.TYPE), tokens, Collections.emptyList(),
					Collections.emptyList(), AnnotationValue.UNDEFINED, Generic.UNDEFINED));
		}

		@NonNull
		public MethodRegistry inject(TypeDescription instrumentedType, MethodRegistry methodRegistry) {
			return methodRegistry.prepend(
					new LatentMatcher.Resolved<>(ElementMatchers.isConstructor()
						.and(ElementMatchers
							.takesGenericArguments(instrumentedType.getRecordComponents().asTypeList()))),
					new MethodRegistry.Handler.ForImplementation(this),
					MethodAttributeAppender.ForInstrumentedMethod.EXCLUDING_RECEIVER, Transformer.NoOp.make());
		}

		@NonNull
		public ByteCodeAppender appender(Implementation.Target implementationTarget) {
			return new GenericsByteBuddy.RecordConstructorStrategy.Appender(implementationTarget.getInstrumentedType());
		}

		@NonNull
		public InstrumentedType prepare(InstrumentedType instrumentedType) {
			for (RecordComponentDescription.InDefinedShape recordComponent : instrumentedType.getRecordComponents()) {
				instrumentedType = instrumentedType
					.withField(
							new FieldDescription.Token(recordComponent.getActualName(), 18, recordComponent.getType(),
									recordComponent.getDeclaredAnnotations()
										.filter(ElementMatchers.targetsElement(ElementType.FIELD))))
					.withMethod(new MethodDescription.Token(recordComponent.getActualName(), 1, Collections.emptyList(),
							recordComponent.getType(), Collections.emptyList(), Collections.emptyList(),
							recordComponent.getDeclaredAnnotations()
								.filter(ElementMatchers.targetsElement(ElementType.METHOD)),
							AnnotationValue.UNDEFINED, Generic.UNDEFINED));
			}

			return instrumentedType;
		}

		@HashCodeAndEqualsPlugin.Enhance
		protected static class Appender implements ByteCodeAppender {

			private final TypeDescription instrumentedType;

			protected Appender(TypeDescription instrumentedType) {
				this.instrumentedType = instrumentedType;
			}

			@NonNull
			public ByteCodeAppender.Size apply(@NonNull MethodVisitor methodVisitor,
					@NonNull Implementation.Context implementationContext, MethodDescription instrumentedMethod) {
				if (instrumentedMethod.isMethod()) {
					return (new ByteCodeAppender.Simple(MethodVariableAccess.loadThis(),
							FieldAccess
								.forField((FieldDescription.InDefinedShape) ((FieldList<?>) this.instrumentedType
									.getDeclaredFields()
									.filter(ElementMatchers.named(instrumentedMethod.getName()))).getOnly())
								.read(),
							MethodReturn.of(instrumentedMethod.getReturnType())))
						.apply(methodVisitor, implementationContext, instrumentedMethod);
				}
				else {
					List<StackManipulation> stackManipulations = new ArrayList<>(
							this.instrumentedType.getRecordComponents().size() * 3 + 2);
					stackManipulations.add(MethodVariableAccess.loadThis());
					stackManipulations
						.add(MethodInvocation.invoke(new MethodDescription.Latent(JavaType.RECORD.getTypeStub(),
								new MethodDescription.Token(1))));
					int offset = 1;

					for (RecordComponentDescription.InDefinedShape recordComponent : this.instrumentedType
						.getRecordComponents()) {
						stackManipulations.add(MethodVariableAccess.loadThis());
						stackManipulations.add(MethodVariableAccess.of(recordComponent.getType()).loadFrom(offset));
						stackManipulations.add(FieldAccess
							.forField((FieldDescription.InDefinedShape) ((FieldList<?>) this.instrumentedType
								.getDeclaredFields()
								.filter(ElementMatchers.named(recordComponent.getActualName()))).getOnly())
							.write());
						offset += recordComponent.getType().getStackSize().getSize();
					}

					stackManipulations.add(MethodReturn.VOID);
					return (new ByteCodeAppender.Simple(stackManipulations)).apply(methodVisitor, implementationContext,
							instrumentedMethod);
				}
			}

		}

	}

	@HashCodeAndEqualsPlugin.Enhance
	protected enum RecordObjectMethod implements Implementation {

		HASH_CODE("hashCode", StackManipulation.Trivial.INSTANCE, Integer.TYPE),
		EQUALS("equals", MethodVariableAccess.REFERENCE.loadFrom(1), Boolean.TYPE, Object.class),
		TO_STRING("toString", StackManipulation.Trivial.INSTANCE, String.class);

		private final String name;

		private final StackManipulation stackManipulation;

		private final TypeDescription returnType;

		private final List<? extends TypeDescription> arguments;

		RecordObjectMethod(String name, StackManipulation stackManipulation, Class<?> returnType,
				Class<?>... arguments) {
			this.name = name;
			this.stackManipulation = stackManipulation;
			this.returnType = ForLoadedType.of(returnType);
			this.arguments = new TypeList.ForLoadedTypes(arguments);
		}

		@NonNull
		public ByteCodeAppender appender(Implementation.Target implementationTarget) {
			StringBuilder stringBuilder = new StringBuilder();
			List<JavaConstant> methodHandles = new ArrayList<>(
					implementationTarget.getInstrumentedType().getRecordComponents().size());

			for (RecordComponentDescription.InDefinedShape recordComponent : implementationTarget.getInstrumentedType()
				.getRecordComponents()) {
				if (!stringBuilder.isEmpty()) {
					stringBuilder.append(";");
				}

				stringBuilder.append(recordComponent.getActualName());
				methodHandles.add(JavaConstant.MethodHandle.ofGetter(
						(FieldDescription.InDefinedShape) ((FieldList<?>) implementationTarget.getInstrumentedType()
							.getDeclaredFields()
							.filter(ElementMatchers.named(recordComponent.getActualName()))).getOnly()));
			}

			return new ByteCodeAppender.Simple(MethodVariableAccess.loadThis(), this.stackManipulation,
					MethodInvocation
						.invoke(new MethodDescription.Latent(JavaType.OBJECT_METHODS.getTypeStub(),
								new MethodDescription.Token("bootstrap", 9,
										Generic.OfNonGenericType.ForLoadedType.of(Object.class),
										Arrays.asList(JavaType.METHOD_HANDLES_LOOKUP.getTypeStub().asGenericType(),
												ForLoadedType.of(String.class).asGenericType(),
												JavaType.TYPE_DESCRIPTOR.getTypeStub().asGenericType(),
												ForLoadedType.of(Class.class).asGenericType(),
												ForLoadedType.of(String.class).asGenericType(),
												ArrayProjection.of(JavaType.METHOD_HANDLE.getTypeStub())
													.asGenericType()))))
						.dynamic(this.name, this.returnType,
								CompoundList.of(implementationTarget.getInstrumentedType(), this.arguments),
								CompoundList.of(
										Arrays.asList(
												JavaConstant.Simple.of(implementationTarget.getInstrumentedType()),
												JavaConstant.Simple.ofLoaded(stringBuilder.toString())),
										methodHandles)),
					MethodReturn.of(this.returnType));
		}

		@NonNull
		public InstrumentedType prepare(@NonNull InstrumentedType instrumentedType) {
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
