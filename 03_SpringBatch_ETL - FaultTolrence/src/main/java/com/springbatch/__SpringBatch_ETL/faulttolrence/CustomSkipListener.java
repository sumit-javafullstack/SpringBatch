package com.springbatch.__SpringBatch_ETL.faulttolrence;

import com.springbatch.__SpringBatch_ETL.config.PropertyConfig;
import com.springbatch.__SpringBatch_ETL.model.Customer;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.SkipListener;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.batch.item.ItemStream;
import org.springframework.batch.item.ItemStreamException;
import org.springframework.batch.item.file.FlatFileItemWriter;
import org.springframework.batch.item.file.transform.PassThroughLineAggregator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.FileSystemResource;
import org.springframework.stereotype.Component;

import java.util.Collections;

@Component
@Slf4j
public class CustomSkipListener implements SkipListener<Object, Object> {

  private FlatFileItemWriter<Object> writer = new FlatFileItemWriter<>();

  @Autowired private PropertyConfig properties;

  @SneakyThrows
  @Override
  public void onSkipInRead(Throwable t) {
    log.info("Skipped in Read {} with error at {}", t.toString(), t.getCause());

    writeErrorFile(t.getMessage());
  }

  @SneakyThrows
  @Override
  public void onSkipInWrite(Object item, Throwable t) {
    log.info("Skipped in write : item {} ErrorMessage: {}", item, t.getMessage());
    writeErrorFile(item);
  }

  @SneakyThrows
  @Override
  public void onSkipInProcess(Object item, Throwable t) {
    log.info("Skipped in process : item {} ErrorMessage: {}", item, t.getMessage());
    writeErrorFile(item);
  }

  public void writeErrorFile(Object item) throws Exception {

    writer.setResource(new FileSystemResource(properties.getErrorFile()));
    writer.setAppendAllowed(true);
    writer.setLineAggregator(new PassThroughLineAggregator<>());
    writer.open(new ExecutionContext());
    writer.write(Collections.singletonList(item));
  }
}
