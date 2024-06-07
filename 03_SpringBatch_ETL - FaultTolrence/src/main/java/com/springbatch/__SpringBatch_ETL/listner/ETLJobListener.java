package com.springbatch.__SpringBatch_ETL.listner;

import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobExecutionListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class ETLJobListener implements JobExecutionListener {
  @Override
  public void beforeJob(JobExecution jobExecution) {
    log.info("ETL Job {} started at ", jobExecution.getStartTime());
  }

  @Override
  public void afterJob(JobExecution jobExecution) {
    log.info("ETL Job ended at {} ", jobExecution.getEndTime());
    log.info(
        "Job completed with status {} and with details {}",
        jobExecution.getExitStatus(),
        jobExecution.toString());
  }
}
