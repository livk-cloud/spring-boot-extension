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

package com.livk.quartz.config;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.quartz.CronScheduleBuilder;
import org.quartz.CronTrigger;
import org.quartz.JobDetail;
import org.quartz.JobKey;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.quartz.TriggerKey;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * <p>
 * LivkQuartzScheduler
 * </p>
 *
 * @author livk
 */
@Component
@RequiredArgsConstructor
public class QuartzScheduler {

	private final Scheduler scheduler;

	@PostConstruct
	public void startJob() throws SchedulerException {
		scheduler.start();
	}

	public boolean modifyJob(String name, String group, String time) throws SchedulerException {
		Date date = null;
		TriggerKey triggerKey = new TriggerKey(name, group);
		Trigger trigger = scheduler.getTrigger(triggerKey);
		if (trigger instanceof CronTrigger cronTrigger) {
			String oldTime = cronTrigger.getCronExpression();
			if (!oldTime.equalsIgnoreCase(time)) {
				CronScheduleBuilder cronScheduleBuilder = CronScheduleBuilder.cronSchedule(time);
				CronTrigger build = TriggerBuilder.newTrigger()
					.withIdentity(name, group)
					.withSchedule(cronScheduleBuilder)
					.build();
				date = scheduler.rescheduleJob(triggerKey, build);
			}
		}
		return date != null;
	}

	public void pauseAllJob() throws SchedulerException {
		scheduler.pauseAll();
	}

	public void pauseJob(String name, String group) throws SchedulerException {
		JobKey jobKey = new JobKey(name, group);
		JobDetail jobDetail = scheduler.getJobDetail(jobKey);
		if (jobDetail != null) {
			scheduler.pauseJob(jobKey);
		}
	}

	public void resumeAllJob() throws SchedulerException {
		scheduler.resumeAll();
	}

	public void resumeJob(String name, String group) throws SchedulerException {
		JobKey jobKey = new JobKey(name, group);
		JobDetail jobDetail = scheduler.getJobDetail(jobKey);
		if (jobDetail != null) {
			scheduler.resumeJob(jobKey);
		}
	}

	public void deleteJob(String name, String group) throws SchedulerException {
		JobKey jobKey = new JobKey(name, group);
		JobDetail jobDetail = scheduler.getJobDetail(jobKey);
		if (jobDetail != null) {
			scheduler.deleteJob(jobKey);
		}
	}

	public void scheduleJob(JobDetail jobDetail, Trigger trigger) throws SchedulerException {
		scheduler.scheduleJob(jobDetail, trigger);
	}

}
