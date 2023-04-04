package com.livk.autoconfigure.lock.intercept;

import com.livk.autoconfigure.aop.AnnotationAbstractPointcutAdvisor;
import com.livk.autoconfigure.lock.annotation.OnLock;
import com.livk.autoconfigure.lock.constant.LockScope;
import com.livk.autoconfigure.lock.exception.LockException;
import com.livk.autoconfigure.lock.exception.UnSupportLockException;
import com.livk.autoconfigure.lock.support.DistributedLock;
import com.livk.commons.spring.spel.SpringExpressionResolver;
import lombok.RequiredArgsConstructor;
import org.aopalliance.intercept.MethodInvocation;
import org.springframework.aop.Pointcut;
import org.springframework.aop.support.annotation.AnnotationMatchingPointcut;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.util.Assert;

/**
 * <p>
 * LockAspect
 * </p>
 *
 * @author livk
 */
@RequiredArgsConstructor
public class LockInterceptor extends AnnotationAbstractPointcutAdvisor<OnLock> {
    /**
     * lock的实现类集合
     */
    private final ObjectProvider<DistributedLock> distributedLockProvider;

    /**
     * SpEL表达式解析器
     */
    private final SpringExpressionResolver resolver = new SpringExpressionResolver();

    @Override
    protected Object invoke(MethodInvocation invocation, OnLock onLock) throws Throwable {
        Assert.notNull(onLock, "lock is null");
        LockScope scope = onLock.scope();
        DistributedLock distributedLock = distributedLockProvider.orderedStream()
                .filter(lock -> lock.scope().equals(scope))
                .findFirst()
                .orElseThrow(() -> new UnSupportLockException("缺少scope：" + scope + "的锁实现"));
        boolean async = !LockScope.STANDALONE_LOCK.equals(scope) && onLock.async();
        String key = resolver.evaluate(onLock.key(), invocation.getMethod(), invocation.getArguments());
        boolean isLock = distributedLock.tryLock(onLock.type(), key, onLock.leaseTime(), onLock.waitTime(), async);
        try {
            if (isLock) {
                return invocation.proceed();
            }
            throw new LockException("获取锁失败!");
        } finally {
            if (isLock) {
                distributedLock.unlock();
            }
        }
    }

    @Override
    public Pointcut getPointcut() {
        return AnnotationMatchingPointcut.forMethodAnnotation(annotationType);
    }
}
