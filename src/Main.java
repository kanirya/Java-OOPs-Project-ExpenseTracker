import java.sql.*;
import java.util.Scanner;

public class Main {
    // Database connection parameters
    private static final String URL = "jdbc:sqlserver://localhost:1433;databaseName=OOPsProject;encrypt=true;trustServerCertificate=true";
    private static final String USER = "danish";
    private static final String PASSWORD = "danishkaneria";

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        try {
            // Main menu loop
            while (true) {
                System.out.println("\n==== User Management ====");
                System.out.println("1. Register");
                System.out.println("2. Login");
                System.out.println("3. Show All Users");
                System.out.println("4. Exit");
                System.out.print("Choose an option: ");

                int choice = scanner.nextInt();
                scanner.nextLine(); // Consume newline character

                switch (choice) {
                    case 1 -> registerUser(scanner);
                    case 2 -> loginUser(scanner);
                    case 3 -> showAllUsers();
                    case 4 -> {
                        System.out.println("Exiting the application...");
                        System.exit(0);
                    }
                    default -> System.out.println("Invalid option! Please try again.");
                }
            }
        } catch (Exception e) {
            System.out.println("An error occurred: " + e.getMessage());
            e.printStackTrace();
        } finally {
            scanner.close();
        }
    }

    // Method to register a user
    private static void registerUser(Scanner scanner) {
        System.out.println("\n==== Register User ====");
        System.out.print("Enter username: ");
        String username = scanner.nextLine();
        System.out.print("Enter password: ");
        String password = scanner.nextLine();

        String sql = "INSERT INTO Users (username, password) VALUES (?, ?)";

        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement statement = connection.prepareStatement(sql)) {

            // Set parameters
            statement.setString(1, username);
            statement.setString(2, password);

            // Execute update
            statement.executeUpdate();
            System.out.println("User registered successfully!");

        } catch (SQLException e) {
            if (e.getMessage().contains("PRIMARY KEY")) {
                System.out.println("Error: Username already exists. Please choose a different username.");
            } else {
                System.out.println("Error registering user: " + e.getMessage());
            }
        }
    }

    // Method to log in a user
    private static void loginUser(Scanner scanner) {
        System.out.println("\n==== User Login ====");
        System.out.print("Enter username: ");
        String username = scanner.nextLine();
        System.out.print("Enter password: ");
        String password = scanner.nextLine();

        String sql = "SELECT * FROM Users WHERE username = ? AND password = ?";

        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement statement = connection.prepareStatement(sql)) {

            // Set parameters
            statement.setString(1, username);
            statement.setString(2, password);

            // Execute query
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                System.out.println("Login successful! Welcome, " + username + "!");
            } else {
                System.out.println("Invalid username or password. Please try again.");
            }

        } catch (SQLException e) {
            System.out.println("Error logging in: " + e.getMessage());
        }
    }

    // Method to show all users
    private static void showAllUsers() {
        System.out.println("\n==== All Registered Users ====");

        String sql = "SELECT username FROM Users";

        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(sql)) {

            while (resultSet.next()) {
                System.out.println("Username: " + resultSet.getString("username"));
            }

        } catch (SQLException e) {
            System.out.println("Error fetching users: " + e.getMessage());
        }
    }
}
