package com.hublocal.board.handler.mappers;

import com.hublocal.board.handler.entities.Category;
import com.hublocal.board.handler.model.CategoryDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper
public interface CategoryMapper {

    @Mapping(ignore = true, target = "announcementSet")
    Category toEntity(CategoryDto categoryDto);

    CategoryDto toDto(Category category);
}
