package com.springbatch.__SpringBatch_ETL.config;

import com.springbatch.__SpringBatch_ETL.batch.CustomerProcessor;
import com.springbatch.__SpringBatch_ETL.batch.CustomerReader;
import com.springbatch.__SpringBatch_ETL.batch.CustomerWriter;
import com.springbatch.__SpringBatch_ETL.model.Customer;
import com.springbatch.__SpringBatch_ETL.model.OutputFile;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.partition.PartitionHandler;
import org.springframework.batch.core.partition.support.TaskExecutorPartitionHandler;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

@Configuration
@EnableBatchProcessing
public class BatchConfiguration {

  @Autowired private JobBuilderFactory jobBuilderFactory;

  @Autowired private StepBuilderFactory stepBuilderFactory;

  @Bean
  public Job partitionedJob(JobRepository jobRepository) {
    return jobBuilderFactory
        .get("partitionedJob5")
        .incrementer(new RunIdIncrementer())
        .start(masterStep())
        .build();
  }

  @Bean
  public Step masterStep() {
    return stepBuilderFactory
        .get("masterStep")
        .partitioner(slaveStep().getName(), partitioner())
        .partitionHandler(partitionHandler())
        .build();
  }

  @Bean
  public Step slaveStep() {
    return stepBuilderFactory
        .get("slaveStep")
        .<Customer, OutputFile>chunk(500)
        .reader(customerReader())
        .processor(customerProcessor())
        .writer(customerWriter())
        .build();
  }

  @Bean
  public CustomPartitioner partitioner() {
    return new CustomPartitioner();
  }

  @Bean
  public PartitionHandler partitionHandler() {
    TaskExecutorPartitionHandler handler = new TaskExecutorPartitionHandler();
    handler.setStep(slaveStep());
    handler.setGridSize(10);
    handler.setTaskExecutor(taskExecutor());
    return handler;
  }

  @Bean
  public ThreadPoolTaskExecutor taskExecutor() {
    ThreadPoolTaskExecutor threadPoolTaskExecutor = new ThreadPoolTaskExecutor();
    threadPoolTaskExecutor.setMaxPoolSize(5);
    threadPoolTaskExecutor.setCorePoolSize(5);
    threadPoolTaskExecutor.setQueueCapacity(5);
    return threadPoolTaskExecutor;
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
