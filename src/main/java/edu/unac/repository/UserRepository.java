package edu.unac.repository;

import edu.unac.domain.Book;
import edu.unac.domain.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class UserRepository {
    private Connection connection;

    public UserRepository(Connection connection) {
        this.connection = connection;
    }

    public void save(User user) throws SQLException {
        String sql = "MERGE INTO users KEY (id) VALUES (?, ?)";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, user.getId());
            statement.setString(2, user.getName());
            statement.executeUpdate();
        }
    }

    public Optional<User> findById(String id) throws SQLException {
        String sqlUser = "SELECT * FROM users WHERE id = ?";
        String sqlBooks = "SELECT * FROM books WHERE borrowed_by = ?";

        try (PreparedStatement userStatement = connection.prepareStatement(sqlUser)) {
            userStatement.setString(1, id);
            ResultSet userResultSet = userStatement.executeQuery();

            if (userResultSet.next()) {
                User user = new User(userResultSet.getString("id"));

                try (PreparedStatement booksStatement = connection.prepareStatement(sqlBooks)) {
                    booksStatement.setString(1, id);
                    ResultSet booksResultSet = booksStatement.executeQuery();

                    while (booksResultSet.next()) {
                        Book book = new Book(booksResultSet.getString("id"));
                        user.borrowBook(book);
                    }
                }

                return Optional.of(user);
            }
        }
        return Optional.empty();
    }

    public List<User> findAll() throws SQLException {
        List<User> users = new ArrayList<>();
        String sql = "SELECT * FROM users";
        try (PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {
            while (resultSet.next()) {
                User user = new User(resultSet.getString("id"));
                users.add(user);
            }
        }
        return users;
    }

    public void deleteById(String id) throws SQLException {
        String sql = "DELETE FROM users WHERE id = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, id);
            statement.executeUpdate();
        }
    }
}
