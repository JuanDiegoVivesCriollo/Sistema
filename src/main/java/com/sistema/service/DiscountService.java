package com.sistema.service;

import java.util.ArrayList;
import java.util.List;

import com.sistema.model.Category;
import com.sistema.model.Discount;
import com.sistema.model.Product;
import com.sistema.model.dao.CategoryDAO;
import com.sistema.model.dao.CategoryDAOImpl;
import com.sistema.model.dao.DiscountDAO;
import com.sistema.model.dao.DiscountDAOImpl;
import com.sistema.model.dao.ProductDAO;
import com.sistema.model.dao.ProductDAOImpl;
import com.sistema.util.DiscountStrategy;
import com.sistema.util.factory.DiscountStrategyFactory;

// Servicio mejorado para gestión de descuentos
public class DiscountService {
    private DiscountDAO discountDAO;
    private ProductDAO productDAO;
    private CategoryDAO categoryDAO;

    public DiscountService() {
        this.discountDAO = new DiscountDAOImpl();
        this.productDAO = new ProductDAOImpl();
        this.categoryDAO = new CategoryDAOImpl();
    }

    // Crear descuento general
    public boolean createGeneralDiscount(String nombre, String tipo, double valor, String descripcion) {
        Discount discount = new Discount();
        discount.setNombre(nombre);
        discount.setTipoDescuento(tipo);
        discount.setValor(valor);
        discount.setDescripcion(descripcion);
        discount.setAplicablePara("general");
        discount.setObjetoId(0);
        discount.setActivo(true);
        
        return discountDAO.save(discount);
    }

    // Crear descuento para producto específico
    public boolean createProductDiscount(String nombre, String tipo, double valor, String descripcion, int productId) {
        // Verificar que el producto existe
        Product product = productDAO.findById(productId);
        if (product == null) {
            System.err.println("El producto con ID " + productId + " no existe");
            return false;
        }

        Discount discount = new Discount();
        discount.setNombre(nombre);
        discount.setTipoDescuento(tipo);
        discount.setValor(valor);
        discount.setDescripcion(descripcion);
        discount.setAplicablePara("producto");
        discount.setObjetoId(productId);
        discount.setActivo(true);
        
        return discountDAO.save(discount);
    }

    // Crear descuento para categoría específica
    public boolean createCategoryDiscount(String nombre, String tipo, double valor, String descripcion, int categoryId) {
        // Verificar que la categoría existe
        Category category = categoryDAO.findById(categoryId);
        if (category == null) {
            System.err.println("La categoría con ID " + categoryId + " no existe");
            return false;
        }

        Discount discount = new Discount();
        discount.setNombre(nombre);
        discount.setTipoDescuento(tipo);
        discount.setValor(valor);
        discount.setDescripcion(descripcion);
        discount.setAplicablePara("categoria");
        discount.setObjetoId(categoryId);
        discount.setActivo(true);
        
        return discountDAO.save(discount);
    }

    // Obtener descuentos aplicables a un producto específico
    public List<Discount> getApplicableDiscounts(Product product) {
        List<Discount> allDiscounts = discountDAO.findAll();
        List<Discount> applicableDiscounts = new ArrayList<>();

        for (Discount discount : allDiscounts) {
            if (!discount.isActivo()) continue;

            switch (discount.getAplicablePara()) {
                case "general":
                    applicableDiscounts.add(discount);
                    break;
                case "producto":
                    if (discount.getObjetoId() == product.getId()) {
                        applicableDiscounts.add(discount);
                    }
                    break;
                case "categoria":
                    if (discount.getObjetoId() == product.getCategoriaId()) {
                        applicableDiscounts.add(discount);
                    }
                    break;
            }
        }

        return applicableDiscounts;
    }

    // Calcular el mejor descuento para un producto
    public Discount getBestDiscount(Product product) {
        List<Discount> applicableDiscounts = getApplicableDiscounts(product);
        
        if (applicableDiscounts.isEmpty()) {
            return null;
        }

        Discount bestDiscount = null;
        double maxSavings = 0;

        for (Discount discount : applicableDiscounts) {
            try {
                DiscountStrategy strategy = DiscountStrategyFactory.createDiscountStrategy(
                    discount.getTipoDescuento(), 
                    discount.getValor()
                );
                
                double discountedPrice = strategy.applyDiscount(product.getPrecio());
                double savings = product.getPrecio() - discountedPrice;
                
                if (savings > maxSavings) {
                    maxSavings = savings;
                    bestDiscount = discount;
                }
            } catch (Exception e) {
                System.err.println("Error calculando descuento: " + e.getMessage());
            }
        }

        return bestDiscount;
    }

    // Aplicar descuento a un precio
    public double applyDiscount(double originalPrice, Discount discount) {
        if (discount == null) return originalPrice;
        
        try {
            DiscountStrategy strategy = DiscountStrategyFactory.createDiscountStrategy(
                discount.getTipoDescuento(), 
                discount.getValor()
            );
            return strategy.applyDiscount(originalPrice);
        } catch (Exception e) {
            System.err.println("Error aplicando descuento: " + e.getMessage());
            return originalPrice;
        }
    }

    // Obtener todos los descuentos
    public List<Discount> getAllDiscounts() {
        return discountDAO.findAll();
    }

    // Obtener descuentos activos
    public List<Discount> getActiveDiscounts() {
        List<Discount> allDiscounts = discountDAO.findAll();
        return allDiscounts.stream()
                .filter(Discount::isActivo)
                .collect(java.util.stream.Collectors.toList());
    }

    // Activar/desactivar descuento
    public boolean toggleDiscountStatus(int discountId) {
        Discount discount = discountDAO.findById(discountId);
        if (discount != null) {
            discount.setActivo(!discount.isActivo());
            return discountDAO.update(discount);
        }
        return false;
    }

    // Obtener información detallada del descuento
    public String getDiscountDetails(Discount discount) {
        StringBuilder details = new StringBuilder();
        details.append("Nombre: ").append(discount.getNombre()).append("\n");
        details.append("Tipo: ").append(discount.getTipoDescuento()).append("\n");
        details.append("Valor: ").append(discount.getValor());
        if (discount.getTipoDescuento().equals("porcentaje")) {
            details.append("%");
        } else {
            details.append("$");
        }
        details.append("\n");
        details.append("Aplicable para: ").append(discount.getAplicablePara()).append("\n");
        
        if ("producto".equals(discount.getAplicablePara())) {
            Product product = productDAO.findById(discount.getObjetoId());
            if (product != null) {
                details.append("Producto: ").append(product.getNombre()).append("\n");
            }
        } else if ("categoria".equals(discount.getAplicablePara())) {
            Category category = categoryDAO.findById(discount.getObjetoId());
            if (category != null) {
                details.append("Categoría: ").append(category.getNombreCategoria()).append("\n");
            }
        }
        
        details.append("Estado: ").append(discount.isActivo() ? "Activo" : "Inactivo").append("\n");
        details.append("Descripción: ").append(discount.getDescripcion());
        
        return details.toString();
    }
}
