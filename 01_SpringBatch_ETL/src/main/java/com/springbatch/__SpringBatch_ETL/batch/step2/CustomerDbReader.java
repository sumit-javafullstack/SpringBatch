package com.springbatch.__SpringBatch_ETL.batch.step2;

import com.springbatch.__SpringBatch_ETL.model.Customer;
import com.springbatch.__SpringBatch_ETL.properties.PropertiesFile;
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
public class CustomerDbReader implements ItemReader<Customer>,ItemStream {

  private JdbcCursorItemReader<Customer> jdbcReader = null;

  @Autowired
  @Qualifier("dataSource")
  DataSource dataSource;

  @Autowired PropertiesFile properties;

 // private final String QUERY = "Select * from Customer";

  @BeforeStep
  public void beforeStep()  {
    this.jdbcReader = new JdbcCursorItemReader<>();
    this.jdbcReader.setSql(properties.getQuery());
    this.jdbcReader.setDataSource(dataSource);
    // reader.setPreparedStatementSetter(new ArgumentPreparedStatementSetter(new Object[]{11,22}));,
    // ?,? for dynamic values in the queries
    this.jdbcReader.setFetchSize(10);
    this.jdbcReader.setRowMapper(new BeanPropertyRowMapper<>(Customer.class));
  }

  @Override
  public Customer read() throws Exception {
    return this.jdbcReader.read();
  }

  @Override
  public void open(ExecutionContext executionContext) throws ItemStreamException {

    this.jdbcReader.open(executionContext);
  }

  @Override
  public void update(ExecutionContext executionContext) throws ItemStreamException {}

  @Override
  public void close() throws ItemStreamException {
    this.jdbcReader.close();
  }
}
