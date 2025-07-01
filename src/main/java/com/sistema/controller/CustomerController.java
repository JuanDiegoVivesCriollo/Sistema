package com.sistema.controller;

import java.util.List;

import com.sistema.model.Customer;
import com.sistema.model.dao.CustomerDAO;
import com.sistema.view.CustomerView;

public class CustomerController {
    private final CustomerDAO customerDAO;
    private final CustomerView customerView;

    public CustomerController(CustomerDAO customerDAO, CustomerView customerView) {
        this.customerDAO = customerDAO;
        this.customerView = customerView;
    }

    public void addCustomer(Customer customer) {
        customerDAO.save(customer);
        customerView.showMessage("Cliente registrado.");
    }

    public void updateCustomer(Customer customer) {
        customerDAO.update(customer);
        customerView.showMessage("Cliente actualizado.");
         showAllCustomers();
    }

    public void deleteCustomer(int id) {
        customerDAO.delete(id);
        customerView.showMessage("Cliente eliminado.");
         showAllCustomers();
    }

    public void showAllCustomers() {
        List<Customer> customers = customerDAO.findAll();
        customerView.displayCustomerList(customers);
    }

    
}