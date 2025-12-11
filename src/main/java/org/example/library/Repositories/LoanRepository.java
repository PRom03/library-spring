package org.example.library.Repositories;

import org.example.library.Entities.Loan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface LoanRepository extends JpaRepository<Loan, Long> {
    @Query("""
            SELECT l FROM Loan l where l.user.id = :userId and l.book.isbn = :bookIsbn and l.status IN :statuses
            """)
    public boolean exists(Integer userId, String bookIsbn, List<String> statuses);

    List<Loan> findByUserId(Integer user_id);

    @Query("""
            select l from Loan l where l.user.id = :userId and l.book.isbn = :bookIsbn and l.status IN :statuses
            """)
    Optional<Loan> findByUserIdAndBookIdAndStatusIn(Long userId, String bookIsbn, List<String> statuses);

    @Query("""
            select l from Loan l where l.status=:status and l.returnDate<=:when
            """)
    List<Loan> findByStatusAndReturnDateBefore(String status, LocalDateTime when);

}
