package console_lms.models;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.UUID;

/**
 * Represents a Book in the Library.
 * Includes extended fields: ISBN, publisher, edition, genre, price, pages.
 */
public class Book implements Serializable {
    private static final long serialVersionUID = 2L;

    private String id;
    private String isbn;
    private String title;
    private String author;
    private String publisher;
    private String edition;
    private String genre;
    private double price;
    private int pages;

    // Status tracking
    private boolean isAvailable;
    private String issuedToUserId;
    private LocalDate issuedDate;
    private LocalDate dueDate;

    /**
     * Simple constructor (backward compatible).
     */
    public Book(String title, String author) {
        this.id = UUID.randomUUID().toString().substring(0, 8);
        this.isbn = "";
        this.title = title;
        this.author = author;
        this.publisher = "";
        this.edition = "1st";
        this.genre = "General";
        this.price = 0.0;
        this.pages = 0;
        this.isAvailable = true;
    }

    /**
     * Full constructor with all fields.
     */
    public Book(String isbn, String title, String author, String publisher,
                String edition, String genre, double price, int pages) {
        this.id = UUID.randomUUID().toString().substring(0, 8);
        this.isbn = isbn;
        this.title = title;
        this.author = author;
        this.publisher = publisher;
        this.edition = edition;
        this.genre = genre;
        this.price = price;
        this.pages = pages;
        this.isAvailable = true;
    }

    // ===== GETTERS =====
    public String getId()             { return id; }
    public String getIsbn()           { return isbn; }
    public String getTitle()          { return title; }
    public String getAuthor()         { return author; }
    public String getPublisher()      { return publisher; }
    public String getEdition()        { return edition; }
    public String getGenre()          { return genre; }
    public double getPrice()          { return price; }
    public int getPages()             { return pages; }
    public boolean isAvailable()      { return isAvailable; }
    public String getIssuedToUserId() { return issuedToUserId; }
    public LocalDate getIssuedDate()  { return issuedDate; }
    public LocalDate getDueDate()     { return dueDate; }

    // ===== SETTERS =====
    public void setId(String id)              { this.id = id; }
    public void setIsbn(String isbn)          { this.isbn = isbn; }
    public void setTitle(String title)        { this.title = title; }
    public void setAuthor(String author)      { this.author = author; }
    public void setPublisher(String publisher) { this.publisher = publisher; }
    public void setEdition(String edition)    { this.edition = edition; }
    public void setGenre(String genre)        { this.genre = genre; }
    public void setPrice(double price)        { this.price = price; }
    public void setPages(int pages)           { this.pages = pages; }
    public void setAvailable(boolean available) { this.isAvailable = available; }

    /**
     * Issue this book to a user with a due date.
     */
    public void issueDetails(String userId, LocalDate issuedDate, LocalDate dueDate) {
        this.isAvailable = false;
        this.issuedToUserId = userId;
        this.issuedDate = issuedDate;
        this.dueDate = dueDate;
    }

    /**
     * Mark this book as returned and available.
     */
    public void returnBook() {
        this.isAvailable = true;
        this.issuedToUserId = null;
        this.issuedDate = null;
        this.dueDate = null;
    }

    @Override
    public String toString() {
        String status = isAvailable ? "Available" : "Issued (Due: " + dueDate + ")";
        return String.format("[%s] '%s' by %s - %s | $%.2f", id, title, author, status, price);
    }
}
