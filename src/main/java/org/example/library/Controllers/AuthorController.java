package org.example.library.Controllers;

import org.example.library.Entities.Author;
import org.example.library.Services.AuthorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/authors")
public class AuthorController {
    @Autowired
    private AuthorService authorService;

    @GetMapping(value = "/",produces = "application/json")
    public List<Author> findAll() {
        return authorService.findAll();
    }
    @GetMapping(value = "/{id}",produces = "application/json")
    public Optional<Author> findAuthorById(@PathVariable Long id) {
        return authorService.findAuthorById(id);
    }
}
