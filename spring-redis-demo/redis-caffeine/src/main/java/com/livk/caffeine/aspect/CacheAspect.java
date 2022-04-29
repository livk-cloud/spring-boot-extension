package com.livk.caffeine.aspect;

import com.livk.caffeine.annotation.DoubleCache;
import com.livk.caffeine.handler.CacheHandlerAdapter;
import com.livk.caffeine.util.SpelUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import java.util.TreeMap;

/**
 * <p>
 * CacheAspect
 * </p>
 *
 * @author livk
 * @date 2022/3/30
 */
@Slf4j
@Aspect
@Component
@RequiredArgsConstructor
public class CacheAspect {

	private final CacheHandlerAdapter adapter;

	@Around("@annotation(doubleCache)")
	public Object around(ProceedingJoinPoint point, DoubleCache doubleCache) throws Throwable {
		var signature = (MethodSignature) point.getSignature();
		var method = signature.getMethod();
		if (doubleCache == null) {
			doubleCache = AnnotationUtils.findAnnotation(method, DoubleCache.class);
		}
		Assert.notNull(doubleCache, "doubleCache is null");
		var paramNames = signature.getParameterNames();
		var treeMap = new TreeMap<String, Object>();
		for (var i = 0; i < paramNames.length; i++) {
			treeMap.put(paramNames[i], point.getArgs()[i]);
		}
		var spelResult = SpelUtils.parse(doubleCache.key(), treeMap);
		var realKey = doubleCache.cacheName() + ":" + spelResult;
		switch (doubleCache.type()) {
		case FULL -> {
			return adapter.readAndPut(realKey, point, doubleCache.timeOut());
		}
		case PUT -> {
			var proceed = point.proceed();
			adapter.put(realKey, proceed, doubleCache.timeOut());
			return proceed;
		}
		case DELETE -> {
			var proceed = point.proceed();
			adapter.delete(realKey);
			return proceed;
		}
		}
		return point.proceed();
	}

}
