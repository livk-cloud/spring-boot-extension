package com.livk.quartz.config;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.lang.Nullable;

/**
 * <p>
 * ApplicationStartQuartzJobListener
 * </p>
 *
 * @author livk
 * @date 2021/10/25
 */
@Slf4j
@Configuration
@RequiredArgsConstructor
public class ApplicationStartQuartzJobListener implements ApplicationListener<ContextRefreshedEvent> {

	private final LivkQuartzScheduler livkQuartzScheduler;

	@SneakyThrows
	@Override
	public void onApplicationEvent(@Nullable ContextRefreshedEvent event) {
		livkQuartzScheduler.startJob();
		log.info("job is start！");
	}

}
