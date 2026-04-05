
import java.sql.Connection;

public class ConnectivityTest {
    public static void main(String[] args) {
        System.out.println("Attempting to connect to database...");
        Connection conn = DB.getConnection();
        if (conn != null) {
            System.out.println("SUCCESS: Connected to the database!");
            try {
                conn.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            System.out.println("FAILURE: Could not connect to the database.");
            System.out.println(
                    "Check: 1. MySQL running on port 3308? 2. Database 'library' exists? 3. Username/Password correct?");
        }
    }
}
