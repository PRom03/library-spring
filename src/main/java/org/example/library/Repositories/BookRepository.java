package org.example.library.Repositories;

import org.example.library.Entities.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface BookRepository extends JpaRepository<Book, Long> {
    Optional<Book> findBookByIsbn(String isbn);

    @Query("""
SELECT b FROM Book b where b.category.id IN :categoryIds and b.isbn not in :excludedBookIsbns
""")
    List<Book> findByCategoryIdInAndIdNotIn(List<Integer> categoryIds, List<String> excludedBookIsbns);
    }
}
