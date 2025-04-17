package com.hublocal.board.handler.utils.categoryUtils;

import com.hublocal.board.handler.exceptions.CustomException;
import com.hublocal.board.handler.exceptions.NotFoundException;
import com.hublocal.board.handler.model.Announcement;
import com.hublocal.board.handler.model.Category;
import com.hublocal.board.handler.repository.CategoryRepository;
import org.springframework.http.converter.HttpMessageNotReadableException;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class CategoryLogic {

    public static List<Announcement> getAnnouncementsByCategory(CategoryRepository repository, Integer parentId) {
        //find all categories by parent id
        List<Category> categories = repository.findAllByParentId(parentId).orElse(Collections.emptyList());
        if (categories.isEmpty()) return repository.findById(parentId).orElseThrow(NotFoundException::new)
                .getAnnouncementSet().stream().toList();

        List<Category> finalCategories = new ArrayList<>();
        //verify if category has children (find all categories by this parent id -> if list size is = 0)
        int i = 0;
        while (true) {
            Category localCategory =  categories.get(i);
            List<Category> categoriesLocal = repository.findAllByParentId(localCategory.getId()).orElse(Collections.emptyList());

            //act if category has no children - put value to the categoriesFinal, which will be used in final finding announcements
            if(categoriesLocal.isEmpty()) {
                finalCategories.add(localCategory);
                i++;
                if(categories.size() == i) break;
                continue;
            }

            //act if category has children - add to the categories list and continue loop
            categories.addAll(categoriesLocal);
            i++;
            if(categories.size() == i) break;
        }


        List<Announcement> announcements = new ArrayList<>();
        finalCategories.forEach(category -> {
           announcements.addAll(category.getAnnouncementSet());
        });
       return announcements;
    }

    public static boolean verifyCategoryHasNoChildren(CategoryRepository repository, Integer categoryId) {
        return repository.findAllByParentId(categoryId).orElseThrow(NotFoundException::new).size() == 0;
    }

    public static boolean verifyCategoryHasAnnouncements(CategoryRepository categoryRepository, int id) {
        return categoryRepository.findById(id).orElseThrow(NotFoundException::new).getAnnouncementSet().isEmpty();
    }

    public static void validateCategoryIsAvailableByName(CategoryRepository repository, String name) {
        repository.findByName(name).ifPresent(category -> {
            throw new CustomException("Category with name: '" + name + "' is already in the database");
        });
    }

    public static void verifyCategoryExist(Announcement announcement, CategoryRepository repository) {
        try {
            repository.findById(announcement.getCategoryId()).orElseThrow(() -> new CustomException("Category with id: '" + announcement.getCategoryId() + "' not found"));
        } catch (HttpMessageNotReadableException e) {
            throw new CustomException(e.getMessage());
        }
    }
}
