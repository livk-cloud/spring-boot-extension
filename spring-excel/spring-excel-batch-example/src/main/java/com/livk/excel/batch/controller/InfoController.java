package com.livk.excel.batch.controller;

import com.google.common.collect.Lists;
import com.livk.excel.annotation.ExcelImport;
import com.livk.excel.batch.entity.Info;
import com.livk.excel.batch.listener.InfoExcelListener;
import com.livk.excel.batch.listener.JobListener;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.support.ListItemReader;
import org.springframework.batch.repeat.policy.SimpleCompletionPolicy;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.List;

/**
 * <p>
 * InfoController
 * </p>
 *
 * @author livk
 * @date 2022/1/12
 */
@RestController
@RequiredArgsConstructor
public class InfoController {

    private final ItemWriter<List<Info>> writer;
    private final StepBuilderFactory stepBuilderFactory;
    private final JobBuilderFactory jobBuilderFactory;
    private final JobLauncher jobLauncher;

    @ExcelImport(parse = InfoExcelListener.class, paramName = "dataExcels")
    @PostMapping("upload")
    public HttpEntity<Void> upload(List<Info> dataExcels) throws JobInstanceAlreadyCompleteException, JobExecutionAlreadyRunningException, JobParametersInvalidException, JobRestartException {
        var step = excelStep(dataExcels);
        var job = excelJob(step);
        jobLauncher.run(job, new JobParametersBuilder()
                .addDate("date", new Date())
                .toJobParameters());
        return ResponseEntity.ok(null);
    }

    private Step excelStep(List<Info> dataExcels) {
        return stepBuilderFactory.get("excelStep")
                .<List<Info>, List<Info>>chunk(new SimpleCompletionPolicy())
                .reader(new ListItemReader<>(Lists.partition(dataExcels, 1000)))
                .writer(writer)
                .faultTolerant()
                .skipLimit(0)
                .build();
    }

    public Job excelJob(Step step) {
        return jobBuilderFactory.get("excelJob")
                .start(step)
                .listener(new JobListener())
                .build();
    }
}
