package org.example.library.Repositories;

import org.example.library.Entities.Publisher;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface PublisherRepository extends JpaRepository<Publisher, Long> {
    Publisher findPublisherByName(String publisher);

    @Query("""
            SELECT p 
            FROM Publisher p
            WHERE LOWER(p.name) LIKE :query
            """)
    List<Publisher> search(String query);
}
