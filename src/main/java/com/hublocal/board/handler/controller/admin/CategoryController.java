package com.hublocal.board.handler.controller.admin;

import com.hublocal.board.handler.exceptions.NotFoundException;
import com.hublocal.board.handler.entities.Announcement;
import com.hublocal.board.handler.entities.Category;
import com.hublocal.board.handler.model.AnnouncementDto;
import com.hublocal.board.handler.model.CategoryDto;
import com.hublocal.board.handler.service.CategoryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.hublocal.board.handler.utils.HandleFoundObject.getCategory;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping(CategoryController.CATEGORY_PATH)
public class CategoryController {
    public static final String CATEGORY_PATH = "/api/v1/admin/categories";
    private static final String CATEGORY_PATH_ID = "/{categoryId}";
    private static final String LIST_ANNOUNCEMENTS_BY_CATEGORY = CATEGORY_PATH_ID + "/listAnnouncements";

    @Autowired
    private final CategoryService categoryService;

    @GetMapping
    public List<CategoryDto> findAll() {
        return categoryService.listCategories();
    }

    @GetMapping(CATEGORY_PATH_ID)
    public CategoryDto getById(@PathVariable("categoryId") Integer categoryId) {
        return categoryService.getCategoryById(categoryId).orElseThrow(NotFoundException::new);
    }

    @GetMapping(LIST_ANNOUNCEMENTS_BY_CATEGORY)
    public List<AnnouncementDto> getALlAnnouncementsByCategoryId(@PathVariable("categoryId") Integer categoryId) {
        return categoryService.listAnnouncementsByCategoryId(categoryId);
    }

    @PostMapping
    public ResponseEntity<CategoryDto> createCategory(@Validated @RequestBody Category category) {

        Category category1 = categoryService.saveCategory(category);
        int id = category1.getId();
        return new ResponseEntity<>(getCategory(categoryService, id), HttpStatus.CREATED);
    }

    @PutMapping(CATEGORY_PATH_ID)
    public ResponseEntity<CategoryDto> updateById(@PathVariable int categoryId, @Validated @RequestBody CategoryDto category) {
        return ResponseEntity.ok(categoryService.updateCategory(categoryId, category));
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping(CATEGORY_PATH_ID)
    public void deleteById(@PathVariable int categoryId) {
        getCategory(categoryService, categoryId);
        categoryService.deleteCategory(categoryId);
    }



}
