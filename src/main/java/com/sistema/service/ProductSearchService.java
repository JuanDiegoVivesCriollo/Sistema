package com.sistema.service;

import java.util.List;
import java.util.stream.Collectors;

import com.sistema.model.Product;
import com.sistema.model.dao.ProductDAO;
import com.sistema.model.dao.ProductDAOImpl;

// Servicio para búsqueda de productos con diferentes criterios
public class ProductSearchService {
    private ProductDAO productDAO;

    public ProductSearchService() {
        this.productDAO = new ProductDAOImpl();
    }

    // Búsqueda por nombre (contiene texto)
    public List<Product> searchByName(String searchText) {
        List<Product> allProducts = productDAO.findAll();
        return allProducts.stream()
                .filter(product -> product.getNombre().toLowerCase().contains(searchText.toLowerCase()))
                .collect(Collectors.toList());
    }

    // Búsqueda por categoría
    public List<Product> searchByCategory(int categoryId) {
        List<Product> allProducts = productDAO.findAll();
        return allProducts.stream()
                .filter(product -> product.getCategoriaId() == categoryId)
                .collect(Collectors.toList());
    }

    // Búsqueda por rango de precio
    public List<Product> searchByPriceRange(double minPrice, double maxPrice) {
        List<Product> allProducts = productDAO.findAll();
        return allProducts.stream()
                .filter(product -> product.getPrecio() >= minPrice && product.getPrecio() <= maxPrice)
                .collect(Collectors.toList());
    }

    // Búsqueda combinada: nombre, categoría y precio
    public List<Product> searchProducts(String searchText, Integer categoryId, Double minPrice, Double maxPrice) {
        List<Product> allProducts = productDAO.findAll();
        
        return allProducts.stream()
                .filter(product -> {
                    // Filtro por nombre
                    if (searchText != null && !searchText.trim().isEmpty()) {
                        if (!product.getNombre().toLowerCase().contains(searchText.toLowerCase()) &&
                            !product.getDescripcion().toLowerCase().contains(searchText.toLowerCase())) {
                            return false;
                        }
                    }
                    
                    // Filtro por categoría
                    if (categoryId != null && categoryId > 0) {
                        if (product.getCategoriaId() != categoryId) {
                            return false;
                        }
                    }
                    
                    // Filtro por precio mínimo
                    if (minPrice != null && product.getPrecio() < minPrice) {
                        return false;
                    }
                    
                    // Filtro por precio máximo
                    if (maxPrice != null && product.getPrecio() > maxPrice) {
                        return false;
                    }
                    
                    return true;
                })
                .collect(Collectors.toList());
    }

    // Búsqueda por múltiples criterios con texto libre
    public List<Product> smartSearch(String query) {
        if (query == null || query.trim().isEmpty()) {
            return productDAO.findAll();
        }
        
        String[] keywords = query.toLowerCase().split("\\s+");
        List<Product> allProducts = productDAO.findAll();
        
        return allProducts.stream()
                .filter(product -> {
                    String productText = (product.getNombre() + " " + product.getDescripcion()).toLowerCase();
                    for (String keyword : keywords) {
                        if (productText.contains(keyword)) {
                            return true;
                        }
                    }
                    return false;
                })
                .collect(Collectors.toList());
    }

    // Obtener productos con stock bajo
    public List<Product> getLowStockProducts(int threshold) {
        List<Product> allProducts = productDAO.findAll();
        return allProducts.stream()
                .filter(product -> product.getStock() <= threshold)
                .collect(Collectors.toList());
    }

    // Obtener productos más caros
    public List<Product> getTopExpensiveProducts(int limit) {
        List<Product> allProducts = productDAO.findAll();
        return allProducts.stream()
                .sorted((p1, p2) -> Double.compare(p2.getPrecio(), p1.getPrecio()))
                .limit(limit)
                .collect(Collectors.toList());
    }
}
