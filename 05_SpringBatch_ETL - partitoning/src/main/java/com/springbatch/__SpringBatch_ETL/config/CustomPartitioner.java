package com.springbatch.__SpringBatch_ETL.config;

import org.springframework.batch.core.partition.support.Partitioner;
import org.springframework.batch.item.ExecutionContext;

import java.util.HashMap;
import java.util.Map;

;

public class CustomPartitioner implements Partitioner {

  @Override
  public Map<String, ExecutionContext> partition(int gridSize) {
    Map<String, ExecutionContext> partitions = new HashMap<>();

    // each partitions will have 500 record for process

    int targetSize = 5000 / gridSize;
    int start = 1;
    int end = targetSize;
    int max = 0;
    int number = 0;
    while (start <= end && end <= 5000) {
      ExecutionContext values = new ExecutionContext();
      values.putInt("minValue", start);
      values.putInt("maxValue", end);
      partitions.put("partitions" + number, values);

      start = end + 1;
      end = end + targetSize;

      number++;
    }

    return partitions;
  }
}
