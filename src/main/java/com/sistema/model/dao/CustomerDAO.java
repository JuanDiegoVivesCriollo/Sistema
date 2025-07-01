package com.sistema.model.dao;

import java.util.List;

import com.sistema.model.Customer;

public interface CustomerDAO {
    void save(Customer customer);
    void update(Customer customer);
    void delete(int customerId);
    List<Customer> findAll();
    Customer findById(int customerId);
}