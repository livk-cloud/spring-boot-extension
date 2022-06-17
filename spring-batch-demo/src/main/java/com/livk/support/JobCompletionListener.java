package com.livk.support;

import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobExecutionListener;
import org.springframework.stereotype.Component;

/**
 * <p>
 * JobCompletionListener
 * </p>
 *
 * @author livk
 * @date 2021/12/27
 */
@Slf4j
@Component
public class JobCompletionListener implements JobExecutionListener {

	// 用于批处理开始前执行
	@Override
	public void beforeJob(JobExecution jobExecution) {
		log.info("任务id={}开始于{}", jobExecution.getJobId(), jobExecution.getStartTime());
	}

	// 用于批处理开始后执行
	@Override
	public void afterJob(JobExecution jobExecution) {
		if (jobExecution.getStatus() == BatchStatus.COMPLETED) {
			log.info("任务id={}结束于{}", jobExecution.getJobId(), jobExecution.getEndTime());
		} else {
			log.info("任务id={}执行异常状态={}", jobExecution.getJobId(), jobExecution.getStatus());
		}
	}

}
