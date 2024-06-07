package com.springbatch.__SpringBatch_ETL.batch;

import com.springbatch.__SpringBatch_ETL.model.Customer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.annotation.BeforeStep;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemStream;
import org.springframework.batch.item.ItemStreamException;
import org.springframework.batch.item.NonTransientResourceException;
import org.springframework.batch.item.ParseException;
import org.springframework.batch.item.UnexpectedInputException;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.LineMapper;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.core.io.ClassPathResource;

@Slf4j
public class CustomerReader implements ItemReader<Customer>, ItemStream {

  private long counter = 0L;
  private boolean endOfResultSet = false;
  FlatFileItemReader<Customer> itemReader = null;

  @BeforeStep
  public void beforeStep() {
    itemReader = new FlatFileItemReader<>();

    // itemReader.setResource(new ClassPathResource("Customer.csv")); to load from resource
    itemReader.setResource(new ClassPathResource("Customer.csv"));
    itemReader.setName("CustomerCsvReader");
    // itemReader.setLinesToSkip(1);
    itemReader.setLineMapper(lineMapper());// converting rows data to java object
    log.info("{} has been read successfully", itemReader.getName());
  }

  private LineMapper<Customer> lineMapper() {
    DefaultLineMapper<Customer> defaultLineMapper = new DefaultLineMapper<>();
    DelimitedLineTokenizer delimitedLineTokenizer = new DelimitedLineTokenizer();
    delimitedLineTokenizer.setDelimiter(",");
    delimitedLineTokenizer.setStrict(false);
    delimitedLineTokenizer.setNames(
        "id", "occupation", "name", "custId", "salary", "state", "designation", "efficiency");

    BeanWrapperFieldSetMapper<Customer> beanWrapperFieldSetMapper =
        new BeanWrapperFieldSetMapper<>();
    beanWrapperFieldSetMapper.setTargetType(Customer.class);
    defaultLineMapper.setLineTokenizer(delimitedLineTokenizer);
    defaultLineMapper.setFieldSetMapper(beanWrapperFieldSetMapper);

    return defaultLineMapper;
  }

  @Override
  public  Customer read()
      throws Exception, UnexpectedInputException, ParseException, NonTransientResourceException {

    if (this.endOfResultSet) {
      return null;
    }

    Customer user = this.itemReader.read();
    if (user == null) {
      endOfResultSet = true;
    } else {
      counter++;
    }
    return user;
  }

  @Override
  public void open(ExecutionContext executionContext) throws ItemStreamException {
    itemReader.open(new ExecutionContext());
  }

  @Override
  public void update(ExecutionContext executionContext) throws ItemStreamException {

  }

  @Override
  public void close() throws ItemStreamException {

  }
}
