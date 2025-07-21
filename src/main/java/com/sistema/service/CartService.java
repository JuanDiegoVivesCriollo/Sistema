package com.sistema.service;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.sistema.model.Product;

// Patrón Singleton - Servicio de carritos de compra
public class CartService {
    private static CartService instance;
    private Map<Integer, Map<Product, Integer>> userCarts; // userId -> (Product -> quantity)

    private CartService() {
        this.userCarts = new ConcurrentHashMap<>();
    }

    public static synchronized CartService getInstance() {
        if (instance == null) {
            instance = new CartService();
        }
        return instance;
    }

    public void addToCart(int userId, Product product, int quantity) {
        userCarts.computeIfAbsent(userId, k -> new HashMap<>())
                 .merge(product, quantity, Integer::sum);
    }

    public void removeFromCart(int userId, Product product) {
        Map<Product, Integer> cart = userCarts.get(userId);
        if (cart != null) {
            cart.remove(product);
        }
    }

    public void updateQuantity(int userId, Product product, int newQuantity) {
        Map<Product, Integer> cart = userCarts.get(userId);
        if (cart != null && newQuantity > 0) {
            cart.put(product, newQuantity);
        } else if (cart != null) {
            cart.remove(product);
        }
    }

    public Map<Product, Integer> getCart(int userId) {
        return userCarts.getOrDefault(userId, new HashMap<>());
    }

    public void clearCart(int userId) {
        userCarts.remove(userId);
    }

    public double getCartTotal(int userId) {
        Map<Product, Integer> cart = getCart(userId);
        return cart.entrySet().stream()
                   .mapToDouble(entry -> entry.getKey().getPrecio() * entry.getValue())
                   .sum();
    }
}
