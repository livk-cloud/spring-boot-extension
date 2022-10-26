package com.livk.aop.autoconfigure;

import com.livk.aop.autoconfigure.proxy.AnnotationAutoScanProxy;
import org.aopalliance.aop.Advice;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.RootBeanDefinition;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.Bean;
import org.springframework.core.Ordered;

/**
 * <p>
 * AopAutoConfiguration
 * </p>
 *
 * @author livk
 * @date 2022/7/5
 */
@AutoConfiguration
public class AopAutoConfiguration {

    @Bean
    @ConditionalOnClass(Advice.class)
    public BeanFactoryPostProcessor beanFactoryPostProcessor() {
        return beanFactory -> {
            if (beanFactory instanceof BeanDefinitionRegistry registry) {
                RootBeanDefinition beanDefinition = new RootBeanDefinition(AnnotationAutoScanProxy.class);
                beanDefinition.getPropertyValues().add("order", Ordered.HIGHEST_PRECEDENCE);
                beanDefinition.setRole(BeanDefinition.ROLE_INFRASTRUCTURE);
                registry.registerBeanDefinition(AnnotationAutoScanProxy.BEAN_NAME, beanDefinition);

                if (registry.containsBeanDefinition(AnnotationAutoScanProxy.BEAN_NAME)) {
                    BeanDefinition definition = registry.getBeanDefinition(AnnotationAutoScanProxy.BEAN_NAME);
                    definition.getPropertyValues().add("proxyTargetClass", Boolean.TRUE);
                }
            }
        };
    }
}
