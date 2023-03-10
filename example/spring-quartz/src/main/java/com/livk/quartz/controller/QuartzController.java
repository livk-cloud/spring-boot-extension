package com.livk.quartz.controller;

import com.livk.quartz.config.QuartzScheduler;
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
 */
@RestController
@RequiredArgsConstructor
public class QuartzController {

    private final QuartzScheduler quartzScheduler;

    @PostMapping("livkTask")
    public Mono<Void> livkTask() throws SchedulerException {
        JobDetail jobDetail = JobBuilder.newJob(com.livk.quartz.scheduler.QuartzScheduler.class).withIdentity("job2", "group2").build();
        jobDetail.getJobDataMap().put("user", "tom2");
        CronScheduleBuilder cronScheduleBuilder = CronScheduleBuilder.cronSchedule("0/10 * * * * ?");
        CronTrigger cronTrigger = TriggerBuilder.newTrigger().withIdentity("job2", "group2").withSchedule(cronScheduleBuilder)
                .build();
        quartzScheduler.scheduleJob(jobDetail, cronTrigger);
        return Mono.empty();
    }

}
