package org.example.library.Services;

import jakarta.transaction.Transactional;
import org.example.library.Entities.Loan;
import org.example.library.Entities.User;
import org.example.library.Entities.UserRole;
import org.example.library.Repositories.AuthorRepository;
import org.example.library.Repositories.BookRepository;
import org.example.library.Repositories.LoanRepository;
import org.example.library.Repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Duration;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class LoanService {
    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private LoanRepository loanRepository;
    @Autowired
    private UserRepository userRepository;

    public boolean exists(Integer bookId, String isbn, List<String> loanedAuthors) {
        return loanRepository.exists(bookId,isbn,loanedAuthors);
    }
    public void save(Loan loan) {
        loanRepository.save(loan);
    }

    public List<Loan> getLoans(Integer userId, String role) {
        if ("client".equals(role)) {
            return loanRepository.findByUserId(userId);
        }
        return loanRepository.findAll();
    }

    public Loan createLoan(Long userId, String isbn) {
        var book = bookRepository.findBookByIsbn(isbn)
                .orElseThrow(() -> new RuntimeException("Książka niedostępna"));

        if (book.getAvailable() == 0) {
            throw new RuntimeException("Książka niedostępna");
        }

        var existing = loanRepository.findByUserIdAndBookIdAndStatusIn(
                userId,
                book.getIsbn(),
                List.of("reserved","loaned")
        );
        if (existing.isPresent()) {
            throw new RuntimeException("Książka już zarezerwowana");
        }

        var loan = new Loan();
        loan.setBook(book);
        loan.setUser(userRepository.findById(userId).orElse(null));
        loan.setLoanDate(LocalDateTime.now());
        loan.setStatus("reserved");
        loan.setProlonged(false);
        loan.setPenalty(BigDecimal.ZERO);

        book.setAvailable(book.getAvailable() - 1);
        bookRepository.save(book);

        return loanRepository.save(loan);
    }

    public void deleteReservation(Long loanId, Integer userId, String role) {
        var loan = loanRepository.findById(loanId)
                .orElseThrow(() -> new RuntimeException("Nie znaleziono"));

        if ("client".equals(role) && "reserved".equals(loan.getStatus())) {
            var book = loan.getBook();
            book.setAvailable(book.getAvailable()+1);
            bookRepository.save(book);
            loanRepository.delete(loan);
            return;
        }
        throw new RuntimeException("Brak uprawnień");
    }
    @Transactional
    public void markLoaned(Long loanId, String role) {
        if (!"librarian".equals(role)) {
            throw new RuntimeException("Brak uprawnień");
        }

        var loan = loanRepository.findById(loanId)
                .orElseThrow(() -> new RuntimeException("Nie znaleziono"));

        loan.setStatus("loaned");
        loan.setReturnDate(LocalDateTime.now().plusMonths(1));
        loanRepository.save(loan);
    }


    @Transactional
    public void markReturned(Long loanId, String role) {
        if (!"librarian".equals(role)) {
            throw new RuntimeException("Brak uprawnień");
        }

        var loan = loanRepository.findById(loanId)
                .orElseThrow(() -> new RuntimeException("Nie znaleziono"));

        loan.setStatus("returned");
        loan.setReturnDate(LocalDateTime.now());
        loanRepository.save(loan);

        var book = loan.getBook();
        book.setAvailable(book.getAvailable() + 1);
        bookRepository.save(book);
    }

    @Transactional
    public void prolongLoan(Long loanId, Long userId) {
        var loan = loanRepository.findById(loanId)
                .orElseThrow(() -> new RuntimeException("Nie znaleziono"));

        if (!(loan.getUser().getId().equals(userId)||userRepository.findById(userId).orElse(null).getRole().equals(UserRole.librarian))) {
            throw new RuntimeException("Brak uprawnień");
        }
        if (loan.getProlonged()) {
            throw new RuntimeException("Można przedłużyć tylko raz");
        }

        loan.setReturnDate(LocalDateTime.from(loan.getReturnDate()).plusMonths(1));
        loan.setProlonged(true);
        loanRepository.save(loan);
    }

    @Transactional
    public void calculatePenalties() {
        LocalDateTime now = LocalDateTime.now();
        var overdueLoans = loanRepository.findByStatusAndReturnDateBefore("loaned", now);

        for (Loan loan : overdueLoans) {
            Duration overdue = Duration.between(loan.getReturnDate(), now);
            long daysOverdue = overdue.toDays();
            if(overdue.toSeconds()>0) loan.setPenalty(BigDecimal.valueOf(0.01));
            else if(daysOverdue>0)loan.setPenalty(BigDecimal.valueOf(daysOverdue * 0.10).setScale(2, RoundingMode.HALF_UP));
            loanRepository.save(loan);
        }
    }
    public List<Loan> findAll(){
        return loanRepository.findAll();
    }

}

