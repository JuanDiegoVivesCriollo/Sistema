package com.sistema.controller;

import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import com.sistema.model.Category;
import com.sistema.model.Customer;
import com.sistema.model.Discount;
import com.sistema.model.Order;
import com.sistema.model.Product;
import com.sistema.model.User;
import com.sistema.model.dao.CategoryDAO;
import com.sistema.model.dao.CategoryDAOImpl;
import com.sistema.model.dao.CustomerDAO;
import com.sistema.model.dao.CustomerDAOImpl;
import com.sistema.model.dao.DiscountDAO;
import com.sistema.model.dao.DiscountDAOImpl;
import com.sistema.model.dao.ProductDAO;
import com.sistema.model.dao.ProductDAOImpl;
import com.sistema.model.dao.UsuarioDAO;
import com.sistema.service.CartService;
import com.sistema.service.DiscountService;
import com.sistema.service.OrderService;
import com.sistema.service.ProductSearchService;
import com.sistema.util.DiscountStrategy;
import com.sistema.util.factory.DiscountStrategyFactory;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

// Controlador principal que integra todas las funcionalidades
public class MainController {
    // Patrón regex para validación de email
    private static final String EMAIL_PATTERN = 
        "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@" +
        "(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";
    
    private static final Pattern pattern = Pattern.compile(EMAIL_PATTERN);
    
    private UsuarioDAO userDAO;
    private CustomerDAO customerDAO;
    private ProductDAO productDAO;
    private CategoryDAO categoryDAO;
    private OrderService orderService;
    private CartService cartService;
    private DiscountDAO discountDAO;
    private ProductSearchService searchService;
    private DiscountService discountService;
    private User currentUser;
    private Customer currentCustomer;

    public MainController() {
        this.userDAO = new UsuarioDAO();
        this.customerDAO = new CustomerDAOImpl();
        this.productDAO = new ProductDAOImpl();
        this.categoryDAO = new CategoryDAOImpl();
        this.orderService = new OrderService();
        this.cartService = CartService.getInstance();
        this.discountDAO = new DiscountDAOImpl();
        this.searchService = new ProductSearchService();
        this.discountService = new DiscountService();
    }

    // Autenticación
    public boolean login(String email, String password) {
        User user = userDAO.findByEmailAndPassword(email, password);
        if (user != null) {
            this.currentUser = user;
            if ("cliente".equals(user.getRole())) {
                loadCustomerData();
            }
            return true;
        }
        return false;
    }

    private void loadCustomerData() {
        if (currentUser != null && "cliente".equals(currentUser.getRole())) {
            List<Customer> customers = customerDAO.findAll();
            for (Customer customer : customers) {
                if (customer.getIdUsuario() == currentUser.getId()) {
                    this.currentCustomer = customer;
                    break;
                }
            }
        }
    }    public String registerCustomerWithValidation(String email, String password, String nombre, String direccion, String telefono) {
        // Validar email
        String emailValidation = validateEmail(email);
        if (emailValidation != null) {
            return emailValidation;
        }
        
        // Validar contraseña
        if (password == null || password.trim().isEmpty()) {
            return "La contraseña es obligatoria";
        }
        
        if (password.length() < 6) {
            return "La contraseña debe tener al menos 6 caracteres";
        }
        
        // Validar nombre
        if (nombre == null || nombre.trim().isEmpty()) {
            return "El nombre es obligatorio";
        }
        
        if (nombre.length() > 100) {
            return "El nombre es demasiado largo (máximo 100 caracteres)";
        }
        
        // Validar dirección
        if (direccion == null || direccion.trim().isEmpty()) {
            return "La dirección es obligatoria";
        }
        
        if (direccion.length() > 200) {
            return "La dirección es demasiado larga (máximo 200 caracteres)";
        }
        
        // Validar teléfono
        if (telefono == null || telefono.trim().isEmpty()) {
            return "El teléfono es obligatorio";
        }
        
        // Validación básica de teléfono (solo números, espacios, guiones y paréntesis)
        if (!telefono.matches("^[0-9\\s\\-\\(\\)\\+]+$")) {
            return "El teléfono contiene caracteres no válidos";
        }
        
        if (telefono.length() > 20) {
            return "El teléfono es demasiado largo (máximo 20 caracteres)";
        }
        
        try {
            String trimmedEmail = email.trim().toLowerCase();
            
            // Verificar si el email ya existe
            User existingUser = userDAO.findByEmailAndPassword(trimmedEmail, "temp");
            if (existingUser != null) {
                return "Ya existe una cuenta con ese email";
            }
            
            // Crear usuario
            User user = new User();
            user.setEmail(trimmedEmail);
            user.setPassword(password);
            user.setRole("cliente");
            
            if (userDAO.save(user)) {
                // Obtener el usuario recién creado para obtener su ID
                User savedUser = userDAO.findByEmailAndPassword(trimmedEmail, password);
                if (savedUser != null) {
                    // Crear cliente
                    Customer customer = new Customer();
                    customer.setIdUsuario(savedUser.getId());
                    customer.setNombre(nombre.trim());
                    customer.setDireccion(direccion.trim());
                    customer.setTelefono(telefono.trim());
                    
                    if (customerDAO.save(customer)) {
                        return null; // Registro exitoso
                    } else {
                        return "Error al crear el perfil de cliente";
                    }
                } else {
                    return "Error al obtener los datos del usuario creado";
                }
            } else {
                return "Error al crear la cuenta de usuario";
            }
        } catch (Exception e) {
            System.err.println("Error al registrar cliente: " + e.getMessage());
            return "Error interno del sistema. Intente nuevamente.";
        }
    }
      // Mantener método original para compatibilidad
    public boolean registerCustomer(String email, String password, String nombre, String direccion, String telefono) {
        String result = registerCustomerWithValidation(email, password, nombre, direccion, telefono);
        return result == null; // true si no hay errores
    }

    public void logout() {
        this.currentUser = null;
        this.currentCustomer = null;
    }

    // Funcionalidades del administrador
    public boolean isAdmin() {
        return currentUser != null && "admin".equals(currentUser.getRole());
    }    public boolean addProduct(String nombre, String descripcion, double precio, int stock, int categoriaId, String imagenUrl) {
        if (!isAdmin()) return false;
        
        // Validar entrada
        if (nombre == null || nombre.trim().isEmpty()) {
            System.err.println("Error: El nombre del producto no puede estar vacío");
            return false;
        }
        
        if (precio <= 0) {
            System.err.println("Error: El precio debe ser mayor a 0");
            return false;
        }
        
        if (stock < 0) {
            System.err.println("Error: El stock no puede ser negativo");
            return false;
        }
        
        Product product = new Product();
        product.setNombre(nombre);
        product.setDescripcion(descripcion != null ? descripcion : "");
        product.setPrecio(precio);
        product.setStock(stock);
        product.setCategoriaId(categoriaId > 0 ? categoriaId : 1); // Usar categoría 1 por defecto
        product.setImagenUrl(imagenUrl != null ? imagenUrl : "");
          try {
            boolean result = productDAO.save(product);
            if (!result) {
                System.err.println("Error: No se pudo guardar el producto en la base de datos");
            }
            return result;
        } catch (Exception e) {
            System.err.println("Error al agregar producto: " + e.getMessage());
            return false;
        }
    }public boolean updateProduct(Product product) {
        if (!isAdmin()) return false;
        
        try {
            return productDAO.update(product);
        } catch (Exception e) {
            System.err.println("Error al actualizar producto: " + e.getMessage());
            return false;
        }
    }

    public boolean deleteProduct(int productId) {
        if (!isAdmin()) return false;
        
        try {
            return productDAO.delete(productId);
        } catch (Exception e) {
            System.err.println("Error al eliminar producto: " + e.getMessage());
            return false;
        }
    }

    public boolean addDiscount(String nombre, String tipo, double valor, String descripcion) {
        if (!isAdmin()) return false;
        
        Discount discount = new Discount();
        discount.setNombre(nombre);
        discount.setTipoDescuento(tipo);
        discount.setValor(valor);
        discount.setDescripcion(descripcion);
        
        try {
            return discountDAO.save(discount);
        } catch (Exception e) {
            System.err.println("Error al agregar descuento: " + e.getMessage());
            return false;
        }
    }

    public List<Order> getAllOrders() {
        if (!isAdmin()) return FXCollections.emptyObservableList();
        return orderService.getAllOrders();
    }

    public boolean updateOrderStatus(int orderId, String newStatus) {
        if (!isAdmin()) return false;
        
        try {
            orderService.updateOrderStatus(orderId, newStatus);
            return true;
        } catch (Exception e) {
            System.err.println("Error al actualizar estado del pedido: " + e.getMessage());
            return false;
        }
    }

    // Funcionalidades del cliente
    public boolean isCustomer() {
        return currentUser != null && "cliente".equals(currentUser.getRole());
    }

    public ObservableList<Product> getProducts() {
        try {
            return FXCollections.observableArrayList(productDAO.findAll());
        } catch (Exception e) {
            System.err.println("Error al obtener productos: " + e.getMessage());
            return FXCollections.emptyObservableList();
        }
    }

    public boolean addToCart(Product product, int quantity) {
        if (!isCustomer() || currentCustomer == null) return false;
        
        try {
            cartService.addToCart(currentCustomer.getId(), product, quantity);
            return true;
        } catch (Exception e) {
            System.err.println("Error al agregar al carrito: " + e.getMessage());
            return false;
        }
    }

    public boolean removeFromCart(Product product) {
        if (!isCustomer() || currentCustomer == null) return false;
        
        try {
            cartService.removeFromCart(currentCustomer.getId(), product);
            return true;
        } catch (Exception e) {
            System.err.println("Error al remover del carrito: " + e.getMessage());
            return false;
        }
    }

    public Map<Product, Integer> getCart() {
        if (!isCustomer() || currentCustomer == null) return Map.of();
        return cartService.getCart(currentCustomer.getId());
    }

    public double getCartTotal() {
        if (!isCustomer() || currentCustomer == null) return 0.0;
        return cartService.getCartTotal(currentCustomer.getId());
    }

    public boolean createOrder(String paymentMethod, String shippingMethod, Integer discountId) {
        if (!isCustomer() || currentCustomer == null) return false;
        
        try {
            Order order = orderService.createOrder(currentCustomer.getId(), paymentMethod, shippingMethod, discountId);
            return order != null;
        } catch (Exception e) {
            System.err.println("Error al crear pedido: " + e.getMessage());
            return false;
        }
    }

    public List<Order> getMyOrders() {
        if (!isCustomer() || currentCustomer == null) return List.of();
        return orderService.getOrdersByCustomer(currentCustomer.getId());
    }

    public List<Discount> getAvailableDiscounts() {
        try {
            return discountDAO.findAll();
        } catch (Exception e) {
            System.err.println("Error al obtener descuentos: " + e.getMessage());
            return List.of();
        }
    }

    public double calculateDiscountedPrice(double originalPrice, int discountId) {
        try {
            Discount discount = discountDAO.findById(discountId);
            if (discount != null) {
                DiscountStrategy strategy = DiscountStrategyFactory.createDiscountStrategy(
                    discount.getTipoDescuento(), 
                    discount.getValor()
                );
                return strategy.applyDiscount(originalPrice);
            }
        } catch (Exception e) {
            System.err.println("Error al calcular descuento: " + e.getMessage());
        }
        return originalPrice;
    }

    // Nuevos métodos para gestión avanzada de descuentos
    
    // Crear descuento para producto específico
    public boolean addProductDiscount(String nombre, String tipo, double valor, String descripcion, int productId) {
        if (!isAdmin()) return false;
        return discountService.createProductDiscount(nombre, tipo, valor, descripcion, productId);
    }
    
    // Crear descuento para categoría específica
    public boolean addCategoryDiscount(String nombre, String tipo, double valor, String descripcion, int categoryId) {
        if (!isAdmin()) return false;
        return discountService.createCategoryDiscount(nombre, tipo, valor, descripcion, categoryId);
    }
    
    // Activar/desactivar descuento
    public boolean toggleDiscountStatus(int discountId) {
        if (!isAdmin()) return false;
        return discountService.toggleDiscountStatus(discountId);
    }
    
    // Obtener detalles del descuento
    public String getDiscountDetails(Discount discount) {
        return discountService.getDiscountDetails(discount);
    }
    
    // Métodos de búsqueda de productos
    
    // Búsqueda por nombre
    public ObservableList<Product> searchProductsByName(String searchText) {
        try {
            List<Product> results = searchService.searchByName(searchText);
            return FXCollections.observableArrayList(results);
        } catch (Exception e) {
            System.err.println("Error en búsqueda: " + e.getMessage());
            return FXCollections.emptyObservableList();
        }
    }
    
    // Búsqueda por categoría
    public ObservableList<Product> searchProductsByCategory(int categoryId) {
        try {
            List<Product> results = searchService.searchByCategory(categoryId);
            return FXCollections.observableArrayList(results);
        } catch (Exception e) {
            System.err.println("Error en búsqueda: " + e.getMessage());
            return FXCollections.emptyObservableList();
        }
    }
    
    // Búsqueda por rango de precio
    public ObservableList<Product> searchProductsByPriceRange(double minPrice, double maxPrice) {
        try {
            List<Product> results = searchService.searchByPriceRange(minPrice, maxPrice);
            return FXCollections.observableArrayList(results);
        } catch (Exception e) {
            System.err.println("Error en búsqueda: " + e.getMessage());
            return FXCollections.emptyObservableList();
        }
    }
    
    // Búsqueda combinada
    public ObservableList<Product> searchProducts(String searchText, Integer categoryId, Double minPrice, Double maxPrice) {
        try {
            List<Product> results = searchService.searchProducts(searchText, categoryId, minPrice, maxPrice);
            return FXCollections.observableArrayList(results);
        } catch (Exception e) {
            System.err.println("Error en búsqueda: " + e.getMessage());
            return FXCollections.emptyObservableList();
        }
    }
    
    // Búsqueda inteligente
    public ObservableList<Product> smartSearch(String query) {
        try {
            List<Product> results = searchService.smartSearch(query);
            return FXCollections.observableArrayList(results);
        } catch (Exception e) {
            System.err.println("Error en búsqueda: " + e.getMessage());
            return FXCollections.emptyObservableList();
        }
    }
    
    // Obtener categorías
    public ObservableList<Category> getCategories() {
        try {
            List<Category> categories = categoryDAO.findAll();
            return FXCollections.observableArrayList(categories);
        } catch (Exception e) {
            System.err.println("Error al obtener categorías: " + e.getMessage());
            return FXCollections.emptyObservableList();
        }
    }
    
    // Obtener descuentos aplicables a un producto
    public List<Discount> getApplicableDiscounts(Product product) {
        return discountService.getApplicableDiscounts(product);
    }
    
    // Obtener el mejor descuento para un producto
    public Discount getBestDiscount(Product product) {
        return discountService.getBestDiscount(product);
    }
    
    // Calcular precio con descuento
    public double calculateDiscountedPrice(Product product, Discount discount) {
        return discountService.applyDiscount(product.getPrecio(), discount);
    }

    // Método para obtener todos los descuentos (admin)
    public List<Discount> getAllDiscounts() {
        if (!isAdmin()) return List.of();
        return discountService.getAllDiscounts();
    }

    // Getters
    public User getCurrentUser() {
        return currentUser;
    }

    public Customer getCurrentCustomer() {
        return currentCustomer;    }
    
    // Métodos de validación
    
    // Validar formato de email
    public boolean isValidEmail(String email) {
        if (email == null || email.trim().isEmpty()) {
            return false;
        }
        return pattern.matcher(email.trim()).matches();
    }
    
    // Validar que el email no sea demasiado largo
    public boolean isEmailLengthValid(String email) {
        return email != null && email.length() <= 100; // Límite razonable para email
    }
    
    // Validación completa de email
    public String validateEmail(String email) {
        if (email == null || email.trim().isEmpty()) {
            return "El email es obligatorio";
        }
        
        String trimmedEmail = email.trim();
        
        if (!isEmailLengthValid(trimmedEmail)) {
            return "El email es demasiado largo (máximo 100 caracteres)";
        }
        
        if (!isValidEmail(trimmedEmail)) {
            return "El formato del email no es válido. Ejemplo: usuario@dominio.com";
        }
        
        return null; // null significa que es válido
    }
    
    // Login con validación de email
    public String loginWithValidation(String email, String password) {
        // Validar email
        String emailValidation = validateEmail(email);
        if (emailValidation != null) {
            return emailValidation;
        }
        
        // Validar contraseña
        if (password == null || password.trim().isEmpty()) {
            return "La contraseña es obligatoria";
        }
        
        if (password.length() < 6) {
            return "La contraseña debe tener al menos 6 caracteres";
        }
        
        // Intentar login
        String trimmedEmail = email.trim().toLowerCase();
        User user = userDAO.findByEmailAndPassword(trimmedEmail, password);
        if (user != null) {
            this.currentUser = user;
            if ("cliente".equals(user.getRole())) {
                loadCustomerData();
            }
            return null; // Login exitoso
        }
        
        return "Email o contraseña incorrectos";
    }
    
    // Método para obtener el nombre del cliente por ID
    public String getCustomerNameById(int customerId) {
        try {
            Customer customer = customerDAO.findById(customerId);
            return customer != null ? customer.getNombre() : null;
        } catch (Exception e) {
            System.err.println("Error al obtener nombre del cliente: " + e.getMessage());
            return null;
        }
    }
}
