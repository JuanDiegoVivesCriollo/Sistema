package com.sistema.view;

import java.util.List;

import com.sistema.model.Customer;

public interface CustomerView {
    void showMessage(String msg);
    void displayCustomerList(List<Customer> customers);
}