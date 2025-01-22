package controller;


import model.DbConnection;
import model.Transaction;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

    public class TransactionController {
        private final Connection connection = DbConnection.getConnection();

        // Add a new transaction
        public void addTransaction(Transaction transaction) {
            String sql = "INSERT INTO Transactions (Amount, CategoryId) VALUES (?, ?)";
            try (PreparedStatement statement = connection.prepareStatement(sql)) {
                double amount = transaction.getAmount();
                int categoryId = transaction.getCategoryId();

                // Validate if category is income or expense
                String typeQuery = "SELECT Type FROM ExpenseCategories WHERE Id = ?";
                try (PreparedStatement typeStatement = connection.prepareStatement(typeQuery)) {
                    typeStatement.setInt(1, categoryId);
                    ResultSet resultSet = typeStatement.executeQuery();
                    if (resultSet.next()) {
                        String type = resultSet.getString("Type");
                        if ("expense".equals(type)) {
                            amount = -Math.abs(amount); // Ensure it's negative for expense
                        }
                    }
                }

                statement.setDouble(1, amount);
                statement.setInt(2, categoryId);
                statement.executeUpdate();
                System.out.println("Transaction added successfully!");
            } catch (SQLException e) {
                System.out.println("Error adding transaction: " + e.getMessage());
            }
        }

        // Get all transactions
        public List<Transaction> getAllTransactions() {
            List<Transaction> transactions = new ArrayList<>();
            String sql = "SELECT * FROM Transactions";
            try (Statement statement = connection.createStatement();
                 ResultSet resultSet = statement.executeQuery(sql)) {
                while (resultSet.next()) {
                    transactions.add(new Transaction(
                            resultSet.getInt("Id"),
                            resultSet.getDouble("Amount"),
                            resultSet.getInt("CategoryId"),
                            resultSet.getDate("TransactionDate")
                    ));
                }
            } catch (SQLException e) {
                System.out.println("Error fetching transactions: " + e.getMessage());
            }
            return transactions;
        }


}
