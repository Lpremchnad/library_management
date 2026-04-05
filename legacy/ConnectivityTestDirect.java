
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

public class ConnectivityTestDirect {
    public static void main(String[] args) {
        String url = "jdbc:mysql://localhost:3306/library?useSSL=false&characterEncoding=utf8";
        String user = "root";
        String pass = "Lpremchand@777";

        System.out.println("Test 1: Connecting to MySQL Server at " + url + " with user '" + user + "'...");
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection conn = DriverManager.getConnection(url, user, pass);
            System.out.println("SUCCESS: Connected to MySQL Server!");

            System.out.println("Test 2: Verifying Data Integrity...");
            Statement stmt = conn.createStatement();
            ResultSet rsUsers = stmt.executeQuery("SELECT COUNT(*) FROM USERS");
            if (rsUsers.next()) {
                System.out.println("SUCCESS: USERS table has " + rsUsers.getInt(1) + " records.");
            }

            ResultSet rsBooks = stmt.executeQuery("SELECT COUNT(*) FROM BOOKS");
            if (rsBooks.next()) {
                System.out.println("SUCCESS: BOOKS table has " + rsBooks.getInt(1) + " records.");
            }

            conn.close();
        } catch (Exception e) {
            System.out.println("FAILURE: Error during verification.");
            e.printStackTrace();
        }
    }
}
