package edu.unac.exception;

public class BookNotBorrowedByUserException extends Exception {
    public BookNotBorrowedByUserException(String message) {
        super(message);
    }
}
