package org.example.library.Controllers;

import jakarta.persistence.criteria.CriteriaBuilder;
import org.example.library.Entities.Book;
import org.example.library.Entities.Loan;
import org.example.library.Entities.User;
import org.example.library.MyUserDetails;
import org.example.library.Repositories.BookRepository;
import org.example.library.Repositories.LoanRepository;
import org.example.library.Repositories.UserRepository;
import org.example.library.Services.BookService;
import org.example.library.Services.JwtService;
import org.example.library.Services.LoanService;
import org.example.library.Services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/loans")
public class LoanController {
    @Autowired
    private BookService bookService;
    @Autowired
    private LoanService loanService;
    @Autowired
    private UserService userService;
    @Autowired
    private JwtService jwtService;

    public record LoanRequest(String isbn) {}
//    @PostMapping
//    public ResponseEntity<?> createLoan(
//            @RequestBody Map<String, String> body,
//            @AuthenticationPrincipal MyUserDetails userDetails) {
//
//        String isbn = body.get("isbn");
//        if (isbn == null || isbn.isBlank()) {
//            return ResponseEntity.badRequest().body(Map.of("error", "Brak ISBN"));
//        }
//
//        try {
//            Book book = bookService.findBookByIsbn(isbn).orElse(null);
//            if (book == null || book.getAvailable() == 0) {
//                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
//                        .body(Map.of("error", "Książka niedostępna"));
//            }
//
//            User user = userService.getUserById(userDetails.getId()).orElseThrow();
//            boolean exists = loanService.exists(
//                    user.getId(), book.getIsbn(), List.of("reserved", "loaned")
//            );
//            if (exists) {
//                return ResponseEntity.status(HttpStatus.CONFLICT)
//                        .body(Map.of("error", "Książka już zarezerwowana przez użytkownika"));
//            }
//
//            Loan loan = new Loan();
//            loan.setBook(book);
//            loan.setUser(user);
//            loan.setLoanDate(Instant.from(LocalDateTime.now()));
//            loan.setStatus("reserved");
//            loan.setProlonged(false);
//
//            loanService.save(loan);
//
//            book.setAvailable(book.getAvailable() - 1);
//            bookService.save(book);
//
//            return ResponseEntity.status(HttpStatus.CREATED).body(loan);
//
//        } catch (Exception e) {
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
//                    .body(Map.of("error", "Server error"));
//        }
//    }

    @PostMapping
    public ResponseEntity<?> createLoan(@RequestHeader("Authorization") String token,
                                        @RequestBody LoanRequest dto) {
        token = token.replace("Bearer ", "");
        var loan = loanService.createLoan(Long.valueOf(userService.getUserByEmail(jwtService.extractEmail(token)).orElse(null).getId()), dto.isbn());
        return ResponseEntity.status(HttpStatus.CREATED).body(loan);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteLoan(@PathVariable Long id,
                                        @RequestHeader("Authorization") String token) {
        token = token.replace("Bearer ", "");
        User user = userService.getUserByEmail(jwtService.extractEmail(token)).orElse(null);
        loanService.deleteReservation(id, user.getId(), user.getRole().toString());
        return ResponseEntity.ok().body(Map.of("message", "Rezerwacja usunięta"));
    }
    @PatchMapping("/{id}/loaned")
    public ResponseEntity<?> markLoaned(@PathVariable Long id,
                                        @RequestHeader("Authorization") String token) {
        token = token.replace("Bearer ", "");
        loanService.markLoaned(id, userService.getUserByEmail(jwtService.extractEmail(token)).orElse(null).getRole().toString());
        return ResponseEntity.ok(Map.of("message","Książka oznaczona jako wypożyczona"));
    }

    @PatchMapping("/{id}/returned")
    public ResponseEntity<?> markReturned(@PathVariable Long id,
                                          @RequestHeader("Authorization") String token) {
        token = token.replace("Bearer ", "");
        loanService.markReturned(id, userService.getUserByEmail(jwtService.extractEmail(token)).orElse(null).getRole().toString());
        return ResponseEntity.ok(Map.of("message","Książka zwrócona"));
    }

    @PatchMapping("/{id}/prolong")
    public ResponseEntity<?> prolong(@PathVariable Long id,
                                     @RequestHeader("Authorization") String token) {
        token=token.replace("Bearer ", "");
        loanService.prolongLoan(id, Long.valueOf(userService.getUserByEmail(jwtService.extractEmail(token)).orElse(null).getId()));
        return ResponseEntity.ok(Map.of("message","Przedłużono wypożyczenie o miesiąc"));
    }

    @PostMapping("/penalties")
    public ResponseEntity<?> calculatePenalties() {
        loanService.calculatePenalties();
        return ResponseEntity.ok(Map.of("message","Kary naliczone"));
    }
    @GetMapping(produces = "application/json")
    public List<Loan> findAllLoans() {
        return loanService.findAll();
    }

}

