package com.springbatch.__SpringBatch_ETL.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties(prefix = "app.springbatch")
public class PropertiesFile {

  private String query;
  private Input input = new Input();
  private Output output = new Output();

  @Data
  public static class Input {
    private String filename;
  }

  @Data
  public static class Output {
    private String filename1;
    private String filename2;
  }
}
