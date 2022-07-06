package com.livk.aop.support;

import com.livk.aop.intercept.AnnotationIntercept;
import org.springframework.aop.support.DefaultPointcutAdvisor;
import org.springframework.aop.support.annotation.AnnotationMatchingPointcut;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.RootBeanDefinition;

/**
 * <p>
 * AnnotationAspect
 * </p>
 *
 * @author livk
 * @date 2022/7/5
 */
public class AnnotationAspect extends BaseAspect<AnnotationIntercept> {
    @Override
    protected BeanDefinition getBeanDefinition(AnnotationIntercept annotationIntercept) {
        return new RootBeanDefinition(DefaultPointcutAdvisor.class, () -> {
            DefaultPointcutAdvisor advisor = new DefaultPointcutAdvisor();
            AnnotationMatchingPointcut pointcut;
            if (annotationIntercept.supportClass() && annotationIntercept.supportMethod()) {
                pointcut = new AnnotationMatchingPointcut(annotationIntercept.type(), annotationIntercept.type());
            } else if (annotationIntercept.supportClass()) {
                pointcut = new AnnotationMatchingPointcut(annotationIntercept.type());
            } else {
                pointcut = new AnnotationMatchingPointcut(null, annotationIntercept.type());
            }
            advisor.setPointcut(pointcut);
            advisor.setAdvice(new DefaultAnnotationIntercept(annotationIntercept));
            return advisor;
        });
    }
}
