import model.DbConnection;

import java.sql.*;
import java.util.Scanner;

public class Main {
    static Connection connection = DbConnection.getConnection();

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        try {
            while (true) {
                System.out.println("\n==== Management System ====");
                System.out.println("1. Register User");
                System.out.println("2. Login User");
                System.out.println("3. Show All Users");
                System.out.println("4. Add Category");
                System.out.println("5. Add Transaction");
                System.out.println("6. Show All Transactions");
                System.out.println("7. Exit");
                System.out.print("Choose an option: ");

                int choice = scanner.nextInt();
                scanner.nextLine(); // Consume newline character

                switch (choice) {
                    case 1 -> registerUser(scanner);
                    case 2 -> loginUser(scanner);
                    case 3 -> showAllUsers();
                    case 4 -> addCategory(scanner);
                    case 5 -> addTransaction(scanner);
                    case 6 -> showAllTransactions();
                    case 7 -> {
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

    // Register a new user
    private static void registerUser(Scanner scanner) {
        System.out.println("\n==== Register User ====");
        System.out.print("Enter username: ");
        String username = scanner.nextLine();
        System.out.print("Enter password: ");
        String password = scanner.nextLine();

        String sql = "INSERT INTO Users (username, password) VALUES (?, ?)";

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, username);
            statement.setString(2, password);
            statement.executeUpdate();
            System.out.println("User registered successfully!");
        } catch (SQLException e) {
            if (e.getMessage().contains("PRIMARY KEY")) {
                System.out.println("Error: Username already exists.");
            } else {
                System.out.println("Error registering user: " + e.getMessage());
            }
        }
    }

    // Log in a user
    private static void loginUser(Scanner scanner) {
        System.out.println("\n==== User Login ====");
        System.out.print("Enter username: ");
        String username = scanner.nextLine();
        System.out.print("Enter password: ");
        String password = scanner.nextLine();

        String sql = "SELECT * FROM Users WHERE username = ? AND password = ?";

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, username);
            statement.setString(2, password);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                System.out.println("Login successful! Welcome, " + username + "!");
            } else {
                System.out.println("Invalid username or password.");
            }
        } catch (SQLException e) {
            System.out.println("Error logging in: " + e.getMessage());
        }
    }

    // Show all registered users
    private static void showAllUsers() {
        System.out.println("\n==== All Registered Users ====");
        String sql = "SELECT username FROM Users";

        try (Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(sql)) {

            while (resultSet.next()) {
                System.out.println("Username: " + resultSet.getString("username"));
            }
        } catch (SQLException e) {
            System.out.println("Error fetching users: " + e.getMessage());
        }
    }

    // Add a new category
    private static void addCategory(Scanner scanner) {
        System.out.println("\n==== Add Category ====");
        System.out.print("Enter category name: ");
        String categoryName = scanner.nextLine();
        System.out.print("Enter category type (Income/Expense): ");
        String categoryType = scanner.nextLine();

        String sql = "INSERT INTO ExpenseCategories (Name, Type) VALUES (?, ?)";

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, categoryName);
            statement.setString(2, categoryType);
            statement.executeUpdate();
            System.out.println("Category added successfully!");
        } catch (SQLException e) {
            System.out.println("Error adding category: " + e.getMessage());
        }
    }

    // Add a new transaction
    private static void addTransaction(Scanner scanner) {
        System.out.println("\n==== Add Transaction ====");
        System.out.print("Enter category ID: ");
        int categoryId = scanner.nextInt();
        System.out.print("Enter transaction amount: ");
        double amount = scanner.nextDouble();
        scanner.nextLine(); // Consume newline character

        // Adjust amount based on category type
        String getCategoryTypeSql = "SELECT Type FROM ExpenseCategories WHERE Id = ?";
        try (PreparedStatement getCategoryTypeStmt = connection.prepareStatement(getCategoryTypeSql)) {
            getCategoryTypeStmt.setInt(1, categoryId);
            ResultSet rs = getCategoryTypeStmt.executeQuery();

            if (rs.next()) {
                String categoryType = rs.getString("Type");
                if (categoryType.equalsIgnoreCase("Expense")) {
                    amount = -Math.abs(amount); // Ensure it's negative for expenses
                }
            } else {
                System.out.println("Invalid category ID. Transaction not added.");
                return;
            }
        } catch (SQLException e) {
            System.out.println("Error checking category type: " + e.getMessage());
            return;
        }

        // Insert transaction
        String sql = "INSERT INTO Transactions (CategoryId, Amount) VALUES (?, ?)";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, categoryId);
            statement.setDouble(2, amount);
            statement.executeUpdate();
            System.out.println("Transaction added successfully!");
        } catch (SQLException e) {
            System.out.println("Error adding transaction: " + e.getMessage());
        }
    }

    // Show all transactions
    private static void showAllTransactions() {
        System.out.println("\n==== All Transactions ====");
        String sql = "SELECT t.Id, c.Name, t.Amount, t.TransactionDate " +
                "FROM Transactions t " +
                "JOIN ExpenseCategories c ON t.Id = c.Id";

        try (Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(sql)) {

            while (resultSet.next()) {
                System.out.println("Transaction ID: " + resultSet.getInt("Id"));
                System.out.println("Category: " + resultSet.getString("Name"));
                System.out.println("Amount: " + resultSet.getDouble("Amount"));
                System.out.println("Date: " + resultSet.getTimestamp("TransactionDate"));
                System.out.println("------------------------");
            }
        } catch (SQLException e) {
            System.out.println("Error fetching transactions: " + e.getMessage());
        }
    }
}
