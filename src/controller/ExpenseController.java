package controller;

import model.DbConnection;
import model.Category;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ExpenseController {
    private final Connection connection = DbConnection.getConnection();

    // Add a new category
    public void addCategory(Category category) {
        String sql = "INSERT INTO ExpenseCategories (Name, Type) VALUES (?, ?)";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, category.getName());
            statement.setString(2, category.getType());
            statement.executeUpdate();
            System.out.println("Category added successfully!");
        } catch (SQLException e) {
            System.out.println("Error adding category: " + e.getMessage());
        }
    }

    // Get all categories
    public List<Category> getAllCategories() {
        List<Category> categories = new ArrayList<>();
        String sql = "SELECT * FROM ExpenseCategories";
        try (Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(sql)) {
            while (resultSet.next()) {
                categories.add(new Category(
                        resultSet.getInt("Id"),
                        resultSet.getString("Name"),
                        resultSet.getString("Type")
                ));
            }
        } catch (SQLException e) {
            System.out.println("Error fetching categories: " + e.getMessage());
        }
        return categories;
    }

    // Delete a category (cascade deletes related transactions)
    public void deleteCategory(int id) {
        String sql = "DELETE FROM ExpenseCategories WHERE Id = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, id);
            statement.executeUpdate();
            System.out.println("Category deleted successfully!");
        } catch (SQLException e) {
            System.out.println("Error deleting category: " + e.getMessage());
        }
    }
}
