package edu.unac.serivce;

import edu.unac.domain.Book;
import edu.unac.domain.User;
import edu.unac.exception.*;
import edu.unac.repository.BookRepository;
import edu.unac.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class LibraryServiceTest {
    private  UserRepository userRepository;
    private  BookRepository bookRepository;
    private  LibraryService libraryService;

    private  final  String userId = "user123";
    private  final String bookId = "book123";

    @BeforeEach
    void  setUp(){
        userRepository = mock(UserRepository.class);
        bookRepository = mock(BookRepository.class);
        libraryService = new LibraryService(userRepository, bookRepository, 5);
    }

    @Test
    public  void  testBorrowBookSuccess() throws Exception{

        User user = new User(userId);
        Book book = new Book(bookId);

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(bookRepository.findById(bookId)).thenReturn(Optional.of(book));

        libraryService.borrowBook(userId, bookId);

        assertEquals(userId, book.getBorrowedBy());
        assertTrue(book.isBorrowed());
        verify(userRepository).save(user);
        verify(bookRepository).save(book);


    }

    @Test
    public  void  testBorrowBookUserNotFound() throws SQLException {
        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        Exception exception = assertThrows(UserNotFoundException.class, () -> {
            libraryService.borrowBook(userId, bookId);
        });

        assertEquals("User not found", exception.getMessage());
        verify(userRepository).findById(userId);
        verify(bookRepository, never()).findById(bookId);
    }
     @Test
     public void testBorrowBookBookNotFound() throws SQLException {
        User user = new User(userId);
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(bookRepository.findById(bookId)).thenReturn(Optional.empty());

        Exception exception = assertThrows(BookNotFoundException.class, () ->{
            libraryService.borrowBook(userId, bookId);
        });

        assertEquals("Book not found", exception.getMessage());
        verify(bookRepository).findById(bookId);
     }

     @Test
     public  void  testBorrowBookAlreadyBorrowed() throws SQLException {
        User user = new User(userId);
        Book book = new Book(bookId);
        book.setBorrowedBy("otherUser");

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(bookRepository.findById(bookId)).thenReturn(Optional.of(book));

        Exception exception = assertThrows(BookAlreadyBorrowedException.class, () ->{
            libraryService.borrowBook(userId, bookId);
        });

        assertEquals("Book is already borrowed", exception.getMessage());
        verify(bookRepository).findById(bookId);
    }

    @Test
    public void testBorrowBookMaxsReached() throws SQLException {
        // Definir el número máximo de libros que se pueden prestar
        int maxBorrowedBooks = 5;

        // Crear un usuario con el número máximo de libros prestados
        User user = new User(userId);
        for (int i = 0; i < maxBorrowedBooks; i++) {
            user.borrowBook(new Book("book" + i));  // Simular libros prestados
        }

        // Crear un nuevo libro para intentar tomar prestado
        Book book = new Book(bookId);

        // Configurar los mocks
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(bookRepository.findById(bookId)).thenReturn(Optional.of(book));

        // Verificar que se lanza la excepción MaxBooksBorrowedException
        MaxBooksBorrowedException exception = assertThrows(MaxBooksBorrowedException.class, () -> {
            libraryService.borrowBook(userId, bookId);
        });

        // Comprobar el mensaje de la excepción
        assertEquals("User has already borrowed the maximum number of books", exception.getMessage());
    }


    @Test
    public  void testReturnBookSuccess() throws  Exception {
        User user = new User(userId);
        Book book = new Book(bookId);
        book.setBorrowedBy(userId);

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(bookRepository.findById(bookId)).thenReturn(Optional.of(book));

        libraryService.returnBook(userId, bookId);

        assertFalse(book.isBorrowed());
        verify(userRepository).save(user);
        verify(bookRepository).save(book);
    }

    @Test
    public void TestReturnBookNotBorrowedByUser() throws SQLException {
        User user = new User(userId);
        Book book = new Book(bookId);
        book.setBorrowedBy("otherUser");

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(bookRepository.findById(bookId)).thenReturn(Optional.of(book));

        Exception exception = assertThrows(BookNotBorrowedByUserException.class,() ->{
            libraryService.returnBook(userId, bookId);
        } );

        assertEquals("This book was not borrowed by this user", exception.getMessage());
    }


}