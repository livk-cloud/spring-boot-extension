/*
 * Copyright 2021-present the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.livk.excel.batch.controller;

import com.google.common.collect.Lists;
import com.livk.context.fastexcel.annotation.ExcelParam;
import com.livk.context.fastexcel.annotation.RequestExcel;
import com.livk.context.fastexcel.listener.TypeExcelMapReadListener;
import com.livk.excel.batch.entity.Info;
import com.livk.excel.batch.listener.InfoExcelListener;
import com.livk.excel.batch.listener.JobListener;
import com.livk.excel.batch.support.FastExcelItemReader;
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
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * @author livk
 */
@RestController
@RequiredArgsConstructor
public class InfoController {

	private final ItemWriter<List<Info>> writer;

	private final JobRepository jobRepository;

	private final DataSourceTransactionManager dataSourceTransactionManager;

	private final JobLauncher jobLauncher;

	@RequestExcel(parse = InfoExcelListener.class)
	@PostMapping("upload")
	public HttpEntity<Void> upload(@ExcelParam List<Info> dataExcels) throws JobInstanceAlreadyCompleteException,
			JobExecutionAlreadyRunningException, JobParametersInvalidException, JobRestartException {
		Step step = excelStep(dataExcels);
		Job job = excelJob(step);
		jobLauncher.run(job,
				new JobParametersBuilder().addLocalDateTime("date", LocalDateTime.now()).toJobParameters());
		return ResponseEntity.ok(null);
	}

	@PostMapping("excel")
	public HttpEntity<List<Info>> up(@RequestParam("file") MultipartFile file) throws IOException {
		FastExcelItemReader<Info> itemReader = new FastExcelItemReader<>(file.getInputStream(),
				new TypeExcelMapReadListener<>() {
				});
		List<Info> list = new ArrayList<>();
		while (true) {
			Info o = itemReader.read();
			if (o != null) {
				o.setId(System.currentTimeMillis());
				list.add(o);
			}
			else {
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
		return new JobBuilder("excelJob", jobRepository).start(step).listener(new JobListener()).build();
	}

}
