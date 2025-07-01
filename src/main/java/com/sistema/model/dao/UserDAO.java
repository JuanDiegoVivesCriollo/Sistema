package com.sistema.model.dao;

import java.util.List;

import com.sistema.model.User;

public interface UserDAO {
    boolean save(User user);
    User findByEmailAndPassword(String email, String password);
    User findById(int id);
    List<User> findAll();
    boolean update(User user);
    boolean delete(int id);
}