package com.livk.example;

import com.livk.aop.intercept.ExecutionIntercept;
import lombok.extern.slf4j.Slf4j;
import org.aopalliance.intercept.MethodInvocation;
import org.springframework.stereotype.Component;

/**
 * <p>
 * ExpressionAspect
 * </p>
 *
 * @author livk
 * @date 2022/7/6
 */
@Slf4j
@Component
public class ExpressionAspect implements ExecutionIntercept {
    @Override
    public String expression() {
        return "execution(public * com.livk.example.AopController.look())";
    }

    @Override
    public Object invoke(MethodInvocation invocation) throws Throwable {
        log.info("exp start");
        Object proceed = invocation.proceed();
        log.info("exp end");
        return proceed;
    }
}
