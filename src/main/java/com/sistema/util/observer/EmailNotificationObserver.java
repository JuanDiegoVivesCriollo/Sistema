package com.sistema.util.observer;

import com.sistema.util.EmailService;

// Patrón Observer - Observador concreto para notificaciones por email
public class EmailNotificationObserver implements OrderObserver {
    private EmailService emailService;

    public EmailNotificationObserver() {
        this.emailService = new EmailService();
    }

    @Override
    public void onOrderStatusChanged(int orderId, String newStatus, String customerEmail) {
        String subject = "Actualización de tu pedido #" + orderId;
        String message = String.format(
            "Hola,\n\nTu pedido #%d ha cambiado al estado: %s\n\nGracias por tu compra.",
            orderId, newStatus
        );
        emailService.sendEmail(customerEmail, subject, message);
    }
}
