package org.example.library.Services;

import org.example.library.Entities.Author;
import org.example.library.Entities.Book;
import org.example.library.Entities.Category;
import org.example.library.Entities.Publisher;
import org.example.library.Repositories.AuthorRepository;
import org.example.library.Repositories.BookRepository;
import org.example.library.Repositories.CategoryRepository;
import org.example.library.Repositories.PublisherRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SearchService {

    private final BookRepository bookRepository;
    private final AuthorRepository authorRepository;
    private final CategoryRepository categoryRepository;
    private final PublisherRepository publisherRepository;
    public record SearchResultDto(
            List<Book> books,
            List<Author> authors,
            List<Category> categories,
            List<Publisher> publishers
    ) {}

    public SearchService(BookRepository bookRepository, AuthorRepository authorRepository, CategoryRepository categoryRepository, PublisherRepository publisherRepository) {
        this.bookRepository = bookRepository;
        this.authorRepository = authorRepository;
        this.categoryRepository = categoryRepository;
        this.publisherRepository = publisherRepository;
    }

    public SearchResultDto search(String query) {

        if (query == null || query.isBlank()) {
            return new SearchResultDto(List.of(), List.of(), List.of(), List.of());
        }

        String q = "%" + query.toLowerCase() + "%";

        return new SearchResultDto(
                bookRepository.search(q),
                authorRepository.search(q),
                categoryRepository.search(q),
                publisherRepository.search(q)
        );
    }
}
