package org.example.library.Services;

import org.example.library.Entities.Category;
import org.example.library.Repositories.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CategoryService {
    @Autowired
    private CategoryRepository categoryRepository;
    public CategoryService(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;

    }
    public List<Category> findAll() {
        return categoryRepository.findAll();

    }
    public Optional<Category> findCategoryById(Long id) {
        return categoryRepository.findById(id);
    }
}
