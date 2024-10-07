package edu.unac.serivce;

import edu.unac.domain.Book;
import edu.unac.domain.User;
import edu.unac.exception.*;
import edu.unac.repository.BookRepository;
import edu.unac.repository.UserRepository;

import java.sql.SQLException;

public class LibraryService {
    private final UserRepository userRepository;
    private final BookRepository bookRepository;
    private final int maxBorrowedBooks;

    public LibraryService(UserRepository userRepository, BookRepository bookRepository, int maxBorrowedBooks) {
        this.userRepository = userRepository;
        this.bookRepository = bookRepository;
        this.maxBorrowedBooks = maxBorrowedBooks;
    }

    public void borrowBook(String userId, String bookId)
            throws UserNotFoundException, BookNotFoundException, BookAlreadyBorrowedException,
            MaxBooksBorrowedException, SQLException {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User not found"));
        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new BookNotFoundException("Book not found"));

        if (book.isBorrowed()) {
            throw new BookAlreadyBorrowedException("Book is already borrowed");
        }

        if (user.getBorrowedBooksCount() >= maxBorrowedBooks) {
            throw new MaxBooksBorrowedException("User has already borrowed the maximum number of books");
        }

        book.setBorrowedBy(user.getId());
        user.borrowBook(book);

        save(book, user);
    }

    public void returnBook(String userId, String bookId)
            throws UserNotFoundException, BookNotFoundException, BookNotBorrowedByUserException, SQLException {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User not found"));
        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new BookNotFoundException("Book not found"));

        if (!book.isBorrowed() || !book.getBorrowedBy().equals(userId)) {
            throw new BookNotBorrowedByUserException("This book was not borrowed by this user");
        }

        book.returnBook();
        user.returnBook(book);

        save(book, user);
    }

    private void save(Book book, User user) throws SQLException {
        bookRepository.save(book);
        userRepository.save(user);
    }
}
