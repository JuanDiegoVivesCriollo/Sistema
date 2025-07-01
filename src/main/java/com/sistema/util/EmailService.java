package com.sistema.util;

public class EmailService {
    public void sendEmail(String to, String subject, String body) {
        // Simulación: solo imprime en consola
        System.out.println("Enviando email a: " + to);
        System.out.println("Asunto: " + subject);
        System.out.println("Mensaje: " + body);
    }
}