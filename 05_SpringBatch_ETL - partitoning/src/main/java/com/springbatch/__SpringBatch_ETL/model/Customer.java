package com.springbatch.__SpringBatch_ETL.model;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;

@Data
public class Customer {

    @Id
    private int id;
    private String occupation;
    private String name;
    private int custId;
    private double salary;
    private String state;
    private String designation;
    private String efficiency;

}
