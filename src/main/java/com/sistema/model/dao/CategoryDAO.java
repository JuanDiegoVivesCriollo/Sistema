package com.sistema.model.dao;

import java.util.List;

import com.sistema.model.Category;

public interface CategoryDAO {
    boolean save(Category category);
    boolean update(Category category);
    boolean delete(int categoryId);
    List<Category> findAll();
    Category findById(int categoryId);
}
