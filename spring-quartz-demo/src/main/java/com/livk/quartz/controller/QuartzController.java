package com.livk.quartz.controller;

import com.livk.quartz.config.LivkQuartzScheduler;
import com.livk.quartz.scheduler.QuartzScheduler;
import lombok.RequiredArgsConstructor;
import org.quartz.*;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

/**
 * <p>
 * QuartzController
 * </p>
 *
 * @author livk
 * @date 2021/10/25
 */
@RestController
@RequiredArgsConstructor
public class QuartzController {

	private final LivkQuartzScheduler livkQuartzScheduler;

	@PostMapping("livkTask")
	public Mono<Void> livkTask() throws SchedulerException {
		var jobDetail = JobBuilder.newJob(QuartzScheduler.class).withIdentity("job2", "group2").build();
		jobDetail.getJobDataMap().put("user", "tom2");
		var cronScheduleBuilder = CronScheduleBuilder.cronSchedule("0/10 * * * * ?");
		var cronTrigger = TriggerBuilder.newTrigger().withIdentity("job2", "group2").withSchedule(cronScheduleBuilder)
				.build();
		livkQuartzScheduler.scheduleJob(jobDetail, cronTrigger);
		return Mono.empty();
	}

}
