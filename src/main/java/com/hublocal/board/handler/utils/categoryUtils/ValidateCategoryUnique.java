package com.hublocal.board.handler.utils.categoryUtils;

import com.hublocal.board.handler.exceptions.CustomException;
import com.hublocal.board.handler.repository.CategoryRepository;

public class ValidateCategoryUnique {

    public static void validateCategoryIsAvailableByName(CategoryRepository repository, String name) {
        repository.findByName(name).ifPresent(category -> {
            throw new CustomException("Category with name: '" + name + "' is already in the database");
        });
    }

}
