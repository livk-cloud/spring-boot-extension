package com.livk.aop.support;

import com.livk.aop.intercept.ExecutionIntercept;
import org.springframework.aop.aspectj.AspectJExpressionPointcut;
import org.springframework.aop.support.DefaultPointcutAdvisor;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.RootBeanDefinition;

/**
 * <p>
 * ExecutionAspect
 * </p>
 *
 * @author livk
 * @date 2022/7/6
 */
public class ExecutionAspect extends BaseAspect<ExecutionIntercept> {

    @Override
    protected BeanDefinition getBeanDefinition(ExecutionIntercept executionIntercept) {
        return new RootBeanDefinition(DefaultPointcutAdvisor.class, () -> {
            AspectJExpressionPointcut pointcut = new AspectJExpressionPointcut();
            pointcut.setExpression(executionIntercept.expression());
            return new DefaultPointcutAdvisor(pointcut, new DefaultExecutionIntercept(executionIntercept));
        });
    }
}
