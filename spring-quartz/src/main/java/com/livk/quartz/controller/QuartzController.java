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
 */
@RestController
@RequiredArgsConstructor
public class QuartzController {

    private final LivkQuartzScheduler livkQuartzScheduler;

    @PostMapping("livkTask")
    public Mono<Void> livkTask() throws SchedulerException {
        JobDetail jobDetail = JobBuilder.newJob(QuartzScheduler.class).withIdentity("job2", "group2").build();
        jobDetail.getJobDataMap().put("user", "tom2");
        CronScheduleBuilder cronScheduleBuilder = CronScheduleBuilder.cronSchedule("0/10 * * * * ?");
        CronTrigger cronTrigger = TriggerBuilder.newTrigger().withIdentity("job2", "group2").withSchedule(cronScheduleBuilder)
                .build();
        livkQuartzScheduler.scheduleJob(jobDetail, cronTrigger);
        return Mono.empty();
    }

}
