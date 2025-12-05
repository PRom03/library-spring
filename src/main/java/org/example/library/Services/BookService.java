package org.example.library.Services;

import org.example.library.Entities.Author;
import org.example.library.Entities.Book;
import org.example.library.Entities.Category;
import org.example.library.Entities.Publisher;
import org.example.library.Repositories.AuthorRepository;
import org.example.library.Repositories.BookRepository;
import org.example.library.Repositories.CategoryRepository;
import org.example.library.Repositories.PublisherRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class BookService {
    private final BookRepository bookrepository;
    private final CategoryRepository categoryRepository;
    private final PublisherRepository publisherRepository;
    private final AuthorRepository authorRepository;
    public BookService(BookRepository bookrepository,CategoryRepository categoryRepository,PublisherRepository publisherRepository,AuthorRepository authorRepository) {
        this.bookrepository = bookrepository;
        this.categoryRepository = categoryRepository;
        this.publisherRepository = publisherRepository;
        this.authorRepository = authorRepository;

    }
    public record BookDTO(String isbn, String title, Long authorId,Integer yearOfPublication,Long categoryId, Long publisherId,Integer available) {}
    public List<Book> findAll() {
        return bookrepository.findAll();
    }
    public Optional<Book> findBookByIsbn(String isbn) {
        return bookrepository.findBookByIsbn(isbn);
    }
    public Book save(BookDTO bookDto) {
        Book book=new Book();
        book.setIsbn(bookDto.isbn());
        book.setTitle(bookDto.title());
        book.setYearOfPublication(bookDto.yearOfPublication());
        book.setAvailable(bookDto.available());
        book.setAuthor(authorRepository.findById(bookDto.authorId()).orElse(new Author()));
        book.setCategory(categoryRepository.findById(bookDto.categoryId()).orElse(new Category()));
        book.setPublisher(publisherRepository.findById(bookDto.publisherId()).orElse(new Publisher()));
        return bookrepository.save(book);
    }
    public Book update(Book book,BookDTO bookDto) {
        book.setIsbn(bookDto.isbn());
        book.setTitle(bookDto.title());
        book.setYearOfPublication(bookDto.yearOfPublication());
        book.setAvailable(bookDto.available());
        book.setAuthor(authorRepository.findById(bookDto.authorId()).orElse(new Author()));
        book.setCategory(categoryRepository.findById(bookDto.categoryId()).orElse(new Category()));
        book.setPublisher(publisherRepository.findById(bookDto.publisherId()).orElse(new Publisher()));
        return bookrepository.save(book);
    }
    public void delete(Book book) {
        bookrepository.delete(book);
    }
}
