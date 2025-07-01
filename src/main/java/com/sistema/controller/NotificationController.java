package com.sistema.controller;

import java.util.List;

import com.sistema.model.Notification;
import com.sistema.model.dao.NotificationDAO;
import com.sistema.view.NotificationView;

public class NotificationController {
    private final NotificationDAO notificationDAO;
    private final NotificationView notificationView;

    public NotificationController(NotificationDAO notificationDAO, NotificationView notificationView) {
        this.notificationDAO = notificationDAO;
        this.notificationView = notificationView;
    }

    public void sendNotification(Notification notification) {
        notificationDAO.save(notification);
        notificationView.showMessage("Notificación enviada.");
    }

    public void showAllNotifications() {
        List<Notification> notifications = notificationDAO.findAll();
        notificationView.displayNotificationList(notifications);
    }
}