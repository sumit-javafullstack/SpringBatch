package com.springbatch.__SpringBatch_ETL.config;

import com.springbatch.__SpringBatch_ETL.batch.CustomerProcessor;
import com.springbatch.__SpringBatch_ETL.batch.CustomerReader;
import com.springbatch.__SpringBatch_ETL.batch.CustomerWriter;
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
  public Step stepToLoadCSVtoDB() throws Exception {
    return stepBuilderFactory
        .get("LOAD-TO-DB-STEP")
        .<Customer, Customer>chunk(200)
        .reader(customerReader())
        .processor(customerProcessor())
        .writer(customerWriter())
        .faultTolerant()
        .skipPolicy(exceptionSkipPolicy)//Theory2
        // .skip(Exception.class)
        .skipLimit(10)// this is optional as well for exceptionSkipPolicy
        .listener(customSkipListener)
        .listener(loadDbStepListener)
        .build();
  }

  @Bean
  public Job job() throws Exception {
    return jobBuilderFactory
        .get("ETL-JOB")
        .incrementer(new RunIdIncrementer())
        .start(stepToLoadCSVtoDB())
        .listener(etlJobListener)
        .build();
  }

  @Bean
  public CustomerProcessor customerProcessor() {
    return new CustomerProcessor();
  }

  @Bean
  public CustomerReader customerReader() {
    return new CustomerReader();
  }

  @Bean
  public CustomerWriter customerWriter() {
    return new CustomerWriter();
  }
}
