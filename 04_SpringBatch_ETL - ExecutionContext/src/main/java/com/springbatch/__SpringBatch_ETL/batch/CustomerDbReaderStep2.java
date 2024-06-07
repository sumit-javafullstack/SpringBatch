package com.springbatch.__SpringBatch_ETL.batch;

import com.springbatch.__SpringBatch_ETL.model.Customer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.annotation.BeforeStep;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemStream;
import org.springframework.batch.item.ItemStreamException;
import org.springframework.batch.item.database.JdbcCursorItemReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.BeanPropertyRowMapper;

import javax.sql.DataSource;

@Slf4j
public class CustomerDbReaderStep2 implements ItemReader<Customer>, ItemStream {

  private JdbcCursorItemReader<Customer> reader = null;

  private boolean endOfResult = false;

  private int counter = 0;

  private final String QUERY = "Select * from Customer"; // where age> ? and salary > ?

  @Autowired
  @Qualifier("dataSource")
  DataSource dataSource;

  @BeforeStep
  public void beforeStep() {

    this.reader = new JdbcCursorItemReader<>();
    this.reader.setDataSource(dataSource);
    this.reader.setFetchSize(500);
    this.reader.setSql(QUERY);
    // reader.setPreparedStatementSetter(new ArgumentPreparedStatementSetter(new Object[]{11,22}));,
    // ?,?
    this.reader.setRowMapper(new BeanPropertyRowMapper<>(Customer.class));
    counter++;
  }

  @Override
  public Customer read() throws Exception {

    return this.reader.read();
  }

  @Override
  public void open(ExecutionContext executionContext) throws ItemStreamException {

    this.reader.open(executionContext);
    log.info("retrieving values from step1 {} ",executionContext.getString("counter"));
  }

  @Override
  public void update(ExecutionContext executionContext) throws ItemStreamException {}

  @Override
  public void close() throws ItemStreamException {

    log.info("counter " + counter);
  }
}
