package console_lms.ui;

import console_lms.models.Account;
import console_lms.models.Book;
import console_lms.models.UserRole;
import console_lms.service.LibraryService;

import java.util.List;
import java.util.Scanner;

public class ConsoleApp {
    private final LibraryService libraryService;
    private final Scanner scanner;
    private Account currentUser;

    public ConsoleApp(LibraryService libraryService) {
        this.libraryService = libraryService;
        this.scanner = new Scanner(System.in);
    }

    public void start() {
        System.out.println("=====================================");
        System.out.println("  Welcome to Library Management");
        System.out.println("=====================================");

        while (true) {
            if (currentUser == null) {
                showLoginMenu();
            } else if (currentUser.getRole() == UserRole.ADMIN) {
                showAdminMenu();
            } else {
                showUserMenu();
            }
        }
    }

    private void showLoginMenu() {
        System.out.println("\n1. Login");
        System.out.println("2. Exit");
        System.out.print("Choose option: ");
        String choice = scanner.nextLine();

        if (choice.equals("1")) {
            System.out.print("Username: ");
            String username = scanner.nextLine();
            System.out.print("Password: ");
            String password = scanner.nextLine();

            Account account = libraryService.login(username, password);
            if (account != null) {
                System.out.println("Login successful! Welcome " + account.getUsername() + ".");
                this.currentUser = account;
            } else {
                System.out.println("Invalid credentials. Try again.");
            }
        } else if (choice.equals("2")) {
            System.out.println("Goodbye!");
            System.exit(0);
        } else {
            System.out.println("Invalid choice.");
        }
    }

    private void showAdminMenu() {
        System.out.println("\n--- ADMIN DASHBOARD ---");
        System.out.println("1. Add New User");
        System.out.println("2. Remove Existing User");
        System.out.println("3. Add Book to Library");
        System.out.println("4. Remove Book");
        System.out.println("5. View All Books");
        System.out.println("6. Issue Book to User");
        System.out.println("7. View Issued Books Status");
        System.out.println("8. Logout");
        System.out.print("Choose option: ");

        String choice = scanner.nextLine();
        switch (choice) {
            case "1":
                System.out.print("Enter new username: ");
                String newU = scanner.nextLine();
                System.out.print("Enter password: ");
                String newP = scanner.nextLine();
                if(libraryService.registerUser(newU, newP)) {
                    System.out.println("User created successfully!");
                } else {
                    System.out.println("Username already exists.");
                }
                break;
            case "2":
                System.out.print("Enter username to remove: ");
                String rU = scanner.nextLine();
                if(libraryService.removeUser(rU)) {
                    System.out.println("User removed successfully!");
                } else {
                    System.out.println("User not found or is an admin.");
                }
                break;
            case "3":
                System.out.print("Enter book title: ");
                String title = scanner.nextLine();
                System.out.print("Enter book author: ");
                String author = scanner.nextLine();
                libraryService.addBook(title, author);
                System.out.println("Book added successfully.");
                break;
            case "4":
                System.out.print("Enter Book ID to remove: ");
                String removedId = scanner.nextLine();
                if(libraryService.removeBook(removedId)) {
                    System.out.println("Book removed successfully!");
                } else {
                    System.out.println("Book not found.");
                }
                break;
            case "5":
                List<Book> allBooks = libraryService.getAllBooks();
                System.out.println("\n--- All Books ---");
                if (allBooks.isEmpty()) {
                    System.out.println("No books in the library.");
                }
                for (Book b : allBooks) {
                    System.out.println(b);
                }
                break;
            case "6":
                System.out.print("Enter Book ID to issue: ");
                String issueId = scanner.nextLine();
                System.out.print("Enter User ID to issue to: ");
                String userId = scanner.nextLine();
                if(libraryService.issueBook(issueId, userId, 14)) {
                    System.out.println("Book issued successfully.");
                } else {
                    System.out.println("Failed to issue book (Either unavailable or doesn't exist).");
                }
                break;
            case "7":
                List<Book> issuedBooks = libraryService.getIssuedBooks();
                System.out.println("\n--- Issued Books ---");
                if (issuedBooks.isEmpty()) {
                    System.out.println("No books are currently issued.");
                }
                for (Book b : issuedBooks) {
                    System.out.println(b + " -> Issued To: " + b.getIssuedToUserId());
                }
                break;
            case "8":
                this.currentUser = null;
                System.out.println("Logged out successfully.");
                break;
            default:
                System.out.println("Invalid option.");
        }
    }

    private void showUserMenu() {
        System.out.println("\n--- USER DASHBOARD ---");
        System.out.println("1. View Available Books");
        System.out.println("2. Search Books");
        System.out.println("3. Request/Borrow Book");
        System.out.println("4. Return Book");
        System.out.println("5. View My Issued Books");
        System.out.println("6. Logout");
        System.out.print("Choose option: ");

        String choice = scanner.nextLine();
        switch (choice) {
            case "1":
                List<Book> avBooks = libraryService.getAvailableBooks();
                System.out.println("\n--- Available Books ---");
                if (avBooks.isEmpty()) {
                    System.out.println("No books currently available.");
                }
                for (Book b : avBooks) {
                    System.out.println(b);
                }
                break;
            case "2":
                System.out.print("Enter Title or Author to search: ");
                String query = scanner.nextLine();
                List<Book> matching = libraryService.searchBooks(query);
                System.out.println("\n--- Search Results ---");
                if (matching.isEmpty()) {
                    System.out.println("No matches found.");
                }
                for (Book b : matching) {
                    System.out.println(b);
                }
                break;
            case "3":
                System.out.print("Enter Book ID you want to request/borrow: ");
                String reqId = scanner.nextLine();
                if (libraryService.requestBook(reqId, currentUser.getId())) {
                    System.out.println("Book successfully issued to you (Standard 14 days)!");
                } else {
                    System.out.println("Could not issue book. It might be unavailable or invalid ID.");
                }
                break;
            case "4":
                System.out.print("Enter Book ID to return: ");
                String retId = scanner.nextLine();
                List<Book> myBooks = libraryService.getBooksIssuedTo(currentUser.getId());
                boolean isMine = myBooks.stream().anyMatch(b -> b.getId().equals(retId));
                if (isMine) {
                    double fine = libraryService.returnBook(retId);
                    System.out.println("Book returned successfully.");
                    if (fine > 0) {
                        System.out.println("Notice: Passed due date. Late Fine calculated: $" + fine);
                    }
                } else {
                    System.out.println("You do not have a book with this ID issued to you.");
                }
                break;
            case "5":
                List<Book> currentlyIssued = libraryService.getBooksIssuedTo(currentUser.getId());
                System.out.println("\n--- My Issued Books ---");
                if (currentlyIssued.isEmpty()) {
                    System.out.println("You have no issued books.");
                }
                for (Book b : currentlyIssued) {
                    System.out.println(b);
                }
                break;
            case "6":
                this.currentUser = null;
                System.out.println("Logged out successfully.");
                break;
            default:
                System.out.println("Invalid option.");
        }
    }
}
