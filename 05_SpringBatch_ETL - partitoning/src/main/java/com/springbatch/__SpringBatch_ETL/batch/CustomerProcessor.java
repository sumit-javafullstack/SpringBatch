package com.springbatch.__SpringBatch_ETL.batch;

import com.springbatch.__SpringBatch_ETL.model.Customer;
import com.springbatch.__SpringBatch_ETL.model.OutputFile;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.item.ItemProcessor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Slf4j
public class CustomerProcessor implements ItemProcessor<Customer,OutputFile> {


    @Override
    public OutputFile process(Customer customer) throws Exception {
        OutputFile output = new OutputFile();
        output.setCountry(customer.getState());
        output.setTotalTransaction(customer.getId());
        output.setField2((long) customer.getCustId());
        return output;
    }
}
