package com.sistema.model.dao;

import java.util.List;

import com.sistema.model.Notification;

public interface NotificationDAO {
    void save(Notification notification);
    void update(Notification notification);
    void delete(int notificationId);
    List<Notification> findAll();
    Notification findById(int notificationId);
}