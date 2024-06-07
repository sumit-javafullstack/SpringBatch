package com.springbatch.__SpringBatch_ETL.batch;

import com.springbatch.__SpringBatch_ETL.model.OutputFile;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.file.FlatFileItemWriter;
import org.springframework.batch.item.file.transform.BeanWrapperFieldExtractor;
import org.springframework.batch.item.file.transform.DelimitedLineAggregator;
import org.springframework.batch.item.file.transform.FieldExtractor;
import org.springframework.batch.item.file.transform.LineAggregator;
import org.springframework.core.io.FileSystemResource;

import java.util.List;

@Slf4j
public class CustomerWriter implements ItemWriter<OutputFile> {

  private String fileName = "C:\\Users\\HP\\Pictures\\OutputFile1.dat";
  FlatFileItemWriter<OutputFile> writer = new FlatFileItemWriter<>();

  @Override
  public synchronized void write(List<? extends OutputFile> item) throws Exception {

    writer.setLineAggregator(lineAggregator());
    writer.setResource(new FileSystemResource(fileName));
    writer.setAppendAllowed(true);
    log.info("Process entered into writer");
    writer.open(new ExecutionContext());
    writer.write(item);

  }

  private LineAggregator<OutputFile> lineAggregator() {
    DelimitedLineAggregator<OutputFile> lineAggregator = new DelimitedLineAggregator<>();
    lineAggregator.setDelimiter(",");
    lineAggregator.setFieldExtractor(fieldExtractor());
    return lineAggregator;
  }

  private FieldExtractor<OutputFile> fieldExtractor() {
    BeanWrapperFieldExtractor<OutputFile> wrapper = new BeanWrapperFieldExtractor<>();
    wrapper.setNames(
            new String[] {
                    "country", "totalTransaction", "field2"
            });
    return wrapper;
  }
}
