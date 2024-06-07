package com.springbatch.__SpringBatch_ETL.listner;

import com.springbatch.__SpringBatch_ETL.config.PropertyConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.StepExecutionListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.File;
import java.time.LocalDateTime;

@Slf4j
@Component
public class LoadDbStepListener implements StepExecutionListener {

  @Autowired
  private PropertyConfig properties;

  @Override
  public void beforeStep(StepExecution stepExecution) {
    //  can be use for task like tasks like setting up resources, logging, or cleaning up.
    log.info("{} started at {}", stepExecution.getStepName(), stepExecution.getStartTime());

    File file = new File(properties.getErrorFile());
    if(file.exists()){
      file.delete();
    }
  }

  @Override
  public ExitStatus afterStep(StepExecution stepExecution) {
    log.info(
        "Step Name: {} completed at {}", stepExecution.getStepName(), LocalDateTime.now());
    log.info(
        "Step Name: {} with StepExecution details {}",
        stepExecution.getStepName(),
        stepExecution.toString());

    return null;
  }
}
