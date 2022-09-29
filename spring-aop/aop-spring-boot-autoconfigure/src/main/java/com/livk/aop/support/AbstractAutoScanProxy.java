package com.livk.aop.support;

import org.springframework.aop.TargetSource;
import org.springframework.aop.framework.autoproxy.AbstractAutoProxyCreator;
import org.springframework.beans.BeansException;

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
        return new Object[0];
    }
}
