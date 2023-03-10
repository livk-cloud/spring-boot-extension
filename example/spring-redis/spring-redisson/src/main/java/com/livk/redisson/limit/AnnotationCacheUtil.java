package com.livk.redisson.limit;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.livk.commons.bean.domain.Pair;
import lombok.experimental.UtilityClass;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.lang.Nullable;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.concurrent.TimeUnit;

/**
 * <p>
 * AnnotationCacheUtil
 * </p>
 *
 * @author livk
 */
@UtilityClass
public class AnnotationCacheUtil {

    private static final Cache<Pair<Method, Class<? extends Annotation>>, Integer> caffeineCache = Caffeine.newBuilder()
            .initialCapacity(256)
            .maximumSize(1024)
            .expireAfterWrite(60, TimeUnit.MINUTES)
            .build();

    public <A extends Annotation> A findAnnotation(Method method, @Nullable Class<A> annotationType) {
        Integer flag = caffeineCache.getIfPresent(Pair.of(method, annotationType));
        if (flag == null) {
            A annotation = AnnotationUtils.findAnnotation(method, annotationType);
            if (annotation == null) {
                caffeineCache.put(Pair.of(method, annotationType), 1);
            }
            return annotation;
        }
        return null;
    }
}
