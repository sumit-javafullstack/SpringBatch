package com.springbatch.__SpringBatch_ETL.config;

import com.springbatch.__SpringBatch_ETL.batch.step1.CustomerProcessor;
import com.springbatch.__SpringBatch_ETL.batch.step1.CustomerCsvReader;
import com.springbatch.__SpringBatch_ETL.batch.step1.CustomerDbWriter;
import com.springbatch.__SpringBatch_ETL.batch.step2.CustomerDatWriter1;
import com.springbatch.__SpringBatch_ETL.batch.step2.CustomerDatWriter2;
import com.springbatch.__SpringBatch_ETL.batch.step2.CustomerDbReader;
import com.springbatch.__SpringBatch_ETL.model.Customer;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.support.CompositeItemWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.List;

@Configuration
@EnableBatchProcessing
public class BatchConfiguration {

  @Autowired StepBuilderFactory stepBuilderFactory;
  @Autowired JobBuilderFactory jobBuilderFactory;

  @Bean
  public Step step1() throws Exception {
    return stepBuilderFactory
        .get("LOAD-CSV-TO-MYSQL")
        .<Customer, Customer>chunk(200)
        .reader(customerReader())
        .processor(customerProcessor())
        .writer(customerWriter())
        .build();
  }

  @Bean
  public Step step2() throws Exception {
    return stepBuilderFactory
        .get("LOAD-DB-TO-FlatFile")
        .<Customer, Customer>chunk(20)
        .reader(customerDbReader())
        .writer(compositeWriter())
        .build();
  }

  private CompositeItemWriter<Customer> compositeWriter() {
    // List of ItemWriters which has type Customer
    List<ItemWriter<? super Customer>> writers = new ArrayList<>();
    writers.add(customerDatWriter2());
    writers.add(customerDatWriter1());

    CompositeItemWriter<Customer> compositeItemWriter = new CompositeItemWriter();
    compositeItemWriter.setDelegates(writers);
    return compositeItemWriter;
  }

  @Bean
  public Job job() throws Exception {
    return jobBuilderFactory
        .get("ETL-LOAD-JOB")
        .incrementer(new RunIdIncrementer())
        .start(step2())
        .next(step1())
        .build();
  }

  @Bean
  public CustomerProcessor customerProcessor() {
    return new CustomerProcessor();
  }

  @Bean
  public CustomerCsvReader customerReader() {
    return new CustomerCsvReader();
  }

  @Bean
  public CustomerDbWriter customerWriter() {
    return new CustomerDbWriter();
  }

  @Bean
  public CustomerDbReader customerDbReader() {
    return new CustomerDbReader();
  }

  @Bean
  public CustomerDatWriter2 customerDatWriter2() {
    return new CustomerDatWriter2();
  }

  @Bean
  public CustomerDatWriter1 customerDatWriter1() {
    return new CustomerDatWriter1();
  }
}
