package org.example.library.ServicesTests;

import org.example.library.Entities.Author;
import org.example.library.Entities.Book;
import org.example.library.Entities.Category;
import org.example.library.Entities.Publisher;
import org.example.library.Repositories.AuthorRepository;
import org.example.library.Repositories.BookRepository;
import org.example.library.Repositories.CategoryRepository;
import org.example.library.Repositories.PublisherRepository;
import org.example.library.Services.BookService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.verify;

class BookServiceTest {

        private BookRepository bookRepository;
        private BookService bookService;
        private AuthorRepository authorRepository;
        private PublisherRepository publisherRepository;
        private CategoryRepository categoryRepository;

        @BeforeEach
        void setUp() {
            bookRepository = mock(BookRepository.class);
            authorRepository=mock(AuthorRepository.class);
            publisherRepository=mock(PublisherRepository.class);
            categoryRepository=mock(CategoryRepository.class);
            bookService = new BookService(bookRepository,categoryRepository,publisherRepository,authorRepository);
        }

        @Test
        void shouldReturnAllBooks() {
            List<Book> books = List.of(new Book(), new Book());
            when(bookRepository.findAll()).thenReturn(books);

            List<Book> result = bookService.findAll();

            assertThat(result).hasSize(2);
            verify(bookRepository).findAll();
        }

        @Test
        void shouldReturnBookByIsbn(){
            Book book = new Book();
            String isbn="9788301214575";
            when(bookRepository.findBookByIsbn(isbn)).thenReturn(Optional.of(book));

            Optional<Book> result = bookService.findBookByIsbn(isbn);

            assertThat(result).isPresent();
            verify(bookRepository).findBookByIsbn(isbn);
        }

        @Test
        void shouldSaveNewBook() {
            String isbn="9578321460789";
            Author author = new Author();
            author.setId(1);
            Category category = new Category();
            category.setId(1);
            Publisher publisher = new Publisher();
            publisher.setId(1);
            BookService.BookDTO dto =
                    new BookService.BookDTO(isbn, "De libro arbitrio", Long.valueOf(author.getId()), 1521, Long.valueOf(category.getId()),Long.valueOf(publisher.getId()),11);

            Book savedBook = new Book();
            savedBook.setIsbn(isbn);

            when(bookRepository.save(any(Book.class))).thenAnswer(invocation -> {
                Book b = invocation.getArgument(0);
                b.setIsbn(isbn);

                if (b.getAuthor() != null && b.getAuthor().getId() == null) {
                    b.getAuthor().setId(1);
                }
                if (b.getCategory() != null && b.getCategory().getId() == null) {
                    b.getCategory().setId(1);
                }
                if (b.getPublisher() != null && b.getPublisher().getId() == null) {
                    b.getPublisher().setId(1);
                }

                return b;
            });

            Book result = bookService.save(dto);

            ArgumentCaptor<Book> captor = ArgumentCaptor.forClass(Book.class);
            verify(bookRepository).save(captor.capture());

            Book captured = captor.getValue();
            assertThat(captured.getTitle()).isEqualTo("De libro arbitrio");
            assertThat(captured.getAuthor().getId()).isEqualTo(1);
            assertThat(captured.getPublisher().getId()).isEqualTo(1);
            assertThat(captured.getCategory().getId()).isEqualTo(1);
            assertThat(captured.getAvailable()).isEqualTo(11);

            assertThat(result.getIsbn()).isEqualTo(isbn);
        }

        @Test
        void shouldUpdateAuthor() {
            String isbn="9578321460789";

            Author author = new Author();
            author.setId(1);
            Category category = new Category();
            category.setId(1);
            Publisher publisher = new Publisher();
            publisher.setId(1);
            BookService.BookDTO dto =
                    new BookService.BookDTO(isbn, "De servo arbitrio", Long.valueOf(author.getId()), 1521, Long.valueOf(category.getId()),Long.valueOf(publisher.getId()),11);

            Book savedBook = new Book();
            savedBook.setIsbn(isbn);

            when(bookRepository.save(any(Book.class))).thenAnswer(invocation -> {
                Book b = invocation.getArgument(0);
                b.setIsbn(isbn);

                if (b.getAuthor() != null && b.getAuthor().getId() == null) {
                    b.getAuthor().setId(1);
                }
                if (b.getCategory() != null && b.getCategory().getId() == null) {
                    b.getCategory().setId(1);
                }
                if (b.getPublisher() != null && b.getPublisher().getId() == null) {
                    b.getPublisher().setId(1);
                }

                return b;
            });

            Book result = bookService.save(dto);

            ArgumentCaptor<Book> captor = ArgumentCaptor.forClass(Book.class);
            verify(bookRepository).save(captor.capture());

            Book captured = captor.getValue();
            assertThat(captured.getTitle()).isEqualTo("De servo arbitrio");
            assertThat(captured.getAuthor().getId()).isEqualTo(1);
            assertThat(captured.getPublisher().getId()).isEqualTo(1);
            assertThat(captured.getCategory().getId()).isEqualTo(1);
            assertThat(captured.getAvailable()).isEqualTo(11);

            assertThat(result.getIsbn()).isEqualTo(isbn);
        }

        @Test
        void shouldDeleteBook() {
            Book book = new Book();

            bookService.delete(book);

            verify(bookRepository).delete(book);
        }
    }

