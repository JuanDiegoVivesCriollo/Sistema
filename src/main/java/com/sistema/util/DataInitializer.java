package com.sistema.util;

import com.sistema.model.Category;
import com.sistema.model.Discount;
import com.sistema.model.Product;
import com.sistema.model.User;
import com.sistema.model.dao.CategoryDAO;
import com.sistema.model.dao.CategoryDAOImpl;
import com.sistema.model.dao.DiscountDAO;
import com.sistema.model.dao.DiscountDAOImpl;
import com.sistema.model.dao.ProductDAO;
import com.sistema.model.dao.ProductDAOImpl;
import com.sistema.model.dao.UsuarioDAO;

// Clase para inicializar datos de ejemplo en la base de datos
public class DataInitializer {
    
    public static void initializeData() {
        initializeCategories();
        initializeProducts();
        initializeDiscounts();
        initializeAdminUser();
    }    private static void initializeCategories() {
        CategoryDAO categoryDAO = new CategoryDAOImpl();
        
        try {
            // Primero verificar si ya existe al menos una categoría
            if (!categoryDAO.findAll().isEmpty()) {
                System.out.println("Las categorías ya existen en la base de datos.");
                return;
            }
        } catch (Exception e) {
            System.out.println("Error al verificar categorías existentes, continuando con la inicialización...");
        }
        
        // Categorías de ejemplo
        Category[] categories = {
            new Category(0, "Laptops"),
            new Category(0, "Smartphones"),
            new Category(0, "Accesorios"),
            new Category(0, "Audio"),
            new Category(0, "Componentes")
        };
        
        for (Category category : categories) {
            try {
                if (categoryDAO.save(category)) {
                    System.out.println("Categoría agregada: " + category.getNombreCategoria());
                } else {
                    System.out.println("No se pudo agregar categoría: " + category.getNombreCategoria());
                }
            } catch (Exception e) {
                System.err.println("Error al agregar categoría " + category.getNombreCategoria() + ": " + e.getMessage());
            }
        }
    }
      private static void initializeProducts() {
        ProductDAO productDAO = new ProductDAOImpl();
        
        try {
            // Verificar si ya existen productos
            if (!productDAO.findAll().isEmpty()) {
                System.out.println("Los productos ya existen en la base de datos.");
                return;
            }
        } catch (Exception e) {
            System.out.println("Error al verificar productos existentes, continuando con la inicialización...");
        }
        
        // Productos de ejemplo
        Product[] products = {
            new Product(0, "Laptop Gaming ASUS ROG", "Laptop para gaming con RTX 4060, 16GB RAM, SSD 512GB", 2499.99, 15, 1, "laptop_asus.jpg"),
            new Product(0, "iPhone 15 Pro", "Smartphone Apple con chip A17 Pro, 256GB", 1299.99, 25, 2, "iphone15.jpg"),
            new Product(0, "Monitor 4K LG", "Monitor 27\" 4K UHD con HDR", 399.99, 20, 3, "monitor_lg.jpg"),
            new Product(0, "Teclado Mecánico Corsair", "Teclado gaming mecánico RGB", 149.99, 30, 3, "teclado_corsair.jpg"),
            new Product(0, "Mouse Logitech G Pro", "Mouse gaming profesional 25600 DPI", 89.99, 40, 3, "mouse_logitech.jpg"),
            new Product(0, "Auriculares Sony WH-1000XM5", "Auriculares inalámbricos con cancelación de ruido", 299.99, 18, 4, "auriculares_sony.jpg"),
            new Product(0, "SSD Samsung 1TB", "Disco sólido NVMe M.2 de alta velocidad", 129.99, 35, 5, "ssd_samsung.jpg"),
            new Product(0, "Webcam Logitech C920", "Cámara web HD 1080p para streaming", 79.99, 22, 3, "webcam_logitech.jpg")
        };
        
        for (Product product : products) {
            try {
                if (productDAO.save(product)) {
                    System.out.println("Producto agregado: " + product.getNombre());
                } else {
                    System.err.println("No se pudo agregar producto: " + product.getNombre());
                }
            } catch (Exception e) {
                System.err.println("Error al agregar producto " + product.getNombre() + ": " + e.getMessage());
            }
        }
    }
    
    private static void initializeDiscounts() {
        DiscountDAO discountDAO = new DiscountDAOImpl();
        
        // Descuentos de ejemplo
        Discount[] discounts = {
            new Discount(0, "Descuento Black Friday", "porcentaje", 20.0, "Descuento del 20% por Black Friday"),
            new Discount(0, "Descuento Estudiante", "porcentaje", 15.0, "Descuento del 15% para estudiantes"),
            new Discount(0, "Descuento Primera Compra", "fijo", 50.0, "Descuento de $50 en tu primera compra"),
            new Discount(0, "Descuento VIP", "porcentaje", 25.0, "Descuento del 25% para clientes VIP"),
            new Discount(0, "Descuento Verano", "fijo", 30.0, "Descuento de $30 en compras de verano")
        };
        
        for (Discount discount : discounts) {
            try {
                discountDAO.save(discount);
                System.out.println("Descuento agregado: " + discount.getNombre());
            } catch (Exception e) {
                System.err.println("Error al agregar descuento " + discount.getNombre() + ": " + e.getMessage());
            }
        }
    }
    
    private static void initializeAdminUser() {
        UsuarioDAO userDAO = new UsuarioDAO();
        
        // Crear usuario administrador
        try {
            userDAO.addAdmin("admin@techstore.com", "admin123");
            System.out.println("Usuario administrador creado: admin@techstore.com / admin123");
        } catch (Exception e) {
            System.err.println("Error al crear usuario administrador: " + e.getMessage());
        }
        
        // Crear algunos usuarios de ejemplo
        try {
            User user1 = new User();
            user1.setEmail("juan.perez@email.com");
            user1.setPassword("123456");
            user1.setRole("cliente");
            userDAO.save(user1);
            
            User user2 = new User();
            user2.setEmail("maria.garcia@email.com");
            user2.setPassword("123456");
            user2.setRole("cliente");
            userDAO.save(user2);
            
            System.out.println("Usuarios de ejemplo creados");
        } catch (Exception e) {
            System.err.println("Error al crear usuarios de ejemplo: " + e.getMessage());
        }
    }
    
    public static void main(String[] args) {
        System.out.println("Inicializando datos de ejemplo...");
        initializeData();
        System.out.println("Datos inicializados correctamente.");
    }
}
