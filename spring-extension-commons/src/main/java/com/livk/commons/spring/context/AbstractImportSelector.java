package com.livk.commons.spring.context;

import lombok.Setter;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.BeanClassLoaderAware;
import org.springframework.boot.context.annotation.ImportCandidates;
import org.springframework.context.EnvironmentAware;
import org.springframework.context.annotation.DeferredImportSelector;
import org.springframework.core.GenericTypeResolver;
import org.springframework.core.Ordered;
import org.springframework.core.env.Environment;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.lang.Nullable;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

import java.lang.annotation.Annotation;
import java.util.List;

/**
 * <p>
 * AbstractImportSelector
 * </p>
 *
 * @param <T> the type parameter
 * @author livk
 */
public abstract class AbstractImportSelector<T extends Annotation> implements DeferredImportSelector, Ordered, EnvironmentAware, BeanClassLoaderAware {

    /**
     * The Annotation class.
     */
    @SuppressWarnings("unchecked")
    protected final Class<T> annotationClass = (Class<T>) GenericTypeResolver.resolveTypeArgument(this.getClass(), AbstractImportSelector.class);

    /**
     * The Environment.
     */
    @Setter
    protected Environment environment;

    /**
     * The Class loader.
     */
    protected ClassLoader classLoader;

    @NotNull
    @Override
    public String[] selectImports(@Nullable AnnotationMetadata importingClassMetadata) {
        if (!isEnabled()) {
            return new String[0];
        }
        Assert.notNull(annotationClass, "annotation Class not be null");
        List<String> names = ImportCandidates.load(annotationClass, classLoader).getCandidates();
        return StringUtils.toStringArray(names);
    }

    /**
     * Is enabled boolean.
     *
     * @return the boolean
     */
    protected boolean isEnabled() {
        return true;
    }

    @Override
    public int getOrder() {
        return Ordered.HIGHEST_PRECEDENCE;
    }

    @Override
    public void setBeanClassLoader(ClassLoader classLoader) {
        this.classLoader = classLoader;
    }
}
