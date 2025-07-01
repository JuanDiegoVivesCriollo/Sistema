package com.sistema.controller;

import java.util.List;

import com.sistema.model.Product;
import com.sistema.model.dao.ProductDAO;
import com.sistema.view.ProductView;

public class ProductController {
    private ProductDAO productDAO;
    private ProductView productView;

    public ProductController(ProductDAO productDAO, ProductView productView) {
        this.productDAO = productDAO;
        this.productView = productView;
    }

    public void addProduct(Product product) {
        productDAO.save(product);
        productView.showMessage("Producto agregado.");
    }

    public void updateProduct(Product product) {
        productDAO.update(product);
        productView.showMessage("Producto actualizado.");
    }

    public void deleteProduct(int id) {
        productDAO.delete(id);
        productView.showMessage("Producto eliminado.");
    }

    public void showAllProducts() {
        List<Product> products = productDAO.findAll();
        productView.displayProductList(products);
    }
}