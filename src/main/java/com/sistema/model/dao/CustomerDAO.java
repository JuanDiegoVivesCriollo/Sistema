package com.sistema.model.dao;

import java.util.List;

import com.sistema.model.Customer;

public interface CustomerDAO {
    boolean save(Customer customer);
    boolean update(Customer customer);
    boolean delete(int customerId);
    List<Customer> findAll();
    Customer findById(int customerId);
}