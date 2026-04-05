package console_lms.gui;

import console_lms.models.Account;
import console_lms.models.Book;
import console_lms.service.LibraryService;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

/**
 * Admin Dashboard with dark violet sidebar layout.
 * Features: Dashboard stats, Add Book form, Add User, Issue Book panel, Manage Books table.
 */
public class AdminDashboard extends JFrame {
    private final LibraryService service;
    private final Account admin;
    private JPanel cardPanel;
    private CardLayout cardLayout;
    private DefaultTableModel dashboardTableModel;
    private DefaultTableModel manageBooksTableModel;

    public AdminDashboard(LibraryService service, Account admin) {
        this.service = service;
        this.admin = admin;

        setTitle("Library Pro - Admin Dashboard");
        setSize(1100, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(0, 0));
        getContentPane().setBackground(Theme.BG_DARK);

        // Sidebar
        add(createSidebar(), BorderLayout.WEST);

        // Content Area (CardLayout)
        cardLayout = new CardLayout();
        cardPanel = new JPanel(cardLayout);
        cardPanel.setBackground(Theme.BG_DARK);

        cardPanel.add(createDashboardPanel(), "Dashboard");
        cardPanel.add(createManageBooksPanel(), "ManageBooks");
        cardPanel.add(createAddBookPanel(), "AddBook");
        cardPanel.add(createManageUsersPanel(), "ManageUsers");
        cardPanel.add(createIssueBookPanel(), "IssueBook");
        cardPanel.add(createIssuedBooksPanel(), "IssuedBooks");

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
        JLabel welcome = new JLabel("Welcome, " + admin.getUsername());
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
        JLabel menuLabel = new JLabel("  MAIN MENU");
        menuLabel.setFont(new Font("Segoe UI", Font.BOLD, 11));
        menuLabel.setForeground(Theme.TEXT_MUTED);
        menuLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        menuLabel.setBorder(BorderFactory.createEmptyBorder(0, 20, 5, 0));
        sidebar.add(menuLabel);

        // Menu buttons
        JButton dashBtn = Theme.createSidebarButton("Dashboard", "\u25A3");
        dashBtn.addActionListener(e -> cardLayout.show(cardPanel, "Dashboard"));
        sidebar.add(dashBtn);

        JButton addBookBtn = Theme.createSidebarButton("Add Book", "\u002B");
        addBookBtn.addActionListener(e -> cardLayout.show(cardPanel, "AddBook"));
        sidebar.add(addBookBtn);

        JButton manageBooksBtn = Theme.createSidebarButton("Manage Books", "\u2637");
        manageBooksBtn.addActionListener(e -> cardLayout.show(cardPanel, "ManageBooks"));
        sidebar.add(manageBooksBtn);

        JButton addUserBtn = Theme.createSidebarButton("Add User", "\u263A");
        addUserBtn.addActionListener(e -> cardLayout.show(cardPanel, "ManageUsers"));
        sidebar.add(addUserBtn);

        JButton issueBtn = Theme.createSidebarButton("Issue Book", "\u2794");
        issueBtn.addActionListener(e -> cardLayout.show(cardPanel, "IssueBook"));
        sidebar.add(issueBtn);

        JButton issuedBtn = Theme.createSidebarButton("Issued Books", "\u2611");
        issuedBtn.addActionListener(e -> cardLayout.show(cardPanel, "IssuedBooks"));
        sidebar.add(issuedBtn);

        sidebar.add(Box.createVerticalGlue());

        // Logout button (red)
        JButton logoutBtn = Theme.createSidebarButton("Logout", "\u2716");
        logoutBtn.setForeground(new Color(255, 150, 150));
        logoutBtn.addActionListener(e -> {
            dispose();
            new LoginFrame(service).setVisible(true);
        });
        sidebar.add(logoutBtn);

        return sidebar;
    }

    // ========== DASHBOARD STATS PANEL ==========

    private JPanel createDashboardPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Theme.BG_DARK);
        panel.setBorder(BorderFactory.createEmptyBorder(25, 30, 25, 30));

        // Header
        JLabel header = new JLabel("Dashboard");
        header.setFont(Theme.FONT_TITLE);
        header.setForeground(Theme.TEXT_WHITE);
        panel.add(header, BorderLayout.NORTH);

        // Stats Cards
        JPanel statsContainer = new JPanel(new GridLayout(1, 4, 20, 0));
        statsContainer.setOpaque(false);
        statsContainer.setBorder(BorderFactory.createEmptyBorder(25, 0, 25, 0));

        statsContainer.add(Theme.createStatCard("\uD83D\uDCDA",
                String.valueOf(service.getTotalBooks()), "Total Books", Theme.PURPLE_ACCENT));
        statsContainer.add(Theme.createStatCard("\u2705",
                String.valueOf(service.getAvailableBooksCount()), "Available", Theme.SUCCESS_COLOR));
        statsContainer.add(Theme.createStatCard("\uD83D\uDCE4",
                String.valueOf(service.getIssuedBooksCount()), "Issued", Theme.WARNING_COLOR));
        statsContainer.add(Theme.createStatCard("\uD83D\uDC64",
                String.valueOf(service.getTotalUsers()), "Total Users", new Color(100, 160, 255)));

        // Content with table at bottom
        JPanel centerPanel = new JPanel(new BorderLayout());
        centerPanel.setOpaque(false);
        centerPanel.add(statsContainer, BorderLayout.NORTH);

        // Recent books table (quick overview)
        JPanel tableCard = Theme.createCardPanel();
        tableCard.setLayout(new BorderLayout());
        tableCard.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        JLabel tableTitle = new JLabel("Library Catalog Overview");
        tableTitle.setFont(Theme.FONT_SUBTITLE);
        tableTitle.setForeground(Theme.TEXT_WHITE);
        tableTitle.setBorder(BorderFactory.createEmptyBorder(0, 5, 10, 0));
        tableCard.add(tableTitle, BorderLayout.NORTH);

        String[] cols = {"ID", "ISBN", "Book Name", "Publisher", "Price"};
        DefaultTableModel model = new DefaultTableModel(cols, 0) {
            @Override
            public boolean isCellEditable(int row, int col) { return false; }
        };
        dashboardTableModel = model;
        JTable table = new JTable(model);
        Theme.styleTable(table);
        refreshCatalogTable(model);

        JScrollPane sp = new JScrollPane(table);
        Theme.styleScrollPane(sp);
        tableCard.add(sp, BorderLayout.CENTER);

        centerPanel.add(tableCard, BorderLayout.CENTER);

        panel.add(centerPanel, BorderLayout.CENTER);
        return panel;
    }

    private void refreshCatalogTable(DefaultTableModel model) {
        model.setRowCount(0);
        for (Book b : service.getAllBooks()) {
            model.addRow(new Object[]{b.getId(), b.getIsbn(), b.getTitle(),
                    b.getPublisher(), String.format("$%.2f", b.getPrice())});
        }
    }

    // ========== MANAGE BOOKS PANEL ==========

    private JPanel createManageBooksPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Theme.BG_DARK);
        panel.setBorder(BorderFactory.createEmptyBorder(25, 30, 25, 30));

        // Header with back button
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setOpaque(false);

        JButton backBtn = new JButton("\u2190 Back");
        Theme.styleButton(backBtn);
        backBtn.addActionListener(e -> cardLayout.show(cardPanel, "Dashboard"));
        headerPanel.add(backBtn, BorderLayout.WEST);

        JLabel header = new JLabel("Manage Books Catalog");
        header.setFont(Theme.FONT_TITLE);
        header.setForeground(Theme.TEXT_WHITE);
        header.setBorder(BorderFactory.createEmptyBorder(0, 15, 0, 0));
        headerPanel.add(header, BorderLayout.CENTER);

        panel.add(headerPanel, BorderLayout.NORTH);

        // Table
        String[] cols = {"ID", "ISBN", "Title", "Author", "Publisher", "Price", "Status"};
        DefaultTableModel tModel = new DefaultTableModel(cols, 0) {
            @Override
            public boolean isCellEditable(int row, int col) { return false; }
        };
        manageBooksTableModel = tModel;
        JTable table = new JTable(tModel);
        Theme.styleTable(table);
        refreshBooksTable(tModel);

        JScrollPane scrollPane = new JScrollPane(table);
        Theme.styleScrollPane(scrollPane);
        scrollPane.setBorder(BorderFactory.createEmptyBorder(15, 0, 15, 0));
        panel.add(scrollPane, BorderLayout.CENTER);

        // Action buttons
        JPanel actPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        actPanel.setOpaque(false);

        JButton editBtn = new JButton("Edit Selected");
        Theme.styleButton(editBtn);
        JButton remBtn = new JButton("Remove Selected");
        remBtn.setBackground(Theme.DANGER_BTN);
        Theme.styleButton(remBtn);
        JButton refreshBtn = new JButton("Refresh");
        Theme.styleButton(refreshBtn);

        editBtn.addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row >= 0) {
                String bookId = (String) tModel.getValueAt(row, 0);
                showEditBookDialog(bookId, tModel);
            } else {
                JOptionPane.showMessageDialog(this, "Select a book to edit.");
            }
        });

        remBtn.addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row >= 0) {
                String bookId = (String) tModel.getValueAt(row, 0);
                int confirm = JOptionPane.showConfirmDialog(this,
                        "Are you sure you want to remove this book?", "Confirm Delete",
                        JOptionPane.YES_NO_OPTION);
                if (confirm == JOptionPane.YES_OPTION) {
                    service.removeBook(bookId);
                    refreshBooksTable(tModel);
                }
            } else {
                JOptionPane.showMessageDialog(this, "Select a book first.");
            }
        });

        refreshBtn.addActionListener(e -> refreshBooksTable(tModel));

        actPanel.add(editBtn);
        actPanel.add(remBtn);
        actPanel.add(refreshBtn);
        panel.add(actPanel, BorderLayout.SOUTH);

        return panel;
    }

    private void showEditBookDialog(String bookId, DefaultTableModel tModel) {
        Book book = service.getAllBooks().stream()
                .filter(b -> b.getId().equals(bookId)).findFirst().orElse(null);
        if (book == null) return;

        JPanel formPanel = new JPanel(new GridLayout(7, 2, 10, 8));
        formPanel.setBackground(Theme.BG_CARD);

        JTextField isbnF = new JTextField(book.getIsbn());
        JTextField titleF = new JTextField(book.getTitle());
        JTextField authorF = new JTextField(book.getAuthor());
        JTextField publisherF = new JTextField(book.getPublisher());
        JTextField editionF = new JTextField(book.getEdition());
        JTextField genreF = new JTextField(book.getGenre());
        JTextField priceF = new JTextField(String.valueOf(book.getPrice()));

        Theme.styleTextField(isbnF);
        isbnF.setEditable(true);
        isbnF.setEnabled(true);
        isbnF.setFocusable(true);
        Theme.styleTextField(titleF);
        titleF.setEditable(true);
        titleF.setEnabled(true);
        titleF.setFocusable(true);
        Theme.styleTextField(authorF);
        authorF.setEditable(true);
        authorF.setEnabled(true);
        authorF.setFocusable(true);
        Theme.styleTextField(publisherF);
        publisherF.setEditable(true);
        publisherF.setEnabled(true);
        publisherF.setFocusable(true);
        Theme.styleTextField(editionF);
        editionF.setEditable(true);
        editionF.setEnabled(true);
        editionF.setFocusable(true);
        Theme.styleTextField(genreF);
        genreF.setEditable(true);
        genreF.setEnabled(true);
        genreF.setFocusable(true);
        Theme.styleTextField(priceF);
        priceF.setEditable(true);
        priceF.setEnabled(true);
        priceF.setFocusable(true);

        formPanel.add(createFormLabel("ISBN:")); formPanel.add(isbnF);
        formPanel.add(createFormLabel("Title:")); formPanel.add(titleF);
        formPanel.add(createFormLabel("Author:")); formPanel.add(authorF);
        formPanel.add(createFormLabel("Publisher:")); formPanel.add(publisherF);
        formPanel.add(createFormLabel("Edition:")); formPanel.add(editionF);
        formPanel.add(createFormLabel("Genre:")); formPanel.add(genreF);
        formPanel.add(createFormLabel("Price:")); formPanel.add(priceF);

        int result = JOptionPane.showConfirmDialog(this, formPanel, "Edit Book", JOptionPane.OK_CANCEL_OPTION);
        if (result == JOptionPane.OK_OPTION) {
            try {
                double price = Double.parseDouble(priceF.getText().trim());
                service.editBookFull(bookId, isbnF.getText().trim(), titleF.getText().trim(),
                        authorF.getText().trim(), publisherF.getText().trim(),
                        editionF.getText().trim(), genreF.getText().trim(), price, book.getPages());
                refreshBooksTable(tModel);
                JOptionPane.showMessageDialog(this, "Book updated successfully!");
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Invalid price value.");
            }
        }
    }

    private void refreshBooksTable(DefaultTableModel model) {
        model.setRowCount(0);
        for (Book b : service.getAllBooks()) {
            model.addRow(new Object[]{b.getId(), b.getIsbn(), b.getTitle(), b.getAuthor(),
                    b.getPublisher(), String.format("$%.2f", b.getPrice()),
                    b.isAvailable() ? "Available" : "Issued"});
        }
    }

    private void refreshAllViews() {
        if (dashboardTableModel != null) {
            refreshCatalogTable(dashboardTableModel);
        }
        if (manageBooksTableModel != null) {
            refreshBooksTable(manageBooksTableModel);
        }
    }

    // ========== ADD BOOK PANEL ==========

    private JPanel createAddBookPanel() {
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
        JLabel header = new JLabel("Add New Book");
        header.setFont(Theme.FONT_TITLE);
        header.setForeground(Theme.TEXT_WHITE);
        header.setBorder(BorderFactory.createEmptyBorder(0, 15, 0, 0));
        headerPanel.add(header, BorderLayout.CENTER);
        panel.add(headerPanel, BorderLayout.NORTH);

        // Form card
        JPanel formCard = Theme.createCardPanel();
        formCard.setLayout(new GridBagLayout());
        formCard.setBorder(BorderFactory.createEmptyBorder(40, 50, 40, 50));  // Larger spacing

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(12, 15, 12, 15);  // More padding between fields

        // Fields - explicitly editable
        JTextField isbnField = new JTextField(20);
        JTextField nameField = new JTextField(20);
        JTextField authorField = new JTextField(20);
        JTextField publisherField = new JTextField(20);
        JTextField editionField = new JTextField(20);
        JTextField genreField = new JTextField(20);
        JTextField priceField = new JTextField(20);
        JTextField pagesField = new JTextField(20);

        // Style all fields
        JTextField[] fields = {isbnField, nameField, authorField, publisherField,
                editionField, genreField, priceField, pagesField};
        for (JTextField f : fields) {
            Theme.styleTextField(f);
            f.setEditable(true);
            f.setEnabled(true);
            f.setFocusable(true);
        }

        // Layout: 2 columns of label-field pairs
        String[] labels = {"ISBN", "Book Name", "Author", "Publisher", "Edition", "Genre", "Price", "Pages"};

        for (int i = 0; i < labels.length; i++) {
            int row = i / 2;
            int col = (i % 2) * 2;

            gbc.gridx = col;
            gbc.gridy = row;
            gbc.weightx = 0;
            JLabel lbl = new JLabel(labels[i]);
            lbl.setFont(Theme.FONT_BOLD);
            lbl.setForeground(Theme.TEXT_LIGHT);
            formCard.add(lbl, gbc);

            gbc.gridx = col + 1;
            gbc.weightx = 1.0;
            formCard.add(fields[i], gbc);
        }

        // Save button
        gbc.gridx = 0;
        gbc.gridy = (labels.length / 2) + 1;
        gbc.gridwidth = 4;
        gbc.insets = new Insets(30, 20, 20, 20);
        JButton saveBtn = new JButton("Save Book");
        Theme.styleButton(saveBtn);
        saveBtn.setPreferredSize(new Dimension(240, 50));  // Larger button

        saveBtn.addActionListener(e -> {
    System.out.println("=== Add Book Button Clicked ===");
    try {
        String isbn = isbnField.getText().trim();
        String title = nameField.getText().trim();
        String author = authorField.getText().trim();
        
        if (isbn.isEmpty() || title.isEmpty() || author.isEmpty() || priceField.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Fill all required fields: ISBN, Book Name, Author, Price");
            return;
        }

        double price = Double.parseDouble(priceField.getText().trim());
        int pages = pagesField.getText().trim().isEmpty() ? 0 : Integer.parseInt(pagesField.getText().trim());

        System.out.println("Book Added: " + title);
        
        service.addBook(isbn, title, author, publisherField.getText().trim(),
            editionField.getText().trim(), genreField.getText().trim(), price, pages);

        System.out.println("Book saved successfully");

        JOptionPane.showMessageDialog(this, "Book Added Successfully!");

        // Clear all fields
        for (JTextField f : fields) {
            f.setText("");
        }

        refreshAllViews();

    } catch (NumberFormatException ex) {
        System.err.println("Number parse error: " + ex.getMessage());
        JOptionPane.showMessageDialog(this, "Enter valid numbers for Price/Pages");
    } catch (Exception ex) {
        ex.printStackTrace();
        JOptionPane.showMessageDialog(this, "Error adding book: " + ex.getMessage());
    }
});

        formCard.add(saveBtn, gbc);

        JPanel wrapper = new JPanel(new GridBagLayout());
        wrapper.setOpaque(false);
        wrapper.setBorder(BorderFactory.createEmptyBorder(15, 0, 0, 0));
        wrapper.add(formCard);

        panel.add(wrapper, BorderLayout.CENTER);
        return panel;
    }

    // ========== MANAGE USERS ==========

    private JPanel createManageUsersPanel() {
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
        JLabel header = new JLabel("Manage Users");
        header.setFont(Theme.FONT_TITLE);
        header.setForeground(Theme.TEXT_WHITE);
        header.setBorder(BorderFactory.createEmptyBorder(0, 15, 0, 0));
        headerPanel.add(header, BorderLayout.CENTER);
        panel.add(headerPanel, BorderLayout.NORTH);

        // Table
        String[] cols = {"User ID", "Username", "Role", "Books Borrowed"};
        DefaultTableModel tModel = new DefaultTableModel(cols, 0) {
            @Override
            public boolean isCellEditable(int row, int col) { return false; }
        };
        JTable table = new JTable(tModel);
        Theme.styleTable(table);
        refreshUsersTable(tModel);
        JScrollPane scrollPane = new JScrollPane(table);
        Theme.styleScrollPane(scrollPane);
        scrollPane.setBorder(BorderFactory.createEmptyBorder(15, 0, 15, 0));
        panel.add(scrollPane, BorderLayout.CENTER);

        // Buttons
        JPanel actPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        actPanel.setOpaque(false);

        JButton addBtn = new JButton("Add User");
        Theme.styleButton(addBtn);
        JButton remBtn = new JButton("Remove Selected");
        remBtn.setBackground(Theme.DANGER_BTN);
        Theme.styleButton(remBtn);

        addBtn.addActionListener(e -> {
            JTextField userField = new JTextField();
            Theme.styleTextField(userField);
            userField.setEditable(true);
            userField.setEnabled(true);
            userField.setFocusable(true);
            JPasswordField passFieldDialog = new JPasswordField();
            Theme.stylePasswordField(passFieldDialog);
            passFieldDialog.setEditable(true);
            passFieldDialog.setEnabled(true);
            passFieldDialog.setFocusable(true);
            JPanel inputPanel = new JPanel(new GridLayout(2, 2, 10, 8));
            inputPanel.setBackground(Theme.BG_CARD);
            inputPanel.add(createFormLabel("Username:"));
            inputPanel.add(userField);
            inputPanel.add(createFormLabel("Password:"));
            inputPanel.add(passFieldDialog);

            int res = JOptionPane.showConfirmDialog(this, inputPanel, "Add New User",
                    JOptionPane.OK_CANCEL_OPTION);
            if (res == JOptionPane.OK_OPTION) {
                String u = userField.getText().trim();
                String p = new String(passFieldDialog.getPassword());
                if (u.isEmpty() || p.isEmpty()) {
                    JOptionPane.showMessageDialog(this, "Both fields are required.");
                    return;
                }
                if (!service.registerUser(u, p)) {
                    JOptionPane.showMessageDialog(this, "Username already exists.");
                } else {
                    refreshUsersTable(tModel);
                    JOptionPane.showMessageDialog(this, "User created!");
                }
            }
        });

        remBtn.addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row >= 0) {
                String user = (String) tModel.getValueAt(row, 1);
                int confirm = JOptionPane.showConfirmDialog(this,
                        "Remove user '" + user + "'?", "Confirm", JOptionPane.YES_NO_OPTION);
                if (confirm == JOptionPane.YES_OPTION) {
                    service.removeUser(user);
                    refreshUsersTable(tModel);
                }
            } else {
                JOptionPane.showMessageDialog(this, "Select a user to remove.");
            }
        });

        actPanel.add(addBtn);
        actPanel.add(remBtn);
        panel.add(actPanel, BorderLayout.SOUTH);

        return panel;
    }

    private void refreshUsersTable(DefaultTableModel model) {
        model.setRowCount(0);
        for (Account u : service.getAllUsers()) {
            int books = service.getBooksIssuedTo(u.getId()).size();
            model.addRow(new Object[]{u.getId().substring(0, 8), u.getUsername(), u.getRole(), books});
        }
    }

    // ========== ISSUE BOOK PANEL ==========

    private JPanel createIssueBookPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Theme.BG_DARK);
        panel.setBorder(BorderFactory.createEmptyBorder(25, 30, 25, 30));

        // Header
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setOpaque(false);
        JButton backBtn = new JButton("\u2190 Back");
        Theme.styleButton(backBtn);
        backBtn.addActionListener(e -> cardLayout.show(cardPanel, "Dashboard"));
        headerPanel.add(backBtn, BorderLayout.WEST);
        JLabel header = new JLabel("Issue / Return Book");
        header.setFont(Theme.FONT_TITLE);
        header.setForeground(Theme.TEXT_WHITE);
        header.setBorder(BorderFactory.createEmptyBorder(0, 15, 0, 0));
        headerPanel.add(header, BorderLayout.CENTER);
        panel.add(headerPanel, BorderLayout.NORTH);

        // Form Card
        JPanel formCard = Theme.createCardPanel();
        formCard.setLayout(new GridBagLayout());
        formCard.setBorder(BorderFactory.createEmptyBorder(30, 40, 30, 40));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(8, 10, 8, 10);

        JTextField bookIdField = new JTextField(20);
        JTextField userIdField = new JTextField(20);
        JTextField daysField = new JTextField("14", 20);
        JLabel fineLabel = new JLabel("Fine: $0.00");
        fineLabel.setFont(Theme.FONT_BOLD);
        fineLabel.setForeground(Theme.WARNING_COLOR);

        Theme.styleTextField(bookIdField);
        bookIdField.setEditable(true);
        bookIdField.setEnabled(true);
        bookIdField.setFocusable(true);
        Theme.styleTextField(userIdField);
        userIdField.setEditable(true);
        userIdField.setEnabled(true);
        userIdField.setFocusable(true);
        Theme.styleTextField(daysField);
        daysField.setEditable(true);
        daysField.setEnabled(true);
        daysField.setFocusable(true);

        gbc.gridx = 0; gbc.gridy = 0; gbc.weightx = 0;
        formCard.add(createFormLabel("Book ID:"), gbc);
        gbc.gridx = 1; gbc.weightx = 1.0;
        formCard.add(bookIdField, gbc);

        gbc.gridx = 0; gbc.gridy = 1; gbc.weightx = 0;
        formCard.add(createFormLabel("Username:"), gbc);
        gbc.gridx = 1; gbc.weightx = 1.0;
        formCard.add(userIdField, gbc);

        gbc.gridx = 0; gbc.gridy = 2; gbc.weightx = 0;
        formCard.add(createFormLabel("Duration (Days):"), gbc);
        gbc.gridx = 1; gbc.weightx = 1.0;
        formCard.add(daysField, gbc);

        gbc.gridx = 0; gbc.gridy = 3; gbc.gridwidth = 2;
        formCard.add(fineLabel, gbc);

        // Buttons
        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 0));
        btnPanel.setOpaque(false);

        JButton issueBtn = new JButton("Issue Book");
        Theme.styleButton(issueBtn);

        JButton returnBtn = new JButton("Return Book");
        Theme.styleButton(returnBtn);

        JButton cancelBtn = new JButton("Cancel");
        cancelBtn.setBackground(Theme.DANGER_BTN);
        Theme.styleButton(cancelBtn);

        issueBtn.addActionListener(e -> {
            String bookId = bookIdField.getText().trim();
            String username = userIdField.getText().trim();
            if (bookId.isEmpty() || username.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Book ID and Username are required.");
                return;
            }
            try {
                int days = Integer.parseInt(daysField.getText().trim());
                if (service.issueBookToUser(bookId, username, days)) {
                    JOptionPane.showMessageDialog(this, "Book issued successfully!");
                    bookIdField.setText("");
                    userIdField.setText("");
                } else {
                    JOptionPane.showMessageDialog(this, "Could not issue book. Check Book ID, Username, and availability.");
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Invalid duration value.");
            }
        });

        returnBtn.addActionListener(e -> {
            String bookId = bookIdField.getText().trim();
            if (bookId.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Enter a Book ID to return.");
                return;
            }
            double fine = service.returnBook(bookId);
            fineLabel.setText(String.format("Fine: $%.2f", fine));
            if (fine > 0) {
                JOptionPane.showMessageDialog(this, String.format("Book returned. Overdue fine: $%.2f", fine));
            } else {
                JOptionPane.showMessageDialog(this, "Book returned successfully. No fine.");
            }
            bookIdField.setText("");
        });

        cancelBtn.addActionListener(e -> {
            bookIdField.setText("");
            userIdField.setText("");
            fineLabel.setText("Fine: $0.00");
            cardLayout.show(cardPanel, "Dashboard");
        });

        btnPanel.add(issueBtn);
        btnPanel.add(returnBtn);
        btnPanel.add(cancelBtn);

        gbc.gridx = 0; gbc.gridy = 4; gbc.gridwidth = 2;
        gbc.insets = new Insets(20, 10, 10, 10);
        formCard.add(btnPanel, gbc);

        JPanel wrapper = new JPanel(new GridBagLayout());
        wrapper.setOpaque(false);
        wrapper.setBorder(BorderFactory.createEmptyBorder(15, 0, 0, 0));
        wrapper.add(formCard);

        panel.add(wrapper, BorderLayout.CENTER);
        return panel;
    }

    // ========== ISSUED BOOKS PANEL ==========

    private JPanel createIssuedBooksPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Theme.BG_DARK);
        panel.setBorder(BorderFactory.createEmptyBorder(25, 30, 25, 30));

        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setOpaque(false);
        JButton backBtn = new JButton("\u2190 Back");
        Theme.styleButton(backBtn);
        backBtn.addActionListener(e -> cardLayout.show(cardPanel, "Dashboard"));
        headerPanel.add(backBtn, BorderLayout.WEST);
        JLabel header = new JLabel("Currently Issued Books");
        header.setFont(Theme.FONT_TITLE);
        header.setForeground(Theme.TEXT_WHITE);
        header.setBorder(BorderFactory.createEmptyBorder(0, 15, 0, 0));
        headerPanel.add(header, BorderLayout.CENTER);
        panel.add(headerPanel, BorderLayout.NORTH);

        String[] cols = {"Book ID", "Title", "Issued To", "Issue Date", "Due Date"};
        DefaultTableModel tModel = new DefaultTableModel(cols, 0) {
            @Override
            public boolean isCellEditable(int row, int col) { return false; }
        };
        JTable table = new JTable(tModel);
        Theme.styleTable(table);
        JScrollPane scrollPane = new JScrollPane(table);
        Theme.styleScrollPane(scrollPane);
        scrollPane.setBorder(BorderFactory.createEmptyBorder(15, 0, 15, 0));
        panel.add(scrollPane, BorderLayout.CENTER);

        JButton refreshBtn = new JButton("Refresh");
        Theme.styleButton(refreshBtn);
        refreshBtn.addActionListener(e -> {
            tModel.setRowCount(0);
            for (Book b : service.getIssuedBooks()) {
                String username = service.getUsernameById(b.getIssuedToUserId());
                tModel.addRow(new Object[]{b.getId(), b.getTitle(), username,
                        b.getIssuedDate(), b.getDueDate()});
            }
        });
        refreshBtn.doClick(); // Auto-populate

        JPanel sPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        sPanel.setOpaque(false);
        sPanel.add(refreshBtn);
        panel.add(sPanel, BorderLayout.SOUTH);

        return panel;
    }

    // ========== UTILITY ==========

    private JLabel createFormLabel(String text) {
        JLabel lbl = new JLabel(text);
        lbl.setFont(Theme.FONT_BOLD);
        lbl.setForeground(Theme.TEXT_LIGHT);
        return lbl;
    }
}
