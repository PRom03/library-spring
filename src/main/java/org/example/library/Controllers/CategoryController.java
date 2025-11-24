package org.example.library.Controllers;

import org.example.library.Entities.Category;
import org.example.library.Services.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/categories")
public class CategoryController {
    @Autowired
    private  CategoryService categoryService;



    @GetMapping(value = "/",produces = "application/json")
    public List<Category> findAll() {
        return categoryService.findAll();
    }
    @GetMapping(value = "/{id}",produces = "application/json")
    public Optional<Category> findCategoryById(@PathVariable Long id) {
        return categoryService.findCategoryById(id);
    }
}
