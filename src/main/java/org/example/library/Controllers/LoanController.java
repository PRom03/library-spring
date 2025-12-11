package org.example.library.Controllers;

import org.example.library.Entities.Loan;
import org.example.library.Entities.User;
import org.example.library.Exceptions.LoanException;
import org.example.library.Services.JwtService;
import org.example.library.Services.LoanService;
import org.example.library.Services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.web.ErrorResponse;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/loans")
public class LoanController {

    @Autowired
    private LoanService loanService;
    @Autowired
    private UserService userService;
    @Autowired
    private JwtService jwtService;

    public record LoanRequest(String isbn) {}


    @PostMapping
    public ResponseEntity<?> createLoan(@RequestHeader("Authorization") String token,
                                        @RequestBody LoanRequest dto) {
        if(token.startsWith("Bearer"))token = token.replace("Bearer ", "");
        else return new ResponseEntity<>("Token is invalid", HttpStatus.UNAUTHORIZED);
        if(jwtService.isExpired(token)) {
            return new ResponseEntity<>("Token is expired", HttpStatus.UNAUTHORIZED);
        }
        if(!userService.getUserByEmail(jwtService.extractEmail(token)).orElseThrow().getRole().toString().equals("client"))
        {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
        var loan = loanService.createLoan(Long.valueOf(userService.getUserByEmail(jwtService.extractEmail(token)).orElseThrow().getId()), dto.isbn());
        return ResponseEntity.status(HttpStatus.CREATED).body(loan);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteLoan(@PathVariable Long id,
                                        @RequestHeader("Authorization") String token) {
        if (token.startsWith("Bearer"))token = token.replace("Bearer ", "");
        if(jwtService.isExpired(token)) {
            return new ResponseEntity<>("Token is expired", HttpStatus.UNAUTHORIZED);
        }
        loanService.deleteReservation(id, userService.getUserByEmail(jwtService.extractEmail(token)).orElseThrow().getId(), userService.getUserByEmail(jwtService.extractEmail(token)).orElseThrow().getRole().toString());
        return ResponseEntity.ok().body(Map.of("message", "Rezerwacja usunięta"));
    }
    @PatchMapping("/{id}/loaned")
    public ResponseEntity<?> markLoaned(@PathVariable Long id,
                                        @RequestHeader("Authorization") String token) {
        if (token.startsWith("Bearer")) token=token.replace("Bearer ", "");
        else return new ResponseEntity<>("Invalid token", HttpStatus.UNAUTHORIZED);
        if(jwtService.isExpired(token)) {
            return new ResponseEntity<>("Token is expired", HttpStatus.UNAUTHORIZED);
        }
        loanService.markLoaned(id, userService.getUserByEmail(jwtService.extractEmail(token)).orElseThrow().getRole().toString());
        return ResponseEntity.ok(Map.of("message","Książka oznaczona jako wypożyczona"));
    }

    @PatchMapping("/{id}/returned")
    public ResponseEntity<?> markReturned(@PathVariable Long id,
                                          @RequestHeader("Authorization") String token) {
        if (token.startsWith("Bearer")) token=token.replace("Bearer ", "");
        else return new ResponseEntity<>("Invalid token", HttpStatus.UNAUTHORIZED);
        if(jwtService.isExpired(token)) {
            return new ResponseEntity<>("Token is expired", HttpStatus.UNAUTHORIZED);
        }
        loanService.markReturned(id, userService.getUserByEmail(jwtService.extractEmail(token)).orElseThrow().getRole().toString());
        return ResponseEntity.ok(Map.of("message","Książka zwrócona"));
    }

    @PatchMapping("/{id}/prolong")
    public ResponseEntity<?> prolong(@PathVariable Long id,
                                     @RequestHeader("Authorization") String token) {
        if (token.startsWith("Bearer")) token=token.replace("Bearer ", "");
        else return new ResponseEntity<>("Invalid token", HttpStatus.UNAUTHORIZED);
        if(jwtService.isExpired(token)) {
            return new ResponseEntity<>("Token is expired", HttpStatus.UNAUTHORIZED);
        }
        loanService.prolongLoan(id, Long.valueOf(userService.getUserByEmail(jwtService.extractEmail(token)).orElseThrow().getId()));
        return ResponseEntity.ok(Map.of("message","Przedłużono wypożyczenie o miesiąc"));
    }

    @PostMapping("/penalties")
    public ResponseEntity<?> calculatePenalties(@RequestHeader("Authorization") String token) {
        if (token.startsWith("Bearer")) token=token.replace("Bearer ", "");
        else return new ResponseEntity<>("Invalid token", HttpStatus.UNAUTHORIZED);
        if(jwtService.isExpired(token)) {
            return new ResponseEntity<>("Token is expired", HttpStatus.UNAUTHORIZED);
        }
        if(!userService.getUserByEmail(jwtService.extractEmail(token)).orElseThrow().getRole().toString().equals("librarian"))
        {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
        loanService.calculatePenalties();
        return ResponseEntity.ok(Map.of("message","Kary naliczone"));
    }
    @GetMapping(value="",produces = "application/json")
    public ResponseEntity<?> findAllLoans(@RequestHeader("Authorization") String token) {
        if (token.startsWith("Bearer")) token=token.replace("Bearer ", "");
        else return new ResponseEntity<>("Invalid token", HttpStatus.UNAUTHORIZED);
        if(jwtService.isExpired(token)) {
            return new ResponseEntity<>("Token is expired", HttpStatus.UNAUTHORIZED);
        }
        User user=userService.getUserByEmail(jwtService.extractEmail(token)).orElseThrow();
        return new ResponseEntity<>(loanService.findAll(user),HttpStatus.OK);
    }
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleIllegalState(LoanException ex) {
        return new ErrorResponse() {
            @Override
            public HttpStatusCode getStatusCode() {
                return HttpStatus.BAD_REQUEST;
            }

            @Override
            public ProblemDetail getBody() {
                return ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST,ex.getMessage());
            }
        };
    }

}

