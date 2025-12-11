package org.example.library.Repositories;

import org.example.library.Entities.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface BookRepository extends JpaRepository<Book, Long> {
    Optional<Book> findBookByIsbn(String isbn);

    @Query("""
            SELECT b 
            FROM Book b 
            WHERE LOWER(b.title) LIKE :query 
               OR LOWER(CONCAT(b.author.firstName, ' ', b.author.lastName)) LIKE :query
               OR LOWER(b.author.firstName) LIKE :query
               OR LOWER(b.author.lastName) LIKE :query
               OR lower(b.category.name) LIKE :query
               OR lower(b.publisher.name) LIKE :query
            """)
    List<Book> search(String query);

    @Query("""
                    SELECT b
                    FROM Book b
                    WHERE b.category.id
                        IN :categoryIds AND b.isbn NOT IN :excludedBookIsbns
            """)
    List<Book> findByCategoryIdInAndIdNotIn(List<Integer> categoryIds, List<String> excludedBookIsbns);
}
