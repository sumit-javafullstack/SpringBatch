package com.springbatch.__SpringBatch_ETL.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.PropertySource;

import javax.sql.DataSource;

@Configuration
public class DBConfig {

  @Primary
  @Bean("dataSource")
  @ConfigurationProperties(prefix = "spring.datasource")
  public DataSource dataSource() {

    return DataSourceBuilder.create().build();
  }
}
