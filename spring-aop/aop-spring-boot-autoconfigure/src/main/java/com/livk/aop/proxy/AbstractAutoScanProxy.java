package com.livk.aop.proxy;

import org.aopalliance.aop.Advice;
import org.aopalliance.intercept.MethodInterceptor;
import org.springframework.aop.TargetSource;
import org.springframework.aop.framework.autoproxy.AbstractAutoProxyCreator;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * <p>
 * AbstractAutoScanProxy
 * </p>
 *
 * @author livk
 * @date 2022/9/29
 */
public abstract class AbstractAutoScanProxy extends AbstractAutoProxyCreator implements ApplicationContextAware {

    private ApplicationContext applicationContext;

    @Override
    protected Object[] getAdvicesAndAdvisorsForBean(Class<?> beanClass, String beanName, TargetSource customTargetSource) throws BeansException {
        Set<Object> advices = new HashSet<>();
//        for (PointcutAdvisor advisor : get()) {
//            Pointcut pointcut = advisor.getPointcut();
//            if (pointcut.getClassFilter().matches(beanClass)) {
//                advices = ArrayUtils.addAll(advices, advisor);
//            }
//            for (Method method : beanClass.getMethods()) {
//                if (pointcut.getMethodMatcher().matches(method, beanClass)) {
//                    advices = ArrayUtils.addAll(advices, advisor);
//                }
//            }
//
//        }
        for (Class<? extends Annotation> proxyAnnotation : findProxyAnnotations()) {
            if (beanClass.isAnnotationPresent(proxyAnnotation)) {
                advices.addAll(getProxyClass());
            }
            for (Method method : beanClass.getMethods()) {
                if (method.isAnnotationPresent(proxyAnnotation)) {
                    advices.addAll(getProxyClass());
                }
            }
        }
        return advices.isEmpty() ? DO_NOT_PROXY : advices.toArray();
    }


    protected Collection<Advice> getProxyClass() {
        return applicationContext.getBeanProvider(findMethodInvocations())
                .stream().collect(Collectors.toSet());
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    protected abstract Collection<Class<? extends Annotation>> findProxyAnnotations();

    protected abstract Class<? extends MethodInterceptor> findMethodInvocations();
}
