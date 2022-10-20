package com.livk.lock.aspect;

import com.livk.lock.annotation.OnLock;
import com.livk.lock.constant.LockScope;
import com.livk.lock.exception.LockException;
import com.livk.lock.exception.UnSupportLockException;
import com.livk.lock.support.DistributedLock;
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
 * @date 2022/9/5
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
        boolean isLock = distributedLock.tryLock(onLock.type(), onLock.key(), onLock.leaseTime(), onLock.waitTime(), onLock.async());
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
