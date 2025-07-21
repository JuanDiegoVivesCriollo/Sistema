package com.sistema.service;

import java.util.Date;
import java.util.List;
import java.util.Map;

import com.sistema.model.Customer;
import com.sistema.model.Discount;
import com.sistema.model.Order;
import com.sistema.model.OrderDetail;
import com.sistema.model.Product;
import com.sistema.model.User;
import com.sistema.model.dao.CustomerDAO;
import com.sistema.model.dao.CustomerDAOImpl;
import com.sistema.model.dao.DiscountDAO;
import com.sistema.model.dao.DiscountDAOImpl;
import com.sistema.model.dao.OrderDAO;
import com.sistema.model.dao.OrderDAOImpl;
import com.sistema.model.dao.OrderDetailDAO;
import com.sistema.model.dao.OrderDetailDAOImpl;
import com.sistema.model.dao.ProductDAO;
import com.sistema.model.dao.ProductDAOImpl;
import com.sistema.model.dao.UsuarioDAO;
import com.sistema.util.DiscountStrategy;
import com.sistema.util.factory.DiscountStrategyFactory;
import com.sistema.util.observer.EmailNotificationObserver;

// Servicio principal que integra todos los patrones de diseño
public class OrderService {
    private OrderDAO orderDAO;
    private OrderDetailDAO orderDetailDAO;
    private CustomerDAO customerDAO;
    private ProductDAO productDAO;
    private DiscountDAO discountDAO;
    private CartService cartService;
    private EmailNotificationObserver emailObserver;

    public OrderService() {
        this.orderDAO = new OrderDAOImpl();
        this.orderDetailDAO = new OrderDetailDAOImpl();
        this.customerDAO = new CustomerDAOImpl();
        this.productDAO = new ProductDAOImpl();
        this.discountDAO = new DiscountDAOImpl();
        this.cartService = CartService.getInstance();
        this.emailObserver = new EmailNotificationObserver();
    }

    public Order createOrder(int customerId, String paymentMethod, String shippingMethod, Integer discountId) {
        try {
            // Obtener el carrito del cliente
            Map<Product, Integer> cart = cartService.getCart(customerId);
            if (cart.isEmpty()) {
                throw new IllegalStateException("El carrito está vacío");
            }

            // Calcular total inicial
            double total = cartService.getCartTotal(customerId);

            // Aplicar descuento si existe (Patrón Strategy)
            if (discountId != null) {
                Discount discount = discountDAO.findById(discountId);
                if (discount != null) {
                    DiscountStrategy strategy = DiscountStrategyFactory.createDiscountStrategy(
                        discount.getTipoDescuento(), 
                        discount.getValor()
                    );
                    total = strategy.applyDiscount(total);
                }
            }

            // Crear el pedido
            Order order = new Order();
            order.setClienteId(customerId);
            order.setFechaPedido(new Date());
            order.setEstado("pendiente");
            order.setMetodoPago(paymentMethod);
            order.setMetodoEnvio(shippingMethod);
            order.setTotal(total);

            // Agregar observador de notificaciones (Patrón Observer)
            order.addObserver(emailObserver);

            // Guardar el pedido
            orderDAO.save(order);

            // Crear los detalles del pedido
            for (Map.Entry<Product, Integer> entry : cart.entrySet()) {
                Product product = entry.getKey();
                int quantity = entry.getValue();

                OrderDetail detail = new OrderDetail();
                detail.setIdPedido(order.getId());
                detail.setIdProducto(product.getId());
                detail.setCantidad(quantity);
                detail.setPrecio(product.getPrecio());

                orderDetailDAO.save(detail);

                // Actualizar stock
                product.setStock(product.getStock() - quantity);
                productDAO.update(product);
            }

            // Limpiar carrito
            cartService.clearCart(customerId);

            return order;

        } catch (Exception e) {
            System.err.println("Error al crear pedido: " + e.getMessage());
            throw new RuntimeException("Error al procesar el pedido", e);
        }
    }

    public void updateOrderStatus(int orderId, String newStatus) {
        try {
            Order order = orderDAO.findById(orderId);
            if (order != null) {
                // Obtener email del cliente
                Customer customer = customerDAO.findById(order.getClienteId());
                if (customer != null) {
                    User user = new UsuarioDAO().findById(customer.getIdUsuario());
                    if (user != null) {
                        // Actualizar estado y notificar (Patrón Observer)
                        order.addObserver(emailObserver);
                        order.setEstado(newStatus, user.getEmail());
                        orderDAO.update(order);
                    }
                }
            }
        } catch (Exception e) {
            System.err.println("Error al actualizar estado del pedido: " + e.getMessage());
        }
    }

    public List<Order> getOrdersByCustomer(int customerId) {
        return orderDAO.findByCustomerId(customerId);
    }

    public List<Order> getAllOrders() {
        return orderDAO.findAll();
    }
}
