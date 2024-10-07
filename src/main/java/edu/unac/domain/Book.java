package edu.unac.domain;

public class Book {
    private String id;

    private String title;
    private String borrowedBy;

    public Book(String id) {
        this.id = id;
        this.title = title;
        this.borrowedBy = null;
    }

    public String getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getBorrowedBy() {
        return borrowedBy;
    }

    public void setBorrowedBy(String userId) {
        this.borrowedBy = userId;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void returnBook() {
        this.borrowedBy = null;
    }

    public boolean isBorrowed() {
        return borrowedBy != null;
    }

    @Override
    public String toString() {
        return "Book{" +
                "id='" + id + '\'' +
                ", title='" + title + '\'' +
                ", borrowedBy='" + borrowedBy + '\'' +
                '}';
    }
}
