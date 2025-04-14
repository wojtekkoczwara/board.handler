package com.hublocal.board.handler.service;

import com.hublocal.board.handler.exceptions.CustomException;
import com.hublocal.board.handler.model.Announcement;
import com.hublocal.board.handler.model.Category;
import com.hublocal.board.handler.repository.CategoryRepository;
import com.hublocal.board.handler.utils.HandleFoundObject;
import com.hublocal.board.handler.utils.categoryUtils.CategoryLogic;
import com.hublocal.board.handler.utils.categoryUtils.ValidateCategoryUnique;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;

    @Override
    public List<Category> listCategories() {
        return categoryRepository.findAll();
    }

    @Override
    public Optional<Category> getCategoryById(int id) {
        try {
            return categoryRepository.findById(id);
        } catch (IllegalArgumentException e) {
            throw new CustomException("Id: '" + id + "' must be a number");
        }
    }

    @Transactional
    @Override
    public Category saveCategory(Category category) {
        ValidateCategoryUnique.validateCategoryIsAvailableByName(categoryRepository, category.getName());
        if(category.getParentId() != null) HandleFoundObject.getParentCategory(this, category.getParentId());
        return categoryRepository.save(category);
    }

    @Override
    public Category updateCategory(int id, Category category) {
        Category categoryDb;
        try {
            categoryDb = HandleFoundObject.getCategory(this, id);
            category.setId(categoryDb.getId());
            if(category.getAnnouncementSet().size() == 0) {
                category.setAnnouncementSet(categoryDb.getAnnouncementSet());

            }
        } catch (IllegalArgumentException e) {
            throw new CustomException("Id: '" + id + "' must be a number");
        }
        return categoryRepository.save(category);
    }

    @Override
    public void deleteCategory(int id) {
        if (!CategoryLogic.verifyCategoryHasNoChildren(categoryRepository, id)) {
            throw new CustomException("Category: '" + id + "' has children, category in the delete " +
                    "request must have no children");
        }

        if (!CategoryLogic.verifyCategoryHasAnnouncements(categoryRepository, id)) {
            throw new CustomException("Category: '" + id + "' has announcements, category in the delete " +
                    "request must have no announcements");
        }
        categoryRepository.deleteById(id);
    }

    @Override
    public List<Announcement> listAnnouncementsByCategoryId(Integer categoryId) {
        return CategoryLogic.getAnnouncementsByCategory(categoryRepository, categoryId);
    }


}
