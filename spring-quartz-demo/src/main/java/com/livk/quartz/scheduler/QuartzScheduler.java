package com.livk.quartz.scheduler;

import lombok.extern.slf4j.Slf4j;
import org.quartz.Job;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;

/**
 * <p>
 * QuartzScheduler
 * </p>
 *
 * @author livk
 * @date 2021/10/25
 */
@Slf4j
public class QuartzScheduler implements Job {

	private void before() {
		log.info("before");
	}

	@Override
	public void execute(JobExecutionContext jobExecutionContext) {
		before();
		var jobDataMap = jobExecutionContext.getJobDetail().getJobDataMap();
		log.info("{}", jobDataMap.get("user"));
		after();
	}

	private void after() {
		log.info("after");
	}

}
