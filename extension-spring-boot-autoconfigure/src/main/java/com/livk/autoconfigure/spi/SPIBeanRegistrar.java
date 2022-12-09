package com.livk.autoconfigure.spi;

import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.RootBeanDefinition;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.annotation.AnnotationAttributes;
import org.springframework.core.type.AnnotationMetadata;
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
 * @date 2022/12/9
 */
public class SPIBeanRegistrar implements ImportBeanDefinitionRegistrar {

    @Override
    public void registerBeanDefinitions(AnnotationMetadata importingClassMetadata, BeanDefinitionRegistry registry) {
        AnnotationAttributes attributes = AnnotationAttributes
                .fromMap(importingClassMetadata.getAnnotationAttributes(ImportSPI.class.getName()));
        if (!CollectionUtils.isEmpty(attributes)) {
            Class<?> clazz = attributes.getClass("value");
            Map<? extends Class<?>, ?> map = ServiceLoader.load(clazz)
                    .stream()
                    .collect(Collectors.toMap(ServiceLoader.Provider::type,
                            ServiceLoader.Provider::get));
            for (final Map.Entry<? extends Class<?>, ?> entry : map.entrySet()) {
                RootBeanDefinition beanDefinition = new RootBeanDefinition(entry.getKey());
                beanDefinition.setInstanceSupplier(entry::getValue);
                String beanName = StringUtils.uncapitalize(entry.getKey().getSimpleName());
                registry.registerBeanDefinition(beanName, beanDefinition);
            }
        }
    }
}
