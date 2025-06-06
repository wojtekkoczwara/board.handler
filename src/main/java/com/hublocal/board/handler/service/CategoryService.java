package com.hublocal.board.handler.service;

import com.hublocal.board.handler.entities.Announcement;
import com.hublocal.board.handler.entities.Category;
import com.hublocal.board.handler.model.AnnouncementDto;
import com.hublocal.board.handler.model.CategoryDto;

import java.util.List;
import java.util.Optional;

public interface CategoryService {
    List<CategoryDto> listCategories();
    Optional<CategoryDto> getCategoryById(int id);
    Category saveCategory(Category category);
    CategoryDto updateCategory(int id, CategoryDto categoryDto);
    void deleteCategory(int id);
    List<AnnouncementDto> listAnnouncementsByCategoryId(Integer categoryId);
}
