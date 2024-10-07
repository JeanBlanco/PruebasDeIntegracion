package edu.unac.repository;

import edu.unac.domain.Book;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class BookRepository {
    private Connection connection;

    public BookRepository(Connection connection) {
        this.connection = connection;
    }

    public void save(Book book) throws SQLException {
        String sql = "MERGE INTO books KEY (id) VALUES (?, ?, ?)";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, book.getId());
            statement.setString(2, book.getTitle());
            statement.setString(3, book.getBorrowedBy());
            statement.executeUpdate();
        }
    }

    public Optional<Book> findById(String id) throws SQLException {
        String sql = "SELECT * FROM books WHERE id = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, id);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                Book book = new Book(resultSet.getString("id"));
                book.setBorrowedBy(resultSet.getString("borrowed_by"));
                return Optional.of(book);
            }
        }
        return Optional.empty();
    }

    public List<Book> findAll() throws SQLException {
        List<Book> books = new ArrayList<>();
        String sql = "SELECT * FROM books";
        try (PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {
            while (resultSet.next()) {
                Book book = new Book(resultSet.getString("id"));
                book.setBorrowedBy(resultSet.getString("borrowed_by"));
                books.add(book);
            }
        }
        return books;
    }

    public Optional<Book> findByTitle(String title) throws SQLException {
        String sql = "SELECT * FROM books WHERE title = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, title);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                Book book = new Book(resultSet.getString("id"));
                book.setBorrowedBy(resultSet.getString("borrowed_by"));
                return Optional.of(book);
            }
        }
        return Optional.empty();
    }

    public void deleteById(String id) throws SQLException {
        String sql = "DELETE FROM books WHERE id = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, id);
            statement.executeUpdate();
        }
    }
}
