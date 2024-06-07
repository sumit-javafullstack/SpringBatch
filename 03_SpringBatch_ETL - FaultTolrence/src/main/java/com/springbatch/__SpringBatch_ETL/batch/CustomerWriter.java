package com.springbatch.__SpringBatch_ETL.batch;

import com.springbatch.__SpringBatch_ETL.model.Customer;
import com.springbatch.__SpringBatch_ETL.repository.CustomerRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
public class CustomerWriter implements ItemWriter<Customer> {

    @Autowired
    CustomerRepository customerRepository;
    @Override
    public void write(List<? extends Customer> list) throws Exception {
        customerRepository.saveAll(list);
    }
}
