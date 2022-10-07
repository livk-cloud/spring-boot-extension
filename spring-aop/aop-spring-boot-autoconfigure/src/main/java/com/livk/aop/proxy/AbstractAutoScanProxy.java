package com.livk.aop.proxy;

import org.springframework.aop.PointcutAdvisor;
import org.springframework.aop.TargetSource;
import org.springframework.aop.framework.autoproxy.AbstractAutoProxyCreator;
import org.springframework.beans.BeansException;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

/**
 * <p>
 * AbstractAutoScanProxy
 * </p>
 *
 * @author livk
 * @date 2022/9/29
 */
public abstract class AbstractAutoScanProxy extends AbstractAutoProxyCreator {

    @Override
    protected Object[] getAdvicesAndAdvisorsForBean(Class<?> beanClass, String beanName, TargetSource customTargetSource) throws BeansException {
        Set<Object> advices = new HashSet<>();
        for (Class<? extends Annotation> proxyAnnotation : findProxyAnnotations()) {
            if (beanClass.isAnnotationPresent(proxyAnnotation)) {
                advices.addAll(getPointcutAdvisors());
            }
            for (Method method : beanClass.getMethods()) {
                if (method.isAnnotationPresent(proxyAnnotation)) {
                    advices.addAll(getPointcutAdvisors());
                }
            }
        }
        return advices.isEmpty() ? DO_NOT_PROXY : advices.toArray();
    }

    protected abstract Collection<PointcutAdvisor> getPointcutAdvisors();

    protected abstract Collection<Class<? extends Annotation>> findProxyAnnotations();
}
