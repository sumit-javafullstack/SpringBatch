package com.springbatch.__SpringBatch_ETL.faulttolrence;

import com.springbatch.__SpringBatch_ETL.model.Customer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.SkipListener;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class CustomSkipListener implements SkipListener<Object, Object>{
  @Override
  public void onSkipInRead(Throwable t) {
    log.info("Skipped in Read {} with error at {}", t.toString(),t.getCause());
  }

  @Override
  public void onSkipInWrite(Object item, Throwable t) {
    log.info("Skipped in write : item {} ErrorMessage: {}", item, t.getMessage());
  }

  @Override
  public void onSkipInProcess(Object item, Throwable t) {
    log.info("Skipped in process : item {} ErrorMessage: {}", item, t.getMessage());
  }


}
