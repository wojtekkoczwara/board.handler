package com.hublocal.board.handler.service;

import com.hublocal.board.handler.exceptions.CustomException;
import com.hublocal.board.handler.entities.Category;
import com.hublocal.board.handler.mappers.AnnouncementMapper;
import com.hublocal.board.handler.mappers.CategoryMapper;
import com.hublocal.board.handler.model.AnnouncementDto;
import com.hublocal.board.handler.model.CategoryDto;
import com.hublocal.board.handler.repository.CategoryRepository;
import com.hublocal.board.handler.utils.HandleFoundObject;
import com.hublocal.board.handler.utils.categoryUtils.CategoryLogic;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;
    private final CategoryMapper categoryMapper;
    private final AnnouncementMapper announcementMapper;

    @Override
    public List<CategoryDto> listCategories() {
        return categoryRepository.findAll().stream().map(categoryMapper::toDto).collect(Collectors.toList());
    }

    @Override
    public Optional<CategoryDto> getCategoryById(int id) {
        try {
            return categoryRepository.findById(id).map(categoryMapper::toDto);
        } catch (IllegalArgumentException e) {
            throw new CustomException("Id: '" + id + "' must be a number");
        }
    }

    @Transactional
    @Override
    public Category saveCategory(Category category) {
        CategoryLogic.validateCategoryIsAvailableByName(categoryRepository, category.getName());
        if(category.getParentId() != null) HandleFoundObject.getParentCategory(this, category.getParentId());
        return categoryRepository.save(category);
    }

    @Override
    public CategoryDto updateCategory(int id, CategoryDto categoryDto) {
        Category categoryDb;
        try {
            categoryDb = HandleFoundObject.getCategoryFromRepository(categoryRepository, id);
            categoryDto.setId(categoryDb.getId());

            Category categoryToSave = categoryMapper.toEntity(categoryDto);
            categoryToSave.setId(categoryDb.getId());
            categoryToSave.setAnnouncementSet(categoryDb.getAnnouncementSet());

            return categoryMapper.toDto(categoryRepository.saveAndFlush(categoryToSave));
        } catch (IllegalArgumentException e) {
            throw new CustomException("Id: '" + id + "' must be a number");
        }
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
    public List<AnnouncementDto> listAnnouncementsByCategoryId(Integer categoryId) {
        return CategoryLogic.getAnnouncementsByCategoryDto(categoryRepository, announcementMapper, categoryId);
    }


}
