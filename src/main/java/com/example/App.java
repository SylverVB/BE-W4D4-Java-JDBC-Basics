package com.example;

import java.sql.*;
import io.github.cdimascio.dotenv.Dotenv;

public class App {
    public static void main(String[] args) {
        // Load environment variables using dotenv
        Dotenv dotenv = Dotenv.load();

        String dbUrl = dotenv.get("DB_URL");
        String dbUser = dotenv.get("DB_USER");
        String dbPassword = dotenv.get("DB_PASSWORD");

        // Check if any of the database credentials are not set properly
        if (dbUrl == null || dbUser == null || dbPassword == null) {
            System.out.println("Database credentials are not set properly.");
            return;
        }

        // Use try-with-resources to automatically close resources
        try (
            // Establishing the connection
            Connection conn = DriverManager.getConnection(dbUrl, dbUser, dbPassword);
            
            // Create a statement to run SQL queries
            Statement stmt = conn.createStatement()
        ) {

            // Create the employees table (if it doesn't exist)
            String createTableQuery = "CREATE TABLE IF NOT EXISTS employees (id INT AUTO_INCREMENT PRIMARY KEY, first_name VARCHAR(50), last_name VARCHAR(50))";
            stmt.executeUpdate(createTableQuery);
            System.out.println("Employees table created.");

            // Insert a sample employee record
            String insertQuery = "INSERT INTO employees (first_name, last_name) VALUES ('John', 'Doe')";
            stmt.executeUpdate(insertQuery);
            System.out.println("Test employee record inserted.");

            // PreparedStatement for a dynamic query
            PreparedStatement preparedStmt = conn.prepareStatement("SELECT * FROM employees");
            
            // Execute the query and get the result set using PreparedStatement
            ResultSet rs = preparedStmt.executeQuery();

            // Iterate over the result set and print the data
            while (rs.next()) {
                int id = rs.getInt("id");
                String firstName = rs.getString("first_name");
                String lastName = rs.getString("last_name");
                System.out.println(id + ": " + firstName + " " + lastName);
            }

            // Resources are automatically closed here due to try-with-resources
        } catch (SQLException e) {
            // Print any SQL exceptions that occur
            e.printStackTrace();
        }
    }
}