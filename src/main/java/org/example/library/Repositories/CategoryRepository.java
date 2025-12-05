package org.example.library.Repositories;

import org.example.library.Entities.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface CategoryRepository extends JpaRepository<Category, Long> {
    Category findCategoryByName(String category);
    @Query("""
           SELECT c 
           FROM Category c
           WHERE LOWER(c.name) LIKE :query
           """)
    List<Category> search(String query);
}
