package com.springbatch.__SpringBatch_ETL.repository;

import com.springbatch.__SpringBatch_ETL.model.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CustomerRepository extends JpaRepository<Customer,Integer> {

}
