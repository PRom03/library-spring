package org.example.library.ServicesTests;

import org.example.library.Entities.Author;
import org.example.library.Entities.Book;
import org.example.library.Entities.Category;
import org.example.library.Entities.Publisher;
import org.example.library.Repositories.AuthorRepository;
import org.example.library.Repositories.BookRepository;
import org.example.library.Repositories.CategoryRepository;
import org.example.library.Repositories.PublisherRepository;
import org.example.library.Services.SearchService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

public class SearchServiceTest {

    private BookRepository bookRepository;

    private AuthorRepository authorRepository;

    private CategoryRepository categoryRepository;

    private PublisherRepository publisherRepository;

    private SearchService searchService;
    @BeforeEach
    void setUp() {
        bookRepository=mock(BookRepository.class);
        authorRepository=mock(AuthorRepository.class);
        categoryRepository=mock(CategoryRepository.class);
        publisherRepository=mock(PublisherRepository.class);
        searchService=new SearchService(bookRepository,authorRepository,categoryRepository,publisherRepository);

    }
    @Test
    void shouldReturnEmptyResultWhenQueryIsBlank() {
        var result = searchService.search("   ");

        assertThat(result.books()).hasSize(0);
        assertThat(result.authors()).hasSize(0);
        assertThat(result.categories()).hasSize(0);
        assertThat(result.publishers()).hasSize(0);

        verifyNoInteractions(bookRepository, authorRepository, categoryRepository, publisherRepository);
    }

    @Test
    void shouldCallRepositoriesAndReturnResults() {
        var q = "%test%";

        var books = List.of(new Book());
        var authors = List.of(new Author());
        var categories = List.of(new Category());
        var publishers = List.of(new Publisher());

        when(bookRepository.search(q)).thenReturn(books);
        when(authorRepository.search(q)).thenReturn(authors);
        when(categoryRepository.search(q)).thenReturn(categories);
        when(publisherRepository.search(q)).thenReturn(publishers);

        var result = searchService.search("test");

        assertThat(result.books()).hasSize(1);
        assertThat(result.authors()).hasSize(1);
        assertThat(result.categories()).hasSize(1);
        assertThat(result.publishers()).hasSize(1);

        verify(bookRepository).search(q);
        verify(authorRepository).search(q);
        verify(categoryRepository).search(q);
        verify(publisherRepository).search(q);
    }
}
