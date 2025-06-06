package com.hublocal.board.handler.repository;

import com.hublocal.board.handler.entities.Category;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CategoryRepository extends JpaRepository<Category, Integer> {
    Optional<Category> findByName(String name);
    Optional<List<Category>> findAllByParentId(Integer parentId);
}
