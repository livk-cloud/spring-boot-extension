package com.livk.spring;

import org.springframework.context.annotation.DeferredImportSelector;
import org.springframework.core.GenericTypeResolver;
import org.springframework.core.io.support.SpringFactoriesLoader;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.lang.Nullable;
import org.springframework.util.Assert;

import java.util.List;

/**
 * <p>
 * AbstractImportSelector
 * </p>
 *
 * @author livk
 * @date 2022/9/20
 */
public abstract class AbstractImportSelector<T> implements DeferredImportSelector {

    @SuppressWarnings("unchecked")
    private final Class<T> annotationClass = (Class<T>) GenericTypeResolver.resolveTypeArgument(this.getClass(), AbstractImportSelector.class);

    @Override
    public String[] selectImports(@Nullable AnnotationMetadata importingClassMetadata) {
        if (!isEnabled()) {
            return new String[0];
        }
        Assert.notNull(annotationClass, "annotation Class not be null");
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        List<String> names = SpringFactoriesLoader.loadFactoryNames(annotationClass, classLoader);
        return names.toArray(new String[0]);
    }

    protected Class<T> getAnnotationClass() {
        return this.annotationClass;
    }

    protected boolean isEnabled() {
        return true;
    }
}
