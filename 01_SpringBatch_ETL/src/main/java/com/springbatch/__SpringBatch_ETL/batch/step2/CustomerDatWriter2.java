package com.springbatch.__SpringBatch_ETL.batch.step2;

import com.springbatch.__SpringBatch_ETL.model.Customer;
import com.springbatch.__SpringBatch_ETL.properties.PropertiesFile;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.batch.item.ItemStream;
import org.springframework.batch.item.ItemStreamException;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.file.FlatFileItemWriter;
import org.springframework.batch.item.file.transform.BeanWrapperFieldExtractor;
import org.springframework.batch.item.file.transform.DelimitedLineAggregator;
import org.springframework.batch.item.file.transform.FieldExtractor;
import org.springframework.batch.item.file.transform.FormatterLineAggregator;
import org.springframework.batch.item.file.transform.LineAggregator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;

import java.util.List;

@Slf4j
public class CustomerDatWriter2 implements ItemWriter<Customer> {


    @Autowired
    private PropertiesFile properties;

    FlatFileItemWriter<Customer> writer = new FlatFileItemWriter<>();

    @Override
    public void write(List<? extends Customer> list) throws Exception {

        writer.setLineAggregator(lineAggregator());
        writer.setResource(new FileSystemResource(properties.getOutput().getFilename2()));
        writer.setAppendAllowed(true);
        log.info("Process entered into writer");
        writer.open(new ExecutionContext());
        writer.write( list);

    }

    private LineAggregator<Customer> lineAggregator() {
        FormatterLineAggregator<Customer> lineAggregator = new FormatterLineAggregator();
        lineAggregator.setFormat("%03d%05d%016.2f");
        lineAggregator.setFieldExtractor(fieldExtractor());
        return lineAggregator;
    }

    private FieldExtractor<Customer> fieldExtractor() {
        BeanWrapperFieldExtractor<Customer> wrapper = new BeanWrapperFieldExtractor<>();
        wrapper.setNames(
                new String[] {
                        "id","custId", "salary"
                });
        return wrapper;
    }

}
