package com.springbatch.__SpringBatch_ETL.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "io.springbatch.faulttolrence.input")
@Data
public class PropertyConfig {
  private String file;
  private String errorFile;

}
