import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {
    protected static final String URL = "jdbc:mysql://localhost:3306/rental_management"; // Database Name
    protected static final String USER = "root"; //  MySQL username
    protected static final String PASSWORD = ""; // MySQL password

    public static Connection getConnection() {
        try {
            return DriverManager.getConnection(URL, USER, PASSWORD);
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Error connecting to the database", e);
        }
    }
}