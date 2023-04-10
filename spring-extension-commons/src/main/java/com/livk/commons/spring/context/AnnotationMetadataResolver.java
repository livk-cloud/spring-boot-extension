package com.livk.commons.spring.context;

import com.livk.commons.util.AnnotationUtils;
import com.livk.commons.util.ObjectUtils;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.boot.autoconfigure.AutoConfigurationPackages;
import org.springframework.core.annotation.AnnotatedElementUtils;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternUtils;
import org.springframework.core.type.classreading.CachingMetadataReaderFactory;
import org.springframework.core.type.classreading.MetadataReader;
import org.springframework.core.type.classreading.MetadataReaderFactory;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.util.HashSet;
import java.util.Set;

/**
 * The type Annotation metadata resolver.
 *
 * @author livk
 */
public class AnnotationMetadataResolver {

    private final String[] packages;

    private final ResourcePatternResolver resolver;

    private final MetadataReaderFactory metadataReaderFactory;

    /**
     * Instantiates a new Annotation metadata resolver.
     *
     * @param resourceLoader the resource loader
     * @param packages       the packages
     */
    public AnnotationMetadataResolver(ResourceLoader resourceLoader, String... packages) {
        this.packages = packages;
        this.resolver = ResourcePatternUtils.getResourcePatternResolver(resourceLoader);
        this.metadataReaderFactory = new CachingMetadataReaderFactory(resourceLoader);
    }

    /**
     * Instantiates a new Annotation metadata resolver.
     *
     * @param resourceLoader the resource loader
     * @param beanFactory    the bean factory
     */
    public AnnotationMetadataResolver(ResourceLoader resourceLoader, BeanFactory beanFactory) {
        this(resourceLoader, StringUtils.toStringArray(AutoConfigurationPackages.get(beanFactory)));
    }

    /**
     * Find set.
     *
     * @param annotationType the annotation type
     * @return the set
     */
    public Set<Class<?>> find(Class<? extends Annotation> annotationType) {
        Set<Class<?>> typeSet = new HashSet<>();
        if (ObjectUtils.isEmpty(packages)) {
            return typeSet;
        }
        for (String packageStr : packages) {
            packageStr = packageStr.replace(".", "/");
            try {
                Resource[] resources = resolver.getResources("classpath*:" + packageStr + "/**/*.class");
                for (Resource resource : resources) {
                    MetadataReader metadataReader = metadataReaderFactory.getMetadataReader(resource);
                    String className = metadataReader.getClassMetadata().getClassName();
                    Class<?> type = Class.forName(className);
                    if (AnnotationUtils.isAnnotationDeclaredLocally(annotationType, type) ||
                        AnnotatedElementUtils.hasAnnotation(type, annotationType)) {
                        typeSet.add(type);
                    }
                }
            } catch (IOException | ClassNotFoundException e) {
                throw new RuntimeException(e);
            }
        }
        return typeSet;
    }
}
