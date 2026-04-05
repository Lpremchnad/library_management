package console_lms.gui;

import javax.swing.*;
import javax.swing.border.AbstractBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.RoundRectangle2D;

/**
 * Theme configuration for the Library Management System.
 * Dark Violet / Purple gradient dashboard theme.
 */
public class Theme {

    // ========== COLOR PALETTE ==========
public static final Color BG_DARK        = new Color(30, 30, 47);     // #1E1E2F - Main background
    public static final Color BG_CARD        = new Color(42, 42, 64);     // #2A2A40 - Card / Panel background
    public static final Color SIDEBAR_BG     = new Color(42, 42, 64);     // Sidebar background
    public static final Color SIDEBAR_HOVER  = new Color(108, 92, 231);  // Hover highlight

    // Purple gradients for buttons
    public static final Color PURPLE_LIGHT   = new Color(160, 100, 255);  // Light purple
    public static final Color PURPLE_DARK    = new Color(100, 50, 200);   // Dark purple
    public static final Color PURPLE_ACCENT  = new Color(108, 92, 231);  // #6C5CE7 - Button color
    public static final Color PURPLE_HOVER   = new Color(138, 124, 255);  // #8A7CFF - Hover state

    // Text colors
    public static final Color TEXT_WHITE     = new Color(255, 255, 255);
    public static final Color TEXT_LIGHT     = new Color(200, 200, 220);  // Light gray text
    public static final Color TEXT_MUTED     = new Color(150, 150, 175);  // Muted labels

    // Field colors
    public static final Color FIELD_BG       = new Color(38, 38, 60);    // Dark input field background
    public static final Color FIELD_BORDER   = new Color(80, 70, 120);   // Subtle border for fields
    public static final Color FIELD_FOCUS    = new Color(140, 80, 240);  // Focus border color

    // Table colors
    public static final Color TABLE_BG       = new Color(35, 35, 55);
    public static final Color TABLE_ALT_ROW  = new Color(40, 40, 62);
    public static final Color TABLE_HEADER   = new Color(50, 45, 80);
    public static final Color TABLE_GRID     = new Color(55, 55, 80);
    public static final Color TABLE_SELECT   = new Color(100, 60, 180);

    // Special colors
    public static final Color DANGER_BTN     = new Color(220, 60, 60);
    public static final Color DANGER_HOVER   = new Color(240, 80, 80);
    public static final Color SUCCESS_COLOR  = new Color(50, 200, 120);
    public static final Color WARNING_COLOR  = new Color(255, 180, 50);

    // ========== FONTS ==========
    public static final Font FONT_REGULAR    = new Font("Segoe UI", Font.PLAIN, 14);
    public static final Font FONT_BOLD       = new Font("Segoe UI", Font.BOLD, 14);
    public static final Font FONT_TITLE      = new Font("Segoe UI", Font.BOLD, 24);
    public static final Font FONT_SUBTITLE   = new Font("Segoe UI", Font.BOLD, 18);
    public static final Font FONT_SMALL      = new Font("Segoe UI", Font.PLAIN, 12);
    public static final Font FONT_SIDEBAR    = new Font("Segoe UI", Font.BOLD, 14);
    public static final Font FONT_CARD_VALUE = new Font("Segoe UI", Font.BOLD, 32);

    // Legacy references for backward compat
    public static final Font MAIN_FONT  = FONT_REGULAR;
    public static final Font BOLD_FONT  = FONT_BOLD;
    public static final Font TITLE_FONT = FONT_TITLE;
    public static final Color TEXT_MAIN = TEXT_WHITE;
    public static final Color GRID_COLOR = TABLE_GRID;

    // ========== BUTTON STYLING ==========

    /**
     * Styles a gradient purple button with hover effect.
     */
    public static void styleButton(JButton button) {
        button.setFont(FONT_BOLD);
        button.setForeground(TEXT_WHITE);
        button.setBackground(PURPLE_ACCENT);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setContentAreaFilled(false);
        button.setOpaque(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));

        button.setUI(new javax.swing.plaf.basic.BasicButtonUI() {
            @Override
            public void paint(Graphics g, JComponent c) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                JButton btn = (JButton) c;
                Color bg = btn.getModel().isRollover() ? PURPLE_HOVER : PURPLE_ACCENT;
                Color bgEnd = btn.getModel().isRollover() ? PURPLE_ACCENT : PURPLE_DARK;

                // Check for danger button
                if (btn.getBackground().equals(DANGER_BTN)) {
                    bg = btn.getModel().isRollover() ? DANGER_HOVER : DANGER_BTN;
                    bgEnd = bg.darker();
                }

                GradientPaint gp = new GradientPaint(0, 0, bg, 0, c.getHeight(), bgEnd);
                g2.setPaint(gp);
                g2.fillRoundRect(0, 0, c.getWidth(), c.getHeight(), 12, 12);

                g2.dispose();
                super.paint(g, c);
            }
        });
    }

    /**
     * Styles a sidebar menu button with hover animation.
     */
    public static JButton createSidebarButton(String text, String iconChar) {
        JButton btn = new JButton(iconChar + "  " + text);
        btn.setFont(FONT_SIDEBAR);
        btn.setForeground(TEXT_LIGHT);
        btn.setBackground(SIDEBAR_BG);
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setContentAreaFilled(false);
        btn.setOpaque(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setHorizontalAlignment(SwingConstants.LEFT);
        btn.setBorder(BorderFactory.createEmptyBorder(12, 20, 12, 20));
        btn.setMaximumSize(new Dimension(240, 45));

        btn.setUI(new javax.swing.plaf.basic.BasicButtonUI() {
            @Override
            public void paint(Graphics g, JComponent c) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                JButton b = (JButton) c;
                if (b.getModel().isRollover()) {
                    g2.setColor(SIDEBAR_HOVER);
                    g2.fillRoundRect(4, 0, c.getWidth() - 8, c.getHeight(), 10, 10);
                    // Left accent bar
                    g2.setColor(PURPLE_ACCENT);
                    g2.fillRoundRect(0, 4, 4, c.getHeight() - 8, 4, 4);
                }

                g2.dispose();
                super.paint(g, c);
            }
        });

        return btn;
    }

    // ========== TEXT FIELD STYLING ==========

    /**
     * Style a text field with dark violet theme and rounded border.
     */
    public static void styleTextField(JTextField field) {
        field.setFont(FONT_REGULAR);
        field.setEditable(true);
        field.setEnabled(true);
        field.setFocusable(true);
        field.setOpaque(false);
        field.setBackground(FIELD_BG);
        field.setForeground(TEXT_WHITE);
        field.setCaretColor(TEXT_WHITE);
        field.setSelectionColor(PURPLE_ACCENT);
        field.setSelectedTextColor(TEXT_WHITE);
        field.setBorder(BorderFactory.createCompoundBorder(
                new RoundedBorder(FIELD_BORDER, 10),
                BorderFactory.createEmptyBorder(12, 16, 12, 16)
        ));
        
        // Focus listener for better UX
        field.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                field.setBorder(BorderFactory.createCompoundBorder(
                    new RoundedBorder(FIELD_FOCUS, 10),
                    BorderFactory.createEmptyBorder(12, 16, 12, 16)
                ));
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                field.setBorder(BorderFactory.createCompoundBorder(
                    new RoundedBorder(FIELD_BORDER, 10),
                    BorderFactory.createEmptyBorder(12, 16, 12, 16)
                ));
            }
        });
    }

    /**
     * Style a password field with dark violet theme.
     */
    public static void stylePasswordField(JPasswordField field) {
        field.setFont(FONT_REGULAR);
        field.setEditable(true);
        field.setEnabled(true);
        field.setFocusable(true);
        field.setOpaque(false);
        field.setBackground(FIELD_BG);
        field.setForeground(TEXT_WHITE);
        field.setCaretColor(TEXT_WHITE);
        field.setSelectionColor(PURPLE_ACCENT);
        field.setSelectedTextColor(TEXT_WHITE);
        field.setEchoChar('*');
        field.setBorder(BorderFactory.createCompoundBorder(
                new RoundedBorder(FIELD_BORDER, 10),
                BorderFactory.createEmptyBorder(8, 12, 8, 12)
        ));
    }

    // ========== TABLE STYLING ==========

    /**
     * Fully styles a JTable for the dark violet theme.
     */
    public static void styleTable(JTable table) {
        table.setBackground(TABLE_BG);
        table.setForeground(TEXT_LIGHT);
        table.setGridColor(TABLE_GRID);
        table.setRowHeight(36);
        table.setFont(FONT_REGULAR);
        table.setSelectionBackground(TABLE_SELECT);
        table.setSelectionForeground(TEXT_WHITE);
        table.setShowHorizontalLines(true);
        table.setShowVerticalLines(false);
        table.setIntercellSpacing(new Dimension(0, 1));

        // Alternating row colors
        table.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value,
                    boolean isSelected, boolean hasFocus, int row, int column) {
                Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                if (!isSelected) {
                    c.setBackground(row % 2 == 0 ? TABLE_BG : TABLE_ALT_ROW);
                    c.setForeground(TEXT_LIGHT);
                } else {
                    c.setBackground(TABLE_SELECT);
                    c.setForeground(TEXT_WHITE);
                }
                setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 10));
                return c;
            }
        });

        // Header styling
        JTableHeader header = table.getTableHeader();
        header.setBackground(TABLE_HEADER);
        header.setForeground(TEXT_WHITE);
        header.setFont(FONT_BOLD);
        header.setBorder(BorderFactory.createMatteBorder(0, 0, 2, 0, PURPLE_ACCENT));
        header.setDefaultRenderer(new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value,
                    boolean isSelected, boolean hasFocus, int row, int column) {
                Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                c.setBackground(TABLE_HEADER);
                c.setForeground(TEXT_WHITE);
                setFont(FONT_BOLD);
                setHorizontalAlignment(SwingConstants.LEFT);
                setBorder(BorderFactory.createCompoundBorder(
                        BorderFactory.createMatteBorder(0, 0, 2, 0, PURPLE_ACCENT),
                        BorderFactory.createEmptyBorder(8, 10, 8, 10)
                ));
                return c;
            }
        });
    }

    /**
     * Style scroll pane to match dark theme.
     */
    public static void styleScrollPane(JScrollPane scrollPane) {
        scrollPane.getViewport().setBackground(TABLE_BG);
        scrollPane.setBorder(new RoundedBorder(new Color(55, 55, 85), 12));
        scrollPane.setBackground(TABLE_BG);
    }

    // ========== PANEL UTILITIES ==========

    /**
     * Creates a card panel with rounded corners and dark background.
     */
    public static JPanel createCardPanel() {
        JPanel card = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(BG_CARD);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 16, 16);
                g2.dispose();
            }
        };
        card.setOpaque(false);
        return card;
    }

    /**
     * Creates a stat card for the dashboard with icon, value and label.
     */
    public static JPanel createStatCard(String iconChar, String value, String label, Color accentColor) {
        JPanel card = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                // Card background
                g2.setColor(BG_CARD);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 18, 18);

                // Top accent stripe
                g2.setColor(accentColor);
                g2.fillRoundRect(0, 0, getWidth(), 4, 4, 4);

                g2.dispose();
            }
        };
        card.setOpaque(false);
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBorder(BorderFactory.createEmptyBorder(20, 20, 15, 20));
        card.setPreferredSize(new Dimension(180, 130));

        JLabel iconLabel = new JLabel(iconChar);
        iconLabel.setFont(new Font("Segoe UI", Font.PLAIN, 28));
        iconLabel.setForeground(accentColor);
        iconLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel valueLabel = new JLabel(value);
        valueLabel.setFont(FONT_CARD_VALUE);
        valueLabel.setForeground(TEXT_WHITE);
        valueLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel labelText = new JLabel(label);
        labelText.setFont(FONT_SMALL);
        labelText.setForeground(TEXT_MUTED);
        labelText.setAlignmentX(Component.LEFT_ALIGNMENT);

        card.add(iconLabel);
        card.add(Box.createRigidArea(new Dimension(0, 5)));
        card.add(valueLabel);
        card.add(Box.createRigidArea(new Dimension(0, 3)));
        card.add(labelText);

        return card;
    }

    /**
     * Style a combo box for the dark theme.
     */
    public static void styleComboBox(JComboBox<?> comboBox) {
        comboBox.setFont(FONT_REGULAR);
        comboBox.setBackground(FIELD_BG);
        comboBox.setForeground(TEXT_WHITE);
        comboBox.setBorder(new RoundedBorder(FIELD_BORDER, 10));
    }

    /**
     * Style radio button for dark theme.
     */
    public static void styleRadioButton(JRadioButton rb) {
        rb.setFont(FONT_REGULAR);
        rb.setForeground(TEXT_LIGHT);
        rb.setBackground(BG_CARD);
        rb.setFocusPainted(false);
        rb.setCursor(new Cursor(Cursor.HAND_CURSOR));
    }

    /**
     * Apply global dark UI defaults.
     */
    public static void applyGlobalTheme() {
        UIManager.put("OptionPane.background", BG_CARD);
        UIManager.put("Panel.background", BG_CARD);
        UIManager.put("OptionPane.messageForeground", TEXT_WHITE);
        UIManager.put("OptionPane.messageFont", FONT_REGULAR);
        UIManager.put("TextField.background", FIELD_BG);
        UIManager.put("TextField.foreground", TEXT_WHITE);
        UIManager.put("TextField.caretForeground", TEXT_WHITE);
        UIManager.put("PasswordField.background", FIELD_BG);
        UIManager.put("PasswordField.foreground", TEXT_WHITE);
        UIManager.put("PasswordField.caretForeground", TEXT_WHITE);
        UIManager.put("Label.foreground", TEXT_LIGHT);
        UIManager.put("CheckBox.background", BG_CARD);
        UIManager.put("CheckBox.foreground", TEXT_LIGHT);
        UIManager.put("Button.background", PURPLE_ACCENT);
        UIManager.put("Button.foreground", TEXT_WHITE);
        UIManager.put("ComboBox.background", FIELD_BG);
        UIManager.put("ComboBox.foreground", TEXT_WHITE);
        UIManager.put("ComboBox.selectionBackground", PURPLE_ACCENT);
        UIManager.put("ComboBox.selectionForeground", TEXT_WHITE);
        UIManager.put("ScrollPane.background", BG_DARK);
        UIManager.put("Viewport.background", TABLE_BG);
    }

    // ========== ROUNDED BORDER UTILITY ==========

    /**
     * A custom rounded border for components.
     */
    public static class RoundedBorder extends AbstractBorder {
        private final Color color;
        private final int radius;

        public RoundedBorder(Color color, int radius) {
            this.color = color;
            this.radius = radius;
        }

        @Override
        public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(color);
            g2.drawRoundRect(x, y, width - 1, height - 1, radius, radius);
            g2.dispose();
        }

        @Override
        public Insets getBorderInsets(Component c) {
            return new Insets(4, 8, 4, 8);
        }
    }
}
