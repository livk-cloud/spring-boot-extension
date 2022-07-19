package com.livk.excel.listener;

import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobExecutionListener;

/**
 * <p>
 * JobListener
 * </p>
 *
 * @author livk
 * @date 2022/7/19
 */
@Slf4j
public class JobListener implements JobExecutionListener {

    @Override
    public void beforeJob(JobExecution jobExecution) {
        log.info("任务id={}开始于{}", jobExecution.getJobId(), jobExecution.getStartTime());
    }

    @Override
    public void afterJob(JobExecution jobExecution) {
        if (jobExecution.getStatus() == BatchStatus.COMPLETED) {
            log.info("任务id={}结束于{}", jobExecution.getJobId(), jobExecution.getEndTime());
        } else {
            log.info("任务id={}执行异常状态={}", jobExecution.getJobId(), jobExecution.getStatus());
        }
    }
}
