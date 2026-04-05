package console_lms.service;

import console_lms.models.Account;
import console_lms.models.Book;
import console_lms.models.LibraryData;
import console_lms.models.UserRole;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

/**
 * Core application logic for Library Management.
 * Handles authentication, book management, issuing/returning.
 */
public class LibraryService {

    private final LibraryDataStorage storage;
    private final LibraryData data;

    // Constant for fine calculation
    private static final double DAILY_FINE_AMOUNT = 5.0;

    public LibraryService() {
        this.storage = new LibraryDataStorage();
        this.data = storage.load();
    }

    /**
     * Persists the data to keep it consistent on changes.
     */
    private void commit() {
        storage.save(data);
    }

    // ===================================
    // AUTHENTICATION AND USERS
    // ===================================

    public boolean ensureAdminExists() {
        if (data.getUsers().stream().noneMatch(u -> u.getRole() == UserRole.ADMIN)) {
            // Create default admin and predefined users
            data.getUsers().add(new Account("admin", "admin123", UserRole.ADMIN));
            data.getUsers().add(new Account("user", "user123", UserRole.USER));
            data.getUsers().add(new Account("user2", "user234", UserRole.USER));
            commit();
            return true;
        }
        return false;
    }

    /**
     * Ensures sample books are preloaded for testing.
     */
    public boolean ensureSampleBooks() {
        if (data.getBooks().isEmpty()) {
            data.getBooks().add(new Book("978-0-13-468599-1", "Java Basics", "James Gosling",
                    "Pearson Education", "4th", "Programming", 49.99, 720));
            data.getBooks().add(new Book("978-0-13-284737-7", "Data Structures", "Mark Allen Weiss",
                    "Pearson", "3rd", "Computer Science", 59.95, 614));
            data.getBooks().add(new Book("978-1-118-06333-0", "Operating Systems", "Abraham Silberschatz",
                    "Wiley", "10th", "Computer Science", 69.99, 944));
            data.getBooks().add(new Book("978-0-596-51774-8", "JavaScript: The Good Parts", "Douglas Crockford",
                    "O'Reilly Media", "1st", "Web Development", 29.99, 176));
            data.getBooks().add(new Book("978-0-13-235088-4", "Clean Code", "Robert C. Martin",
                    "Prentice Hall", "1st", "Software Engineering", 39.99, 431));
            data.getBooks().add(new Book("978-0-201-63361-0", "Design Patterns", "Gang of Four",
                    "Addison-Wesley", "1st", "Software Engineering", 54.99, 395));
            data.getBooks().add(new Book("978-0-13-468742-1", "Database Systems", "Ramez Elmasri",
                    "Pearson", "7th", "Databases", 74.95, 1272));
            data.getBooks().add(new Book("978-0-07-352332-3", "Computer Networking", "James Kurose",
                    "McGraw-Hill", "8th", "Networking", 59.99, 856));
            commit();
            return true;
        }
        return false;
    }

    public Account login(String username, String plainPassword) {
        return data.getUsers().stream()
                .filter(u -> u.getUsername().equals(username))
                .filter(u -> u.verifyPassword(plainPassword))
                .findFirst()
                .orElse(null);
    }

    public boolean registerUser(String username, String password) {
        if (data.getUsers().stream().anyMatch(u -> u.getUsername().equals(username))) {
            return false; // username exists
        }
        Account newUser = new Account(username, password, UserRole.USER);
        data.getUsers().add(newUser);
        commit();
        return true;
    }

    public boolean removeUser(String username) {
        boolean removed = data.getUsers().removeIf(u -> u.getUsername().equals(username) && u.getRole() == UserRole.USER);
        if (removed) commit();
        return removed;
    }

    public List<Account> getAllUsers() {
        return data.getUsers().stream()
                .filter(u -> u.getRole() == UserRole.USER)
                .toList();
    }

    public List<Account> getAllAccounts() {
        return data.getUsers();
    }

    // ===================================
    // BOOKS MANAGEMENT
    // ===================================

    public void addBook(String title, String author) {
        data.getBooks().add(new Book(title, author));
        commit();
    }

    /**
     * Add a fully-detailed book.
     */
    public void addBook(String isbn, String title, String author, String publisher,
                        String edition, String genre, double price, int pages) {
        data.getBooks().add(new Book(isbn, title, author, publisher, edition, genre, price, pages));
        commit();
    }

    public boolean removeBook(String bookId) {
        boolean removed = data.getBooks().removeIf(b -> b.getId().equals(bookId));
        if (removed) commit();
        return removed;
    }

    public boolean editBook(String oldId, String newId, String title, String author, boolean isAvailable) {
        // Validate duplicate newId if it's changing
        if (!oldId.equals(newId)) {
            boolean idExists = data.getBooks().stream().anyMatch(b -> b.getId().equals(newId));
            if (idExists) return false;
        }

        Optional<Book> bookOpt = data.getBooks().stream()
                .filter(b -> b.getId().equals(oldId))
                .findFirst();

        if (bookOpt.isPresent()) {
            Book b = bookOpt.get();
            b.setId(newId);
            b.setTitle(title);
            b.setAuthor(author);
            b.setAvailable(isAvailable);

            if (isAvailable && b.getIssuedToUserId() != null) {
                b.returnBook();
            }

            commit();
            return true;
        }
        return false;
    }

    /**
     * Full edit with all extended fields.
     */
    public boolean editBookFull(String bookId, String isbn, String title, String author,
                                String publisher, String edition, String genre,
                                double price, int pages) {
        Optional<Book> bookOpt = data.getBooks().stream()
                .filter(b -> b.getId().equals(bookId))
                .findFirst();

        if (bookOpt.isPresent()) {
            Book b = bookOpt.get();
            b.setIsbn(isbn);
            b.setTitle(title);
            b.setAuthor(author);
            b.setPublisher(publisher);
            b.setEdition(edition);
            b.setGenre(genre);
            b.setPrice(price);
            b.setPages(pages);
            commit();
            return true;
        }
        return false;
    }

    public List<Book> getAllBooks() {
        return data.getBooks();
    }

    public List<Book> getAvailableBooks() {
        return data.getBooks().stream()
                .filter(Book::isAvailable)
                .toList();
    }

    public List<Book> searchBooks(String query) {
        String lowerQ = query.toLowerCase();
        return data.getBooks().stream()
                .filter(b -> b.getTitle().toLowerCase().contains(lowerQ)
                        || b.getAuthor().toLowerCase().contains(lowerQ)
                        || b.getIsbn().toLowerCase().contains(lowerQ))
                .toList();
    }

    public List<Book> getIssuedBooks() {
        return data.getBooks().stream()
                .filter(b -> !b.isAvailable())
                .toList();
    }

    public List<Book> getBooksIssuedTo(String userId) {
        return data.getBooks().stream()
                .filter(b -> userId.equals(b.getIssuedToUserId()))
                .toList();
    }

    // ===================================
    // STATISTICS
    // ===================================

    public int getTotalBooks() {
        return data.getBooks().size();
    }

    public int getAvailableBooksCount() {
        return (int) data.getBooks().stream().filter(Book::isAvailable).count();
    }

    public int getIssuedBooksCount() {
        return (int) data.getBooks().stream().filter(b -> !b.isAvailable()).count();
    }

    public int getTotalUsers() {
        return (int) data.getUsers().stream().filter(u -> u.getRole() == UserRole.USER).count();
    }

    // ===================================
    // ISSUING AND RETURNING
    // ===================================

    public boolean issueBook(String bookId, String userId, int durationDays) {
        Optional<Book> bookOpt = data.getBooks().stream()
                .filter(b -> b.getId().equals(bookId))
                .findFirst();

        if (bookOpt.isPresent()) {
            Book book = bookOpt.get();
            if (book.isAvailable()) {
                book.issueDetails(userId, LocalDate.now(), LocalDate.now().plusDays(durationDays));
                commit();
                return true;
            }
        }
        return false;
    }

    /**
     * Issue book by admin: need to specify which user to issue to.
     */
    public boolean issueBookToUser(String bookId, String username, int durationDays) {
        Optional<Account> userOpt = data.getUsers().stream()
                .filter(u -> u.getUsername().equals(username))
                .findFirst();
        if (userOpt.isEmpty()) return false;
        return issueBook(bookId, userOpt.get().getId(), durationDays);
    }

    public double returnBook(String bookId) {
        Optional<Book> bookOpt = data.getBooks().stream()
                .filter(b -> b.getId().equals(bookId))
                .findFirst();

        double fine = 0.0;
        if (bookOpt.isPresent()) {
            Book book = bookOpt.get();
            if (!book.isAvailable()) {
                if (book.getDueDate() != null && LocalDate.now().isAfter(book.getDueDate())) {
                    long overdueDays = java.time.temporal.ChronoUnit.DAYS.between(book.getDueDate(), LocalDate.now());
                    fine = overdueDays * DAILY_FINE_AMOUNT;
                }
                book.returnBook();
                commit();
            }
        }
        return fine;
    }

    // Optional user request method
    public boolean requestBook(String bookId, String userId) {
        return issueBook(bookId, userId, 14); // Standard issue is 14 days
    }

    /**
     * Find a username by user ID.
     */
    public String getUsernameById(String userId) {
        return data.getUsers().stream()
                .filter(u -> u.getId().equals(userId))
                .map(Account::getUsername)
                .findFirst()
                .orElse("Unknown");
    }
}
