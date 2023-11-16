/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package TasktifierGUI;

import java.sql.*;

/**
 *
 * @author Daniel
 */
public class SQLMethods {
    private static final String JDBC_URL = "jdbc:mysql://localhost:3306/tasktifier_db";
    private static final String USER = "root";
    private static final String PASSWORD = "";
    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(JDBC_URL, USER, PASSWORD);
    }
    public static void testConnection() throws SQLException {
        try (Connection connection = getConnection()) {
            if (connection != null) {
                System.out.println("Connected to the database!");
            }
        } catch (SQLException e) {
            System.out.println("Not connected to the database! Make sure that MySQL server is open and `tasktifier_db` database exists.");
            System.exit(0);
        }
    }
    public static void insertIntoDatabase(String email, String password) {
        try {
            Connection connection = SQLMethods.getConnection();
            String sql = "INSERT INTO users (email, password) VALUES (?, ?)";
            try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
                preparedStatement.setString(1, email);
                preparedStatement.setString(2, password);
                preparedStatement.executeUpdate();
            }
        } catch (SQLException ex) {}
    }
}
