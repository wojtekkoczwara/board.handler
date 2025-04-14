package com.hublocal.board.handler.service;

import com.hublocal.board.handler.model.Announcement;
import com.hublocal.board.handler.model.Category;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface CategoryService {
    List<Category> listCategories();
    Optional<Category> getCategoryById(int id);
    Category saveCategory(Category category);
    Category updateCategory(int id, Category category);
    void deleteCategory(int id);
    List<Announcement> listAnnouncementsByCategoryId(Integer categoryId);
}
