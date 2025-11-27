package org.example.library.Exceptions.Handlers;

import org.example.library.Exceptions.LoanException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class LoanExceptionHandler {
    @ExceptionHandler(LoanException.class)
    public ResponseEntity<?> handleMyCustomException(LoanException ex) {
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(ex.getMessage());
    }
}
