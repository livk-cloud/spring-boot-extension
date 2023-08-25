package com.livk.autoconfigure.disruptor;

import com.livk.autoconfigure.disruptor.annotation.DisruptorEvent;
import com.livk.autoconfigure.disruptor.annotation.EnableDisruptor;
import com.livk.autoconfigure.disruptor.exception.DisruptorRegistrarException;
import com.livk.commons.util.AnnotationUtils;
import com.livk.commons.util.ObjectUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.BeanNameGenerator;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.annotation.AnnotationAttributes;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.lang.NonNull;

import java.util.Arrays;

/**
 * @author livk
 */
@Slf4j
public class DisruptorRegistrar implements ImportBeanDefinitionRegistrar {

    @Override
    public void registerBeanDefinitions(@NonNull AnnotationMetadata importingClassMetadata,
                                        @NonNull BeanDefinitionRegistry registry,
                                        @NonNull BeanNameGenerator beanNameGenerator) {
        AnnotationAttributes attributes = AnnotationUtils.attributesFor(importingClassMetadata, EnableDisruptor.class);
        String[] basePackages = getBasePackages(attributes);
        if (ObjectUtils.isEmpty(basePackages)) {
            throw new DisruptorRegistrarException(EnableDisruptor.class.getName() + " required basePackages or basePackageClasses");
        }
        ClassPathDisruptorScanner scanner = new ClassPathDisruptorScanner(registry, beanNameGenerator);
        scanner.registerFilters(DisruptorEvent.class);
        scanner.scan(basePackages);
    }

    private String[] getBasePackages(AnnotationAttributes attributes) {
        String[] basePackages = attributes.getStringArray("basePackages");
        if (ObjectUtils.isEmpty(basePackages)) {
            Class<?>[] basePackageClasses = attributes.getClassArray("basePackageClasses");
            basePackages = Arrays.stream(basePackageClasses)
                    .map(Class::getPackageName)
                    .distinct()
                    .toArray(String[]::new);
        }
        return basePackages;
    }
}
