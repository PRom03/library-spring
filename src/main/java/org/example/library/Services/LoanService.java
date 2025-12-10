package org.example.library.Services;

import jakarta.transaction.Transactional;
import org.example.library.Exceptions.*;
import org.example.library.Entities.Loan;
import org.example.library.Entities.UserRole;
import org.example.library.Entities.User;
import org.example.library.Utilities.LoanStatus;
import org.example.library.Repositories.BookRepository;
import org.example.library.Repositories.LoanRepository;
import org.example.library.Repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
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
//    @Autowired
//    private JwtService jwtService;
    @Autowired
    private PenaltiesService penaltiesService;


    public void save(Loan loan) {
        loanRepository.save(loan);
    }



    public Loan createLoan(Long userId, String isbn) {
        var book = bookRepository.findBookByIsbn(isbn)
                .orElseThrow(() -> new RuntimeException("Książka niedostępna"));

        if (book.getAvailable() == 0) {
            throw new LoanException("Książka niedostępna");
        }

        var existing = loanRepository.findByUserIdAndBookIdAndStatusIn(
                userId,
                book.getIsbn(),
                List.of(LoanStatus.reserved.toString(),LoanStatus.loaned.toString())
        );
        if (existing.isPresent()) {
            throw new LoanException("Książka już zarezerwowana");
        }

        var loan = new Loan();
        loan.setBook(book);
        loan.setUser(userRepository.findById(userId).orElse(null));
        loan.setLoanDate(LocalDateTime.now());
        loan.setStatus(LoanStatus.reserved.toString());
        loan.setProlonged(false);
        loan.setPenalty(BigDecimal.ZERO);

        book.setAvailable(book.getAvailable() - 1);
        bookRepository.save(book);

        return loanRepository.save(loan);
    }

    public void deleteReservation(Long loanId, Integer userId, String role) {
        var loan = loanRepository.findById(loanId)
                .orElseThrow(() -> new LoanException("Nie znaleziono"));

        if (UserRole.client.toString().equals(role) &&  loan.getUser().getId().equals(userId)&&LoanStatus.reserved.toString().equals(loan.getStatus())) {
            var book = loan.getBook();
            book.setAvailable(book.getAvailable()+1);
            bookRepository.save(book);
            loanRepository.delete(loan);
            return;
        }
        throw new LoanException("Brak uprawnień lub niewłaściwy status zamówienia.");
    }
    @Transactional
    public void markLoaned(Long loanId, String role) {
        if (!UserRole.librarian.toString().equals(role)) {
            throw new LoanException("Brak uprawnień");
        }

        var loan = loanRepository.findById(loanId)
                .orElseThrow(() -> new LoanException("Nie znaleziono"));
        if(!loan.getStatus().equals(LoanStatus.reserved.toString())) throw new LoanException("Książka nie jest wypożyczona")
                ;
        loan.setStatus(LoanStatus.loaned.toString());
        loan.setReturnDate(LocalDateTime.now().plusMonths(1));
        loanRepository.save(loan);
    }


    @Transactional
    public void markReturned(Long loanId, String role) {
        if (!UserRole.librarian.toString().equals(role)) {
            throw new LoanException("Brak uprawnień");
        }

        var loan = loanRepository.findById(loanId)
                .orElseThrow(() -> new LoanException("Nie znaleziono"));

        if(!loan.getStatus().equals(LoanStatus.loaned.toString())) throw new LoanException("Książka nie jest wypożyczona")
                ;
        loan.setStatus(LoanStatus.returned.toString());
        loan.setReturnDate(LocalDateTime.now());
        loanRepository.save(loan);

        var book = loan.getBook();
        book.setAvailable(book.getAvailable() + 1);
        bookRepository.save(book);
    }

    @Transactional
    public void prolongLoan(Long loanId, Long userId){
        var loan = loanRepository.findById(loanId)
                .orElseThrow(() -> new LoanException("Nie znaleziono"));

        if (!(loan.getUser().getId().equals(userId)||userRepository.findById(userId).orElse(null).getRole().toString().equals(UserRole.librarian.toString()))) {
            throw new LoanException("Brak uprawnień");
        }
        if (loan.getProlonged()) {
            throw new LoanException("Można przedłużyć tylko raz");
        }
        if(loan.getStatus().equals(LoanStatus.returned.toString())) {
            throw new LoanException("Książka już zwrócona.");
        }
        loan.setReturnDate(LocalDateTime.from(loan.getReturnDate()).plusMonths(1));
        loan.setProlonged(true);
        loanRepository.save(loan);
    }

    @Transactional
    public void calculatePenalties() {
        penaltiesService.calculatePenalties();
    }
    public List<Loan> findAll(User user){
        if(user.getRole().toString().equals(UserRole.client.toString())) {
            return loanRepository.findByUserId(user.getId());
        }
        return loanRepository.findAll();
    }

}

