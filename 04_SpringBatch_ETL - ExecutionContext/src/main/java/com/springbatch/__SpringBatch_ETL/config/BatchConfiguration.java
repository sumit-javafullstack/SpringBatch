package com.springbatch.__SpringBatch_ETL.config;

import com.springbatch.__SpringBatch_ETL.batch.CustomerCsvReaderStep1;
import com.springbatch.__SpringBatch_ETL.batch.CustomerDbReaderStep2;
import com.springbatch.__SpringBatch_ETL.batch.CustomerDbWriterStep2;
import com.springbatch.__SpringBatch_ETL.batch.CustomerProcessorStep1;
import com.springbatch.__SpringBatch_ETL.batch.CustomerDbWriterStep1;
import com.springbatch.__SpringBatch_ETL.batch.CustomerProcessorStep2;
import com.springbatch.__SpringBatch_ETL.faulttolrence.CustomSkipListener;
import com.springbatch.__SpringBatch_ETL.faulttolrence.ExceptionSkipPolicy;
import com.springbatch.__SpringBatch_ETL.listner.ETLJobListener;
import com.springbatch.__SpringBatch_ETL.listner.LoadDbStepListener;
import com.springbatch.__SpringBatch_ETL.model.Customer;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableBatchProcessing
public class BatchConfiguration {

  @Autowired StepBuilderFactory stepBuilderFactory;
  @Autowired JobBuilderFactory jobBuilderFactory;

  @Autowired LoadDbStepListener loadDbStepListener;
  @Autowired ETLJobListener etlJobListener;

  @Autowired ExceptionSkipPolicy exceptionSkipPolicy;
  @Autowired CustomSkipListener customSkipListener;

  @Bean
  public Step step1ToLoadCSVtoDB() throws Exception {
    return stepBuilderFactory
        .get("LOAD-TO-DB-STEP1")
        .<Customer, Customer>chunk(200)
        .reader(customerReader())
        .processor(customerProcessor())
        .writer(customerWriter())
        .faultTolerant()
        .skipPolicy(exceptionSkipPolicy)
        // .skip(Exception.class)
        .skipLimit(10)
        .listener(customSkipListener)
        .listener(loadDbStepListener)
        .build();
  }

  @Bean
  public Step step2ToLoadDBToCsv() throws Exception {
    return stepBuilderFactory
        .get("LOAD-TO-CSV-STEP2")
        .<Customer, Customer>chunk(1000)
        .reader(customerDbReaderStep2())
        .processor(customerProcessorStep2())
        .writer(customerDbWriterStep2())
        .listener(loadDbStepListener)
        .build();
  }

  @Bean
  public Job job() throws Exception {
    return jobBuilderFactory
        .get("ETL-JOB")
        .incrementer(new RunIdIncrementer())
         .start(step1ToLoadCSVtoDB())
        .next(step2ToLoadDBToCsv())
        .listener(etlJobListener)
        .build();
  }

  @Bean
  public CustomerProcessorStep1 customerProcessor() {
    return new CustomerProcessorStep1();
  }

  @Bean
  public CustomerCsvReaderStep1 customerReader() {
    return new CustomerCsvReaderStep1();
  }

  @Bean
  public CustomerDbWriterStep1 customerWriter() {
    return new CustomerDbWriterStep1();
  }

  @Bean
  public CustomerDbReaderStep2 customerDbReaderStep2() {
    return new CustomerDbReaderStep2();
  }

  @Bean
  public CustomerDbWriterStep2 customerDbWriterStep2() {
    return new CustomerDbWriterStep2();
  }

  @Bean
  public CustomerProcessorStep2 customerProcessorStep2() {
    return new CustomerProcessorStep2();
  }
}
