/*
 * Copyright 2021-2024 spring-boot-extension the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.livk.quartz.controller;

import com.livk.quartz.config.QuartzScheduler;
import lombok.RequiredArgsConstructor;
import org.quartz.CronScheduleBuilder;
import org.quartz.CronTrigger;
import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.SchedulerException;
import org.quartz.TriggerBuilder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

/**
 * <p>
 * QuartzController
 * </p>
 *
 * @author livk
 */
@RestController
@RequiredArgsConstructor
public class QuartzController {

	private final QuartzScheduler quartzScheduler;

	@PostMapping("livkTask")
	public Mono<Void> livkTask() throws SchedulerException {
		JobDetail jobDetail = JobBuilder.newJob(com.livk.quartz.scheduler.QuartzScheduler.class)
			.withIdentity("job2", "group2")
			.build();
		jobDetail.getJobDataMap().put("user", "tom2");
		CronScheduleBuilder cronScheduleBuilder = CronScheduleBuilder.cronSchedule("0/10 * * * * ?");
		CronTrigger cronTrigger = TriggerBuilder.newTrigger()
			.withIdentity("job2", "group2")
			.withSchedule(cronScheduleBuilder)
			.build();
		quartzScheduler.scheduleJob(jobDetail, cronTrigger);
		return Mono.empty();
	}

}
