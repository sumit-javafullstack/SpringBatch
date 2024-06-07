package com.springbatch.__SpringBatch_ETL.batch;

import com.springbatch.__SpringBatch_ETL.model.Customer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.item.ItemProcessor;


@Slf4j
public class CustomerProcessor implements ItemProcessor<Customer,Customer> {
    @Override
    public Customer process(Customer customer) throws Exception {
        customer.setSalary(customer.getSalary() * 100);
        customer.setName("SUMIT");
        return customer;
    }
}
