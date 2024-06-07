package com.springbatch.__SpringBatch_ETL.faulttolrence;

import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.step.skip.SkipLimitExceededException;
import org.springframework.batch.core.step.skip.SkipPolicy;
import org.springframework.batch.item.file.FlatFileParseException;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class ExceptionSkipPolicy implements SkipPolicy {
  @Override
  public boolean shouldSkip(Throwable throwable, int skipCount) throws SkipLimitExceededException {
    if (throwable instanceof NumberFormatException) return true;
    if (throwable instanceof FlatFileParseException) return true;
    return throwable instanceof NullPointerException;
  }
}
