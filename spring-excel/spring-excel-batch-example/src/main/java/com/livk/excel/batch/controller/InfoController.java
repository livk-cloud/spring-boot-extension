package com.livk.excel.batch.controller;

import com.google.common.collect.Lists;
import com.livk.autoconfigure.excel.annotation.ExcelImport;
import com.livk.autoconfigure.excel.listener.TypeExcelReadListener;
import com.livk.excel.batch.entity.Info;
import com.livk.excel.batch.listener.InfoExcelListener;
import com.livk.excel.batch.listener.JobListener;
import com.livk.excel.batch.support.EasyExcelItemReader;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.support.ListItemReader;
import org.springframework.batch.repeat.policy.SimpleCompletionPolicy;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * <p>
 * InfoController
 * </p>
 *
 * @author livk
 */
@RestController
@RequiredArgsConstructor
public class InfoController {

    private final ItemWriter<List<Info>> writer;

    private final JobRepository jobRepository;

    private final DataSourceTransactionManager dataSourceTransactionManager;
    private final JobLauncher jobLauncher;

    @ExcelImport(parse = InfoExcelListener.class, paramName = "dataExcels")
    @PostMapping("upload")
    public HttpEntity<Void> upload(List<Info> dataExcels) throws JobInstanceAlreadyCompleteException, JobExecutionAlreadyRunningException, JobParametersInvalidException, JobRestartException {
        Step step = excelStep(dataExcels);
        Job job = excelJob(step);
        jobLauncher.run(job, new JobParametersBuilder()
                .addDate("date", new Date())
                .toJobParameters());
        return ResponseEntity.ok(null);
    }

    @PostMapping("excel")
    public HttpEntity<List<Info>> up(@RequestParam("file") MultipartFile file) throws IOException {
        EasyExcelItemReader<Info> itemReader = new EasyExcelItemReader<>(file.getInputStream(), new TypeExcelReadListener<>() {
        });
        List<Info> list = new ArrayList<>();
        while (true) {
            Info o = itemReader.read();
            if (o != null) {
                o.setId(System.currentTimeMillis());
                list.add(o);
            } else {
                break;
            }
        }
        return ResponseEntity.ok(list);
    }

    private Step excelStep(List<Info> dataExcels) {
        return new StepBuilder("excelStep", jobRepository)
                .<List<Info>, List<Info>>chunk(new SimpleCompletionPolicy(), dataSourceTransactionManager)
                .reader(new ListItemReader<>(Lists.partition(dataExcels, 1000)))
                .writer(writer)
                .faultTolerant()
                .skipLimit(0)
                .build();
    }

    public Job excelJob(Step step) {
        return new JobBuilder("excelJob", jobRepository)
                .start(step)
                .listener(new JobListener())
                .build();
    }
}
