package com.livk.autoconfigure.spi;

import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.RootBeanDefinition;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.annotation.AnnotatedElementUtils;
import org.springframework.core.annotation.AnnotationAttributes;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.Map;
import java.util.ServiceLoader;
import java.util.stream.Collectors;

/**
 * <p>
 * SPIBeanRegistrar
 * </p>
 *
 * @author livk
 */
public class SPIBeanRegistrar implements ImportBeanDefinitionRegistrar {

    @Override
    public void registerBeanDefinitions(AnnotationMetadata importingClassMetadata, BeanDefinitionRegistry registry) {
        AnnotationAttributes attributes = AnnotationAttributes
                .fromMap(importingClassMetadata.getAnnotationAttributes(ImportSPI.class.getName()));
        if (!CollectionUtils.isEmpty(attributes)) {
            Class<?> importSPIClass = attributes.getClass("value");
            Map<Class<?>, ?> map = ServiceLoader.load(importSPIClass)
                    .stream()
                    .collect(Collectors.toMap(ServiceLoader.Provider::type,
                            ServiceLoader.Provider::get));
            for (Map.Entry<? extends Class<?>, ?> entry : map.entrySet()) {
                Class<?> instanceClass = entry.getKey();
                //如果当前类已经注册，则跳过
                if (!AnnotatedElementUtils.hasAnnotation(instanceClass, Component.class)) {
                    RootBeanDefinition beanDefinition = new RootBeanDefinition(entry.getKey());
                    beanDefinition.setInstanceSupplier(entry::getValue);
                    String beanName = StringUtils.uncapitalize(entry.getKey().getSimpleName());
                    //如果当前beanName已经被注册，则跳过
                    if (!registry.containsBeanDefinition(beanName)) {
                        registry.registerBeanDefinition(beanName, beanDefinition);
                    }
                }
            }
        }
    }
}
