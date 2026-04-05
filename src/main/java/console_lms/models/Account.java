package console_lms.models;

import java.io.Serializable;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Objects;
import java.util.UUID;

/**
 * Represents an Account in the Library Management System.
 * Could be an Admin or a User.
 */
public class Account implements Serializable {
    private static final long serialVersionUID = 1L;
    
    private String id;
    private String username;
    private String passwordHash;
    private UserRole role;

    public Account(String username, String plainPassword, UserRole role) {
        this.id = UUID.randomUUID().toString();
        this.username = username;
        this.passwordHash = hashPassword(plainPassword);
        this.role = role;
    }

    public String getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public UserRole getRole() {
        return role;
    }

    /**
     * Authenticates a user against a plain-text password.
     */
    public boolean verifyPassword(String plainPassword) {
        return this.passwordHash.equals(hashPassword(plainPassword));
    }

    /**
     * Helper to encrypt the password using SHA-256
     */
    private String hashPassword(String password) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] encodedhash = digest.digest(password.getBytes());
            StringBuilder hexString = new StringBuilder(2 * encodedhash.length);
            for (byte b : encodedhash) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) {
                    hexString.append('0');
                }
                hexString.append(hex);
            }
            return hexString.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("SHA-256 not supported by your Java environment", e);
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Account account = (Account) o;
        return Objects.equals(id, account.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
