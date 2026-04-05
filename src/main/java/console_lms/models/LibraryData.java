package console_lms.models;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * A container representing the entire state of the library.
 * Useful for single-file serialization.
 */
public class LibraryData implements Serializable {
    private static final long serialVersionUID = 1L;

    private List<Account> users;
    private List<Book> books;

    public LibraryData() {
        this.users = new ArrayList<>();
        this.books = new ArrayList<>();
    }

    public List<Account> getUsers() {
        return users;
    }

    public void setUsers(List<Account> users) {
        this.users = users;
    }

    public List<Book> getBooks() {
        return books;
    }

    public void setBooks(List<Book> books) {
        this.books = books;
    }
}
