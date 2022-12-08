package com.livk.quartz.scheduler;

import lombok.extern.slf4j.Slf4j;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.springframework.scheduling.quartz.QuartzJobBean;

/**
 * <p>
 * QuartzScheduler
 * </p>
 *
 * @author livk
 */
@Slf4j
public class QuartzScheduler extends QuartzJobBean {

    private void before() {
        log.info("before");
    }

    @Override
    protected void executeInternal(JobExecutionContext context) {
        before();
        JobDataMap jobDataMap = context.getJobDetail().getJobDataMap();
        log.info("{}", jobDataMap.getString("user"));
        after();
    }

    private void after() {
        log.info("after");
    }

}
