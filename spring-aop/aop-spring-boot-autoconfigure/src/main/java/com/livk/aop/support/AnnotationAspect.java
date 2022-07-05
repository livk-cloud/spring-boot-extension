package com.livk.aop.support;

import com.google.common.base.CaseFormat;
import com.livk.aop.intercept.AnnotationIntercept;
import org.jetbrains.annotations.NotNull;
import org.springframework.aop.support.DefaultPointcutAdvisor;
import org.springframework.aop.support.annotation.AnnotationMatchingPointcut;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.ListableBeanFactory;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.RootBeanDefinition;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.type.AnnotationMetadata;

/**
 * <p>
 * AnnotationAspect
 * </p>
 *
 * @author livk
 * @date 2022/7/5
 */
public class AnnotationAspect implements ImportBeanDefinitionRegistrar, BeanFactoryAware {
    private ListableBeanFactory listableBeanFactory;

    @Override
    public void setBeanFactory(@NotNull BeanFactory beanFactory) throws BeansException {
        listableBeanFactory = (ListableBeanFactory) beanFactory;
    }

    @Override
    public void registerBeanDefinitions(@NotNull AnnotationMetadata importingClassMetadata, @NotNull BeanDefinitionRegistry registry) {
        listableBeanFactory.getBeansOfType(AnnotationIntercept.class)
                .values()
                .forEach(annotationIntercept -> {
                    String beanName = getBeanName(annotationIntercept);
                    BeanDefinition beanDefinition = getBeanDefinition(annotationIntercept);
                    registry.registerBeanDefinition(beanName, beanDefinition);
                });
    }

    private String getBeanName(AnnotationIntercept annotationIntercept) {
        return CaseFormat.UPPER_CAMEL.to(CaseFormat.LOWER_CAMEL,
                annotationIntercept.getType().getSimpleName()) + "DefaultPointcutAdvisor";
    }

    private BeanDefinition getBeanDefinition(AnnotationIntercept annotationIntercept) {
        return new RootBeanDefinition(DefaultPointcutAdvisor.class, () -> {
            DefaultPointcutAdvisor advisor = new DefaultPointcutAdvisor();
            AnnotationMatchingPointcut pointcut;
            if (annotationIntercept.supportClass() && annotationIntercept.supportMethod()) {
                pointcut = new AnnotationMatchingPointcut(annotationIntercept.getType(), annotationIntercept.getType());
            } else if (annotationIntercept.supportClass()) {
                pointcut = new AnnotationMatchingPointcut(annotationIntercept.getType());
            } else {
                pointcut = new AnnotationMatchingPointcut(null, annotationIntercept.getType());
            }
            advisor.setPointcut(pointcut);
            advisor.setAdvice(new DefaultMethodInterceptor(annotationIntercept));
            return advisor;
        });
    }
}
