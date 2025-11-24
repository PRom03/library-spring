package org.example.library.Repositories;

import org.example.library.Entities.Book;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface BookRepository extends JpaRepository<Book, Long> {
    Optional<Book> findBookByIsbn(String isbn);
}
