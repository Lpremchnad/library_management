package console_lms.gui;

import console_lms.models.Account;
import console_lms.models.UserRole;
import console_lms.service.LibraryService;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.RoundRectangle2D;

/**
 * Modern dark-violet themed Login screen.
 * Features: centered login card, radio buttons for Admin/User, 
 * Login/Create/Cancel buttons, and password masking.
 */
public class LoginFrame extends JFrame {
    private final LibraryService libraryService;

    public LoginFrame(LibraryService libraryService) {
        this.libraryService = libraryService;

        setTitle("Library Pro - Login");
setSize(600, 620);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);
        setUndecorated(false);

        // Main background panel with gradient
        JPanel mainPanel = new JPanel(new GridBagLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                GradientPaint gp = new GradientPaint(0, 0, Theme.BG_DARK, 
                        getWidth(), getHeight(), new Color(25, 20, 50));
                g2.setPaint(gp);
                g2.fillRect(0, 0, getWidth(), getHeight());
                g2.dispose();
            }
        };
        mainPanel.setOpaque(false);
        setContentPane(mainPanel);

        // Login Card
        JPanel card = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                // Card shadow
                g2.setColor(new Color(0, 0, 0, 40));
                g2.fillRoundRect(4, 4, getWidth() - 4, getHeight() - 4, 24, 24);
                // Card background
                g2.setColor(Theme.BG_CARD);
                g2.fillRoundRect(0, 0, getWidth() - 4, getHeight() - 4, 24, 24);
                // Top accent gradient line
                GradientPaint gp = new GradientPaint(0, 0, Theme.PURPLE_LIGHT, getWidth(), 0, Theme.PURPLE_DARK);
                g2.setPaint(gp);
                g2.fillRoundRect(0, 0, getWidth() - 4, 5, 24, 24);
                g2.dispose();
            }
        };
        card.setOpaque(false);
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBorder(BorderFactory.createEmptyBorder(35, 40, 30, 40));
card.setPreferredSize(new Dimension(480, 500));

        // ===== Icon / Logo area =====
        JLabel iconLabel = new JLabel("\uD83D\uDCDA", SwingConstants.CENTER);
        iconLabel.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 40));
        iconLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        card.add(iconLabel);
        card.add(Box.createRigidArea(new Dimension(0, 5)));

        // Title
        JLabel titleLabel = new JLabel("Welcome to Library");
titleLabel.setFont(new Font(Theme.FONT_TITLE.getFontName(), Font.BOLD, 20));
        titleLabel.setForeground(Theme.TEXT_WHITE);
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        card.add(titleLabel);

        // Subtitle
        JLabel subtitleLabel = new JLabel("Sign in to continue");
        subtitleLabel.setFont(Theme.FONT_SMALL);
        subtitleLabel.setForeground(Theme.TEXT_MUTED);
        subtitleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        card.add(subtitleLabel);
        card.add(Box.createRigidArea(new Dimension(0, 25)));

        // Username field
        JLabel userLabel = new JLabel("Username");
        userLabel.setFont(Theme.FONT_BOLD);
        userLabel.setForeground(Theme.TEXT_LIGHT);
        userLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        card.add(userLabel);
        card.add(Box.createRigidArea(new Dimension(0, 6)));

        JTextField userField = new JTextField();
        Theme.styleTextField(userField);
        userField.setMaximumSize(new Dimension(Integer.MAX_VALUE, 42));
        userField.setAlignmentX(Component.LEFT_ALIGNMENT);
        card.add(userField);
        card.add(Box.createRigidArea(new Dimension(0, 15)));

        // Password field
        JLabel passLabel = new JLabel("Password");
        passLabel.setFont(Theme.FONT_BOLD);
        passLabel.setForeground(Theme.TEXT_LIGHT);
        passLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        card.add(passLabel);
        card.add(Box.createRigidArea(new Dimension(0, 6)));

        JPasswordField passField = new JPasswordField();
        Theme.stylePasswordField(passField);
        passField.setMaximumSize(new Dimension(Integer.MAX_VALUE, 42));
        passField.setAlignmentX(Component.LEFT_ALIGNMENT);
        card.add(passField);
        card.add(Box.createRigidArea(new Dimension(0, 18)));

        // Radio buttons: Admin / User
        JPanel rolePanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 0));
        rolePanel.setOpaque(false);
        rolePanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        rolePanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 35));

        JLabel roleLabel = new JLabel("Login as:");
        roleLabel.setFont(Theme.FONT_BOLD);
        roleLabel.setForeground(Theme.TEXT_LIGHT);

        JRadioButton adminRadio = new JRadioButton("Admin");
        Theme.styleRadioButton(adminRadio);
        adminRadio.setOpaque(false);
        adminRadio.setForeground(Theme.TEXT_LIGHT);

        JRadioButton userRadio = new JRadioButton("User");
        Theme.styleRadioButton(userRadio);
        userRadio.setOpaque(false);
        userRadio.setForeground(Theme.TEXT_LIGHT);
        userRadio.setSelected(true);

        ButtonGroup roleGroup = new ButtonGroup();
        roleGroup.add(adminRadio);
        roleGroup.add(userRadio);

        rolePanel.add(roleLabel);
        rolePanel.add(adminRadio);
        rolePanel.add(userRadio);
        card.add(rolePanel);
        card.add(Box.createRigidArea(new Dimension(0, 22)));

        // ===== Buttons =====
        // Buttons - 2 rows for better visibility
        JPanel buttonPanel = new JPanel(new GridBagLayout());
        buttonPanel.setOpaque(false);
        buttonPanel.setAlignmentX(Component.LEFT_ALIGNMENT);

        GridBagConstraints gbcBtn = new GridBagConstraints();
        gbcBtn.insets = new Insets(0, 0, 10, 10);
        gbcBtn.fill = GridBagConstraints.HORIZONTAL;

        JButton loginBtn = new JButton("Login");
        Theme.styleButton(loginBtn);
        loginBtn.setPreferredSize(new Dimension(120, 42));

        JButton createBtn = new JButton("Create");
        Theme.styleButton(createBtn);
        createBtn.setPreferredSize(new Dimension(120, 42));

        gbcBtn.gridx = 0; gbcBtn.gridy = 0;
        buttonPanel.add(loginBtn, gbcBtn);

        gbcBtn.gridx = 1;
        buttonPanel.add(createBtn, gbcBtn);

        JButton cancelBtn = new JButton("Cancel");
        cancelBtn.setBackground(Theme.DANGER_BTN);
        Theme.styleButton(cancelBtn);
        cancelBtn.setPreferredSize(new Dimension(120, 42));

        gbcBtn.gridx = 0; gbcBtn.gridy = 1;
        gbcBtn.gridwidth = 2;
        gbcBtn.insets = new Insets(0, 0, 0, 0);
        buttonPanel.add(cancelBtn, gbcBtn);

        card.add(buttonPanel);

        // Add card to the center of the mainPanel
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        mainPanel.add(card, gbc);

        // ===== ACTIONS =====

        // Login
        loginBtn.addActionListener(e -> {
            String uname = userField.getText().trim();
            String pwd = new String(passField.getPassword());

            if (uname.isEmpty() || pwd.isEmpty()) {
                showStyledMessage("Please fill in both username and password.", "Validation Error", JOptionPane.WARNING_MESSAGE);
                return;
            }

            Account account = libraryService.login(uname, pwd);
            if (account != null) {
                // Validate role selection matches account role
                boolean wantsAdmin = adminRadio.isSelected();
                if ((wantsAdmin && account.getRole() != UserRole.ADMIN) ||
                    (!wantsAdmin && account.getRole() != UserRole.USER)) {
                    showStyledMessage("The selected role does not match your account.", "Role Mismatch", JOptionPane.WARNING_MESSAGE);
                    return;
                }
                dispose();
                if (account.getRole() == UserRole.ADMIN) {
                    new AdminDashboard(libraryService, account).setVisible(true);
                } else {
                    new UserDashboard(libraryService, account).setVisible(true);
                }
            } else {
                showStyledMessage("Invalid username or password!", "Login Failed", JOptionPane.ERROR_MESSAGE);
            }
        });

        // Create Account
        createBtn.addActionListener(e -> {
            String uname = userField.getText().trim();
            String pwd = new String(passField.getPassword());

            if (uname.isEmpty() || pwd.isEmpty()) {
                showStyledMessage("Please enter a username and password to create.", "Validation Error", JOptionPane.WARNING_MESSAGE);
                return;
            }
            if (pwd.length() < 4) {
                showStyledMessage("Password must be at least 4 characters.", "Validation Error", JOptionPane.WARNING_MESSAGE);
                return;
            }

            boolean success = libraryService.registerUser(uname, pwd);
            if (success) {
                showStyledMessage("Account created successfully!\nYou can now login.", "Success", JOptionPane.INFORMATION_MESSAGE);
            } else {
                showStyledMessage("Username '" + uname + "' already exists.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        // Cancel
        cancelBtn.addActionListener(e -> {
            System.exit(0);
        });

        // Enter key triggers login
        passField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    loginBtn.doClick();
                }
            }
        });
    }

    private void showStyledMessage(String message, String title, int messageType) {
        JOptionPane.showMessageDialog(this, message, title, messageType);
    }
}
