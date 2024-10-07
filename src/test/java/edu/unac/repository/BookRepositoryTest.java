package edu.unac.repository;

import edu.unac.domain.Book;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class BookRepositoryTest {
    @Test
    public void saveBook() throws SQLException {
        Connection connection = DriverManager
                .getConnection("jdbc:h2:mem:testdb");

        connection.prepareStatement(
                "CREATE TABLE IF NOT EXISTS books (id VARCHAR(255) PRIMARY KEY, " +
                        "title VARCHAR(255), " +
                        "borrowed_by VARCHAR(255));"
        ).executeUpdate();

        BookRepository bookRepository = new BookRepository(connection);

        bookRepository.save(new Book("1"));

        Optional<Book> bookOptional = bookRepository.findById("1");
        assertTrue(bookOptional.isPresent());
    }

    @Test
    public void findAll() throws SQLException {
        Connection connection = DriverManager
                .getConnection("jdbc:h2:mem:testdb");

        connection.prepareStatement(
                "CREATE TABLE IF NOT EXISTS books (id VARCHAR(255) PRIMARY KEY, " +
                        "title VARCHAR(255), " +
                        "borrowed_by VARCHAR(255));"
        ).executeUpdate();

        BookRepository bookRepository =
                new BookRepository(connection);

        bookRepository.save(new Book("1"));
        bookRepository.save(new Book("2"));

        List<Book> books = bookRepository.findAll();

        assertTrue(books.size() == 2);
    }

    @Test
    public void findByTitle() throws SQLException {
        // Crear conexión a la base de datos en memoria
        Connection connection = DriverManager.getConnection("jdbc:h2:mem:testdb");

        // Crear tabla de libros
        connection.prepareStatement(
                "CREATE TABLE IF NOT EXISTS books (id VARCHAR(255) PRIMARY KEY, " +
                        "title VARCHAR(255), " +
                        "borrowed_by VARCHAR(255));"
        ).executeUpdate();

        // Crear instancia del repositorio
        BookRepository bookRepository = new BookRepository(connection);

        // Crear libros con títulos asignados
        Book book1 = new Book("1");
        book1.setTitle("Clean Code");  // Establecer título
        bookRepository.save(book1);

        Book book2 = new Book("2");
        book2.setTitle("Effective Java");  // Establecer título
        bookRepository.save(book2);

        // Buscar libro por título
        Optional<Book> bookOptional = bookRepository.findByTitle("Effective Java");

        // Verificar si el libro fue encontrado
        assertTrue(bookOptional.isPresent());
        assertEquals("2", bookOptional.get().getId());
        assertEquals(null, bookOptional.get().getTitle());
    }


    @Test
    public void deleteById() throws SQLException {
        Connection connection = DriverManager
                .getConnection("jdbc:h2:mem:testdb;DB_CLOSE_DELAY=-1");  // Evitar que la base de datos se cierre automáticamente

        connection.prepareStatement(
                "CREATE TABLE IF NOT EXISTS books (id VARCHAR(255) PRIMARY KEY, " +
                        "title VARCHAR(255), " +
                        "borrowed_by VARCHAR(255));"
        ).executeUpdate();

        BookRepository bookRepository = new BookRepository(connection);

        // Guardar libros en la base de datos
        bookRepository.save(new Book("1"));
        bookRepository.save(new Book("2"));

        // Verificar que el libro "1" exista
        Optional<Book> bookOptionalBeforeDelete = bookRepository.findById("1");
        assertTrue(bookOptionalBeforeDelete.isPresent());

        // Eliminar el libro
        bookRepository.deleteById("1");

        // Verificar que el libro "1" ya no exista
        Optional<Book> bookOptionalAfterDelete = bookRepository.findById("1");
        assertFalse(bookOptionalAfterDelete.isPresent());
    }


}
