package com.sistema.view;

import java.util.List;

import com.sistema.model.Notification;

public class NotificationView {
    public void showMessage(String msg) {
        System.out.println(msg);
    }
    public void displayNotificationList(List<Notification> notifications) {
        for (Notification n : notifications) {
            System.out.println("Notificación: " + n.getMensaje());
        }
    }
}