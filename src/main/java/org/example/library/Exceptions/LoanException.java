package org.example.library.Exceptions;

public class LoanException extends RuntimeException {
    public LoanException(String message) {
        super(message);
    }
}
