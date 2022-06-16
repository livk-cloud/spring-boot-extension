package com.livk.common;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.ApplicationEvent;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;

/**
 * <p>
 * SpringContextHolder
 * </p>
 *
 * @author livk
 * @date 2021/10/22
 */
@Slf4j
@Component
public class SpringContextHolder implements ApplicationContextAware, DisposableBean {

	@Getter
	private static ApplicationContext applicationContext = null;

	/**
	 * Spring事件发布
	 * @param event 事件
	 */
	public static void publishEvent(ApplicationEvent event) {
		applicationContext.publishEvent(event);
	}

	@SuppressWarnings("unchecked")
	public static <T> T getBean(String name) {
		return (T) applicationContext.getBean(name);
	}

	public static <T> T getBean(Class<T> typeClass) {
		return applicationContext.getBean(typeClass);
	}

	public static <T> T getBean(String name, Class<T> typeClass) {
		return applicationContext.getBean(name, typeClass);
	}

	@Override
	public void setApplicationContext(@Nullable ApplicationContext applicationContext) throws BeansException {
		if (SpringContextHolder.applicationContext != null) {
			log.warn("SpringContextHolder中的ApplicationContext被覆盖, 原有ApplicationContext为:{}",
					SpringContextHolder.applicationContext);
		}
		synchronized (SpringContextHolder.class) {
			SpringContextHolder.applicationContext = applicationContext;
		}
	}

	@Override
	public void destroy() {
		if (log.isDebugEnabled()) {
			log.debug("清除SpringContextHolder中的ApplicationContext:{}", applicationContext);
		}
		synchronized (SpringContextHolder.class) {
			SpringContextHolder.applicationContext = null;
		}
	}

}
