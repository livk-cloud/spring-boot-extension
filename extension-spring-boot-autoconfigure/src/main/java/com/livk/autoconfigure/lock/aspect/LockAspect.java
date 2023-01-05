package com.livk.autoconfigure.lock.aspect;

import com.livk.autoconfigure.lock.annotation.OnLock;
import com.livk.autoconfigure.lock.constant.LockScope;
import com.livk.autoconfigure.lock.exception.LockException;
import com.livk.autoconfigure.lock.exception.UnSupportLockException;
import com.livk.autoconfigure.lock.support.DistributedLock;
import com.livk.commons.spring.SpringUtils;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.util.Assert;

import java.lang.reflect.Method;
import java.util.Comparator;

/**
 * <p>
 * LockAspect
 * </p>
 *
 * @author livk
 */
@Aspect
@RequiredArgsConstructor
public class LockAspect {

    private final ObjectProvider<DistributedLock> distributedLockProvider;

    @Around("@annotation(onLock)")
    public Object around(ProceedingJoinPoint joinPoint, OnLock onLock) throws Throwable {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();
        if (onLock == null) {
            onLock = AnnotationUtils.findAnnotation(method, OnLock.class);
        }
        Assert.notNull(onLock, "lock is null");
        LockScope scope = onLock.scope();
        DistributedLock distributedLock = distributedLockProvider.stream()
                .filter(lock -> lock.scope().equals(scope))
                .min(Comparator.comparingInt(DistributedLock::getOrder))
                .orElseThrow(() -> new UnSupportLockException("缺少scope：" + scope + "的锁实现"));
        boolean async = !LockScope.STANDALONE_LOCK.equals(scope) && onLock.async();
        String key = SpringUtils.parse(method, joinPoint.getArgs(), onLock.key());
        boolean isLock = distributedLock.tryLock(onLock.type(), key, onLock.leaseTime(), onLock.waitTime(), async);
        try {
            if (isLock) {
                return joinPoint.proceed();
            }
            throw new LockException("获取锁失败!");
        } finally {
            if (isLock) {
                distributedLock.unlock();
            }
        }
    }
}
