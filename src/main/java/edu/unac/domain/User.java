package edu.unac.domain;

import java.util.ArrayList;
import java.util.List;


public class User {
    private String id;

    private String name;
    private List<Book> borrowedBooks;

    public User(String id) {
        this.id = id;
        this.name = name;
        this.borrowedBooks = new ArrayList<>();
    }


    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public List<Book> getBorrowedBooks() {
        return borrowedBooks;
    }

    public void borrowBook(Book book) {
        borrowedBooks.add(book);
    }

    public void returnBook(Book book) {
        borrowedBooks.remove(book);
    }

    public int getBorrowedBooksCount() {
        return borrowedBooks.size(); // Devuelve el número de libros prestados
    }


    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "User{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", borrowedBooksCount=" + borrowedBooks.size() +
                '}';
    }
}