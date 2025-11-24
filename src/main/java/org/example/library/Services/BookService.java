package org.example.library.Services;

import org.example.library.Entities.Book;
import org.example.library.Repositories.BookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class BookService {
    @Autowired
    private BookRepository bookrepository;

    public BookService(BookRepository bookrepository) {
        this.bookrepository = bookrepository;
    }
    public List<Book> findAll() {
        return bookrepository.findAll();
    }
    public Optional<Book> findBookByIsbn(String isbn) {
        return bookrepository.findBookByIsbn(isbn);
    }
    public void save(Book book) {
        bookrepository.save(book);
    }
}
