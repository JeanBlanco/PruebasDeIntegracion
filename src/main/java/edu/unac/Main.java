package edu.unac;

import edu.unac.domain.Book;
import edu.unac.domain.User;
import edu.unac.repository.BookRepository;
import edu.unac.repository.DatabaseConnection;
import edu.unac.repository.UserRepository;
import edu.unac.serivce.LibraryService;

import java.sql.Connection;

public class Main {
    public static void main(String[] args) {
        try (Connection connection = DatabaseConnection.getConnection()) {
            connection.createStatement().execute("CREATE TABLE users (id VARCHAR(255) PRIMARY KEY, name VARCHAR(255));");
            connection.createStatement().execute("CREATE TABLE books (id VARCHAR(255) PRIMARY KEY, title VARCHAR(255), borrowed_by VARCHAR(255), FOREIGN KEY (borrowed_by) REFERENCES users(id));");

            UserRepository userRepository = new UserRepository(connection);
            BookRepository bookRepository = new BookRepository(connection);

            userRepository.save(new User("1"));
            userRepository.save(new User("2"));
            bookRepository.save(new Book("1"));
            bookRepository.save(new Book("2"));

            System.out.println("all users: ");
            System.out.println(userRepository.findAll());
            System.out.println("all books: ");
            System.out.println(bookRepository.findAll());

            LibraryService libraryService = new LibraryService(userRepository, bookRepository, 2);

            System.out.println("Borrow book");
            libraryService.borrowBook("1", "1");
            User user = userRepository.findById("1").get();
            System.out.println(user);

            System.out.println("Borrow book again");
            libraryService.borrowBook("1", "2");
            user = userRepository.findById("1").get();
            System.out.println(user);

            System.out.println(bookRepository.findAll());

            System.out.println("Return book");
            libraryService.returnBook("1", "1");
            user = userRepository.findById("1").get();
            System.out.println(user);

            System.out.println(bookRepository.findAll());

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
