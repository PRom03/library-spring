package org.example.library.Controllers;

import org.example.library.Entities.Book;
import org.example.library.Services.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/books")
public class BookController {
    @Autowired
    private BookService bookService;



    @GetMapping(value = "/",produces = "application/json")
    public List<Book> findAll() {
        return bookService.findAll();
    }
    @GetMapping(value = "/{isbn}",produces = "application/json")
    public Optional<Book> findBookByIsbn(@PathVariable String isbn) {
        return bookService.findBookByIsbn(isbn);
    }
}
