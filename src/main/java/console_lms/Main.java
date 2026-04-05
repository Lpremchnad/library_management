package console_lms;

import console_lms.gui.LoginFrame;
import console_lms.gui.Theme;
import console_lms.service.LibraryService;

import javax.swing.*;

/**
 * Main entry point for the Library Management System.
 * Applies the dark violet theme globally and bootstraps the GUI.
 */
public class Main {
    public static void main(String[] args) {
        // Initialize the core logic engine
        LibraryService service = new LibraryService();

        // Bootstrap: ensure admin and sample users exist
        boolean createdUsers = service.ensureAdminExists();
        if (createdUsers) {
            System.out.println("* Default Admin created [username: admin, password: admin123]");
            System.out.println("* Default User created  [username: user,  password: user123]");
            System.out.println("* Default User created  [username: user2, password: user234]");
        }

        // Bootstrap: ensure sample books exist
        boolean createdBooks = service.ensureSampleBooks();
        if (createdBooks) {
            System.out.println("* Sample books preloaded (8 books)");
        }

        // Apply the dark violet global theme
        Theme.applyGlobalTheme();

        // Set the cross-platform look and feel (disables native look for consistency)
        try {
            UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
            // Re-apply our dark theme on top of cross-platform L&F
            Theme.applyGlobalTheme();
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Launch the GUI on the Event Dispatch Thread
        SwingUtilities.invokeLater(() -> {
            LoginFrame loginFrame = new LoginFrame(service);
            loginFrame.setVisible(true);
        });
    }
}
