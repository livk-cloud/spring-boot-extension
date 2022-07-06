package com.livk.aop.support;

import com.livk.aop.intercept.ExecutionIntercept;
import lombok.RequiredArgsConstructor;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;


/**
 * <p>
 * DefaultExecutionIntercept
 * </p>
 *
 * @author livk
 * @date 2022/7/6
 */
@RequiredArgsConstructor
public class DefaultExecutionIntercept implements MethodInterceptor {

    private final ExecutionIntercept executionIntercept;

    @Nullable
    @Override
    public Object invoke(@NonNull MethodInvocation invocation) throws Throwable {
        return executionIntercept.invoke(invocation);
    }
}
