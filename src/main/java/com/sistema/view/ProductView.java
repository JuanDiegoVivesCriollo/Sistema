package com.sistema.view;

import java.util.List;

import com.sistema.model.Product;

public class ProductView {
    public void showMessage(String msg) {
        System.out.println(msg);
    }
    public void displayProductList(List<Product> products) {
        for (Product p : products) {
            System.out.println(p.getNombre() + " - $" + p.getPrecio());
        }
    }
}