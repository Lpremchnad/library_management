package console_lms.gui;

import console_lms.models.Account;
import console_lms.models.Book;
import console_lms.service.LibraryService;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

/**
 * User Dashboard with dark violet sidebar layout.
 * Features: view available books, see issued books, borrow/return books.
 */
public class UserDashboard extends JFrame {

    private final LibraryService service;
    private final Account user;
    private JPanel cardPanel;
    private CardLayout cardLayout;

    public UserDashboard(LibraryService service, Account user) {
        this.service = service;
        this.user = user;

        setTitle("Library Pro - " + user.getUsername());
        setSize(1050, 680);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(0, 0));
        getContentPane().setBackground(Theme.BG_DARK);

        add(createSidebar(), BorderLayout.WEST);

        cardLayout = new CardLayout();
        cardPanel = new JPanel(cardLayout);
        cardPanel.setBackground(Theme.BG_DARK);

        cardPanel.add(createDashboardPanel(), "Dashboard");
        cardPanel.add(createCatalogPanel(), "Catalog");
        cardPanel.add(createMyBooksPanel(), "MyBooks");

        add(cardPanel, BorderLayout.CENTER);
    }

    // ========== SIDEBAR ==========

    private JPanel createSidebar() {
        JPanel sidebar = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                GradientPaint gp = new GradientPaint(0, 0, Theme.SIDEBAR_BG,
                        0, getHeight(), new Color(28, 28, 48));
                g2.setPaint(gp);
                g2.fillRect(0, 0, getWidth(), getHeight());
                g2.dispose();
            }
        };
        sidebar.setLayout(new BoxLayout(sidebar, BoxLayout.Y_AXIS));
        sidebar.setPreferredSize(new Dimension(240, getHeight()));
        sidebar.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));

        // Logo / Title
        JPanel logoPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        logoPanel.setOpaque(false);
        logoPanel.setMaximumSize(new Dimension(240, 60));
        JLabel logoIcon = new JLabel("\uD83D\uDCDA");
        logoIcon.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 28));
        JLabel title = new JLabel("Library Pro");
        title.setFont(Theme.FONT_TITLE);
        title.setForeground(Theme.PURPLE_LIGHT);
        logoPanel.add(logoIcon);
        logoPanel.add(title);
        sidebar.add(logoPanel);
        sidebar.add(Box.createRigidArea(new Dimension(0, 5)));

        // Welcome message
        JLabel welcome = new JLabel("Welcome, " + user.getUsername());
        welcome.setFont(Theme.FONT_SMALL);
        welcome.setForeground(Theme.TEXT_MUTED);
        welcome.setAlignmentX(Component.CENTER_ALIGNMENT);
        sidebar.add(welcome);
        sidebar.add(Box.createRigidArea(new Dimension(0, 8)));

        // Separator
        JSeparator sep = new JSeparator();
        sep.setForeground(new Color(60, 60, 90));
        sep.setMaximumSize(new Dimension(200, 2));
        sidebar.add(sep);
        sidebar.add(Box.createRigidArea(new Dimension(0, 15)));

        // Menu Label
        JLabel menuLabel = new JLabel("  MENU");
        menuLabel.setFont(new Font("Segoe UI", Font.BOLD, 11));
        menuLabel.setForeground(Theme.TEXT_MUTED);
        menuLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        menuLabel.setBorder(BorderFactory.createEmptyBorder(0, 20, 5, 0));
        sidebar.add(menuLabel);

        // Menu buttons
        JButton dashBtn = Theme.createSidebarButton("Dashboard", "\u25A3");
        dashBtn.addActionListener(e -> cardLayout.show(cardPanel, "Dashboard"));
        sidebar.add(dashBtn);

        JButton catalogBtn = Theme.createSidebarButton("Browse Catalog", "\uD83D\uDD0D");
        catalogBtn.addActionListener(e -> cardLayout.show(cardPanel, "Catalog"));
        sidebar.add(catalogBtn);

        JButton myBooksBtn = Theme.createSidebarButton("My Books", "\uD83D\uDCD6");
        myBooksBtn.addActionListener(e -> cardLayout.show(cardPanel, "MyBooks"));
        sidebar.add(myBooksBtn);

        sidebar.add(Box.createVerticalGlue());

        // Logout
        JButton logoutBtn = Theme.createSidebarButton("Logout", "\u2716");
        logoutBtn.setForeground(new Color(255, 150, 150));
        logoutBtn.addActionListener(e -> {
            dispose();
            new LoginFrame(service).setVisible(true);
        });
        sidebar.add(logoutBtn);

        return sidebar;
    }

    // ========== DASHBOARD ==========

    private JPanel createDashboardPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Theme.BG_DARK);
        panel.setBorder(BorderFactory.createEmptyBorder(25, 30, 25, 30));

        // Header
        JLabel header = new JLabel("Dashboard");
        header.setFont(Theme.FONT_TITLE);
        header.setForeground(Theme.TEXT_WHITE);
        panel.add(header, BorderLayout.NORTH);

        // Stats cards
        JPanel statsContainer = new JPanel(new GridLayout(1, 3, 20, 0));
        statsContainer.setOpaque(false);
        statsContainer.setBorder(BorderFactory.createEmptyBorder(25, 0, 25, 0));

        // The counts update when the panel is shown
        JPanel booksCard = Theme.createStatCard("\uD83D\uDCDA",
                String.valueOf(service.getTotalBooks()), "Books in Library", Theme.PURPLE_ACCENT);
        JPanel availCard = Theme.createStatCard("\u2705",
                String.valueOf(service.getAvailableBooksCount()), "Available", Theme.SUCCESS_COLOR);
        JPanel myCard = Theme.createStatCard("\uD83D\uDCD6",
                String.valueOf(service.getBooksIssuedTo(user.getId()).size()),
                "My Borrowed Books", Theme.WARNING_COLOR);

        statsContainer.add(booksCard);
        statsContainer.add(availCard);
        statsContainer.add(myCard);

        JPanel centerPanel = new JPanel(new BorderLayout());
        centerPanel.setOpaque(false);
        centerPanel.add(statsContainer, BorderLayout.NORTH);

        // Welcome message card
        JPanel welcomeCard = Theme.createCardPanel();
        welcomeCard.setLayout(new BorderLayout());
        welcomeCard.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));

        JPanel innerContent = new JPanel();
        innerContent.setOpaque(false);
        innerContent.setLayout(new BoxLayout(innerContent, BoxLayout.Y_AXIS));

        JLabel welcomeTitle = new JLabel("Welcome back, " + user.getUsername() + "!");
        welcomeTitle.setFont(Theme.FONT_SUBTITLE);
        welcomeTitle.setForeground(Theme.TEXT_WHITE);
        welcomeTitle.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel welcomeDesc = new JLabel("<html>Use the sidebar to browse the catalog, borrow books, or check your currently borrowed books.</html>");
        welcomeDesc.setFont(Theme.FONT_REGULAR);
        welcomeDesc.setForeground(Theme.TEXT_MUTED);
        welcomeDesc.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel borrowedInfo = new JLabel("You have " + service.getBooksIssuedTo(user.getId()).size() + " book(s) currently borrowed.");
        borrowedInfo.setFont(Theme.FONT_BOLD);
        borrowedInfo.setForeground(Theme.PURPLE_LIGHT);
        borrowedInfo.setAlignmentX(Component.LEFT_ALIGNMENT);

        innerContent.add(welcomeTitle);
        innerContent.add(Box.createRigidArea(new Dimension(0, 10)));
        innerContent.add(welcomeDesc);
        innerContent.add(Box.createRigidArea(new Dimension(0, 15)));
        innerContent.add(borrowedInfo);

        welcomeCard.add(innerContent, BorderLayout.CENTER);
        centerPanel.add(welcomeCard, BorderLayout.CENTER);

        panel.add(centerPanel, BorderLayout.CENTER);

        // Auto-refresh stats when panel is shown
        panel.addComponentListener(new java.awt.event.ComponentAdapter() {
            @Override
            public void componentShown(java.awt.event.ComponentEvent evt) {
                int myBooks = service.getBooksIssuedTo(user.getId()).size();
                borrowedInfo.setText("You have " + myBooks + " book(s) currently borrowed.");
            }
        });

        return panel;
    }

    // ========== CATALOG ==========

    private JPanel createCatalogPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Theme.BG_DARK);
        panel.setBorder(BorderFactory.createEmptyBorder(25, 30, 25, 30));

        // Header with back
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setOpaque(false);
        JButton backBtn = new JButton("\u2190 Back");
        Theme.styleButton(backBtn);
        backBtn.addActionListener(e -> cardLayout.show(cardPanel, "Dashboard"));
        headerPanel.add(backBtn, BorderLayout.WEST);

        // Search bar
        JPanel searchGroup = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        searchGroup.setOpaque(false);
        JTextField searchField = new JTextField(18);
        Theme.styleTextField(searchField);
        searchField.setEditable(true);
        searchField.setEnabled(true);
        searchField.setFocusable(true);
        JButton searchBtn = new JButton("Search");
        Theme.styleButton(searchBtn);
        searchGroup.add(searchField);
        searchGroup.add(searchBtn);
        headerPanel.add(searchGroup, BorderLayout.EAST);

        JLabel header = new JLabel("  Browse Catalog");
        header.setFont(Theme.FONT_TITLE);
        header.setForeground(Theme.TEXT_WHITE);
        headerPanel.add(header, BorderLayout.CENTER);

        panel.add(headerPanel, BorderLayout.NORTH);

        // Table
        String[] cols = {"ID", "ISBN", "Title", "Author", "Publisher", "Price", "Status"};
        DefaultTableModel model = new DefaultTableModel(cols, 0) {
            @Override
            public boolean isCellEditable(int row, int col) { return false; }
        };
        JTable table = new JTable(model);
        Theme.styleTable(table);
        JScrollPane scrollPane = new JScrollPane(table);
        Theme.styleScrollPane(scrollPane);
        scrollPane.setBorder(BorderFactory.createEmptyBorder(15, 0, 15, 0));
        panel.add(scrollPane, BorderLayout.CENTER);

        // Footer Actions
        JPanel actionPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        actionPanel.setOpaque(false);
        // Borrow with days input
        JPanel borrowPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        borrowPanel.setOpaque(false);
        JTextField daysField = new JTextField("14", 5);
        Theme.styleTextField(daysField);
        daysField.setEditable(true);
        daysField.setEnabled(true);
        daysField.setFocusable(true);
        daysField.setColumns(3);
        JLabel daysLabel = new JLabel("Days:");
        daysLabel.setForeground(Theme.TEXT_LIGHT);
        daysLabel.setFont(Theme.FONT_REGULAR);
        JButton reqBtn = new JButton("Borrow Selected");
        Theme.styleButton(reqBtn);

        borrowPanel.add(daysLabel);
        borrowPanel.add(daysField);
        borrowPanel.add(reqBtn);

        reqBtn.addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row >= 0) {
                String bId = (String) model.getValueAt(row, 0);
                String status = (String) model.getValueAt(row, 6);
                if ("Issued".equals(status)) {
                    JOptionPane.showMessageDialog(this, "This book is currently unavailable.");
                    return;
                }
                try {
                    int days = Integer.parseInt(daysField.getText().trim());
                    if (days < 1 || days > 30) {
                        JOptionPane.showMessageDialog(this, "Days must be between 1 and 30.");
                        return;
                    }
                    if (service.issueBook(bId, user.getId(), days)) {
                        JOptionPane.showMessageDialog(this, "Book borrowed! Due in " + days + " days.");
                        searchBtn.doClick(); // Refresh
                    } else {
                        JOptionPane.showMessageDialog(this, "Could not borrow this book (check availability).");
                    }
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(this, "Enter valid number of days (1-30).");
                }
            } else {
                JOptionPane.showMessageDialog(this, "Select a book to borrow.");
            }
        });

        actionPanel.add(borrowPanel);

        actionPanel.add(reqBtn);
        panel.add(actionPanel, BorderLayout.SOUTH);

        // Search logic
        Runnable loadData = () -> {
            model.setRowCount(0);
            String q = searchField.getText().trim();
            List<Book> res = q.isEmpty() ? service.getAllBooks() : service.searchBooks(q);
            for (Book b : res) {
                model.addRow(new Object[]{b.getId(), b.getIsbn(), b.getTitle(), b.getAuthor(),
                        b.getPublisher(), String.format("$%.2f", b.getPrice()),
                        b.isAvailable() ? "Available" : "Issued"});
            }
        };

        searchBtn.addActionListener(e -> loadData.run());
        loadData.run(); // Initial load

        // Auto-refresh when shown
        panel.addComponentListener(new java.awt.event.ComponentAdapter() {
            @Override
            public void componentShown(java.awt.event.ComponentEvent evt) {
                loadData.run();
            }
        });

        return panel;
    }

    // ========== MY BOOKS ==========

    private JPanel createMyBooksPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Theme.BG_DARK);
        panel.setBorder(BorderFactory.createEmptyBorder(25, 30, 25, 30));

        // Header with back
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setOpaque(false);
        JButton backBtn = new JButton("\u2190 Back");
        Theme.styleButton(backBtn);
        backBtn.addActionListener(e -> cardLayout.show(cardPanel, "Dashboard"));
        headerPanel.add(backBtn, BorderLayout.WEST);
        JLabel header = new JLabel("  My Borrowed Books");
        header.setFont(Theme.FONT_TITLE);
        header.setForeground(Theme.TEXT_WHITE);
        headerPanel.add(header, BorderLayout.CENTER);
        panel.add(headerPanel, BorderLayout.NORTH);

        // Info card
        JLabel countLabel = new JLabel();
        countLabel.setFont(Theme.FONT_BOLD);
        countLabel.setForeground(Theme.PURPLE_LIGHT);
        countLabel.setBorder(BorderFactory.createEmptyBorder(10, 0, 5, 0));

        // Table
        String[] cols = {"ID", "Title", "Author", "Issue Date", "Due Date"};
        DefaultTableModel model = new DefaultTableModel(cols, 0) {
            @Override
            public boolean isCellEditable(int row, int col) { return false; }
        };
        JTable table = new JTable(model);
        Theme.styleTable(table);
        JScrollPane scrollPane = new JScrollPane(table);
        Theme.styleScrollPane(scrollPane);
        scrollPane.setBorder(BorderFactory.createEmptyBorder(10, 0, 15, 0));

        JPanel centerPanel = new JPanel(new BorderLayout());
        centerPanel.setOpaque(false);
        centerPanel.add(countLabel, BorderLayout.NORTH);
        centerPanel.add(scrollPane, BorderLayout.CENTER);
        panel.add(centerPanel, BorderLayout.CENTER);

        // Return button
        JPanel botPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        botPanel.setOpaque(false);
        JButton returnBtn = new JButton("Return Selected Book");
        Theme.styleButton(returnBtn);
        botPanel.add(returnBtn);
        panel.add(botPanel, BorderLayout.SOUTH);

        Runnable refresh = () -> {
            model.setRowCount(0);
            List<Book> myBooks = service.getBooksIssuedTo(user.getId());
            for (Book b : myBooks) {
                model.addRow(new Object[]{b.getId(), b.getTitle(), b.getAuthor(),
                        b.getIssuedDate(), b.getDueDate()});
            }
            countLabel.setText("Total books borrowed: " + myBooks.size());
        };

        returnBtn.addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row >= 0) {
                String bId = (String) model.getValueAt(row, 0);
                double fine = service.returnBook(bId);
                StringBuilder msg = new StringBuilder("Book returned successfully!");
                if (fine > 0) {
                    msg.append(String.format("\nOverdue fine: $%.2f", fine));
                }
                JOptionPane.showMessageDialog(this, msg.toString());
                refresh.run();
            } else {
                JOptionPane.showMessageDialog(this, "Select a book to return.");
            }
        });

        // Auto-refresh
        panel.addComponentListener(new java.awt.event.ComponentAdapter() {
            @Override
            public void componentShown(java.awt.event.ComponentEvent evt) {
                refresh.run();
            }
        });

        refresh.run();

        return panel;
    }
}
