package org.example.library.Services;

import org.example.library.Entities.Author;
import org.example.library.Repositories.AuthorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
@Service
public class AuthorService {
    @Autowired
    private AuthorRepository authorRepository;

    public List<Author> findAll() {
        return authorRepository.findAll();
    }
    public Optional<Author> findAuthorById(Long id) {
        return authorRepository.findById(id);
    }
}

