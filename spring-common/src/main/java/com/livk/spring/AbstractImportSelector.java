package com.livk.spring;

import org.jetbrains.annotations.NotNull;
import org.springframework.boot.context.annotation.ImportCandidates;
import org.springframework.context.annotation.DeferredImportSelector;
import org.springframework.core.GenericTypeResolver;
import org.springframework.core.Ordered;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.lang.Nullable;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

import java.util.List;

/**
 * <p>
 * AbstractImportSelector
 * </p>
 *
 * @author livk
 * @date 2022/9/20
 */
public abstract class AbstractImportSelector<T> implements DeferredImportSelector, Ordered {

    @SuppressWarnings("unchecked")
    private final Class<T> annotationClass = (Class<T>) GenericTypeResolver.resolveTypeArgument(this.getClass(), AbstractImportSelector.class);

    @NotNull
    @Override
    public String[] selectImports(@Nullable AnnotationMetadata importingClassMetadata) {
        if (!isEnabled()) {
            return new String[0];
        }
        Assert.notNull(annotationClass, "annotation Class not be null");
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        List<String> names = ImportCandidates.load(annotationClass, classLoader).getCandidates();
        return StringUtils.toStringArray(names);
    }

    protected Class<T> getAnnotationClass() {
        return this.annotationClass;
    }

    protected boolean isEnabled() {
        return true;
    }

    @Override
    public int getOrder() {
        return Ordered.HIGHEST_PRECEDENCE;
    }
}
