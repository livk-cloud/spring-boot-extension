package com.livk.batch.controller;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import java.util.Date;

/**
 * <p>
 * JobController
 * </p>
 *
 * @author livk
 * @date 2021/12/27
 */
@RestController
@RequiredArgsConstructor
public class JobController {

    private final JobLauncher jobLauncher;

    private final Job job;

    @SneakyThrows
    @GetMapping("doJob")
    public Mono<Void> doJob() {
        var builder = new JobParametersBuilder();
        builder.addDate("jobDate", new Date());
        jobLauncher.run(job, builder.toJobParameters());
        return Mono.empty();
    }

}
