package com.livk.aop.intercept;

import org.aopalliance.intercept.MethodInvocation;

/**
 * <p>
 * ExecutionIntercept
 * </p>
 *
 * @author livk
 * @date 2022/7/6
 */
public interface ExecutionIntercept {

    String expression();

    Object invoke(MethodInvocation invocation) throws Throwable;
}
