package com.springbatch.__SpringBatch_ETL.batch;

import com.springbatch.__SpringBatch_ETL.model.Customer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.annotation.BeforeStep;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.batch.item.ItemStream;
import org.springframework.batch.item.ItemStreamException;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.file.FlatFileItemWriter;
import org.springframework.batch.item.file.transform.BeanWrapperFieldExtractor;
import org.springframework.batch.item.file.transform.DelimitedLineAggregator;
import org.springframework.batch.item.file.transform.FieldExtractor;
import org.springframework.batch.item.file.transform.LineAggregator;
import org.springframework.core.io.FileSystemResource;

import java.util.List;

@Slf4j
public class CustomerDbWriterStep2 implements ItemWriter<Customer>, ItemStream {

  private String fileName = "C:\\Users\\HP\\Pictures\\Customer1.csv";
  FlatFileItemWriter<Customer> writer = new FlatFileItemWriter<>();

  @BeforeStep
  public void beforeStep(){
    writer.setLineAggregator(lineAggregator());
    writer.setResource(new FileSystemResource(fileName));
    writer.setAppendAllowed(true);
    writer.setShouldDeleteIfEmpty(true);
  }
  @Override
  public void write(List<? extends Customer> list) throws Exception {
    log.info("Process entered into writer");
    writer.write( list);

  }

  private LineAggregator<Customer> lineAggregator() {
    DelimitedLineAggregator<Customer> lineAggregator = new DelimitedLineAggregator<>();
    lineAggregator.setDelimiter(",");
    lineAggregator.setFieldExtractor(fieldExtractor());
    return lineAggregator;
  }

  private FieldExtractor<Customer> fieldExtractor() {
    BeanWrapperFieldExtractor<Customer> wrapper = new BeanWrapperFieldExtractor<>();
    wrapper.setNames(
        new String[] {
          "id", "occupation", "name", "custId", "salary", "state", "designation", "efficiency"
        });
    return wrapper;
  }

  @Override
  public void open(ExecutionContext executionContext) throws ItemStreamException {
    writer.open(executionContext);
  }

  @Override
  public void update(ExecutionContext executionContext) throws ItemStreamException {
    this.writer.update(executionContext);
  }

  @Override
  public void close() throws ItemStreamException {
    writer.close();
  }
}
