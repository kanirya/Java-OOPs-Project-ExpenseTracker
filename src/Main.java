import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Main {
    public static void main(String[] args) {
        // Database connection parameters with SSL options
        String url = "jdbc:sqlserver://localhost:1433;databaseName=OOPsProject;encrypt=true;trustServerCertificate=true";
        String user = "danish";
        String password = "danishkaneria";

        try {
            // Explicitly load the SQL Server JDBC Driver
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");

            // Establish the connection
            try (Connection connection = DriverManager.getConnection(url, user, password)) {
                System.out.println("Connected to SQL Server successfully!");
            } catch (SQLException e) {
                System.out.println("Error establishing the connection: " + e.getMessage());
                e.printStackTrace();
            }
        } catch (ClassNotFoundException e) {
            System.out.println("JDBC Driver not found: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
