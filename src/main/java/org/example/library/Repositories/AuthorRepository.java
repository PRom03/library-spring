package org.example.library.Repositories;

import org.example.library.Entities.Author;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface AuthorRepository extends JpaRepository<Author, Long> {

    Author findAuthorByFirstNameAndLastName(String authorName, String authorName1);

    Author findAuthorById(Integer id);

    @Query("select max(a.id) from Author a")
    Integer findMaxId();

    @Query("""
            SELECT a 
            FROM Author a
            WHERE LOWER(CONCAT(a.firstName, ' ', a.lastName)) LIKE :query
               OR LOWER(a.firstName) LIKE :query
               OR LOWER(a.lastName) LIKE :query
            """)
    List<Author> search(String query);
}
