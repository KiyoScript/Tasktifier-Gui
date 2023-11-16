/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package TasktifierGUI;

import java.sql.*;
import java.util.HashMap;

/**
 *
 * @author Daniel
 */
public class SQLMethods {
    private static final String JDBC_URL = "jdbc:mysql://localhost:3306/tasktifier_db";
    private static final String USER = "root";
    private static final String PASSWORD = "";
    private static Connection connection;
    
    private SQLMethods(){}
    
    public static Connection getConnection() throws SQLException {
        if (connection == null || connection.isClosed()) {
            connection = DriverManager.getConnection(JDBC_URL, USER, PASSWORD);
        }
        return connection;
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

    public static HashMap<String, String> create(HashMap<String, String> userData) {
        HashMap<String, String> resultData = new HashMap<>();
        try {
            Connection connection = getConnection();
            String sql = "INSERT INTO users (email, password) VALUES (?, ?)";
            try (PreparedStatement preparedStatement = connection.prepareStatement(sql,Statement.RETURN_GENERATED_KEYS)) {
                preparedStatement.setString(1, userData.get("email"));
                preparedStatement.setString(2, userData.get("password"));
                preparedStatement.executeUpdate();

                try (ResultSet generatedKeys = preparedStatement.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        resultData.put("generatedKey", String.valueOf(generatedKeys.getLong(1)));
                    }
                }
            }
        } catch (SQLException ex) {
            resultData.put("error", ex.getMessage());
        }
        return resultData;
    }
    
    public static HashMap<String, String> find(String email) {
        HashMap<String, String> resultData = new HashMap<>();

        try {
            Connection connection = getConnection();
            String sql = "SELECT * FROM users WHERE email = ?";
        
            try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
                preparedStatement.setString(1, email);

                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    if (resultSet.next()) {
                        resultData.put("id", String.valueOf(resultSet.getLong("id")));
                        resultData.put("email", resultSet.getString("email"));
                        resultData.put("password", resultSet.getString("password"));
                    }
                }
            }
        } catch (SQLException ex) {
            resultData.put("error", ex.getMessage());
        }

        return resultData.isEmpty() ? null : resultData;
    }
    
    public static boolean isValidLogin(String email, String password) {
        HashMap<String, String> resultData = SQLMethods.find(email);

        return !resultData.isEmpty() && password.equals(resultData.get("password"));
    }

    
    // Methods below will be implemented soon while implementing tasks GUI
    /*
        public static HashMap<String, String> find(HashMap<String, String> userData)
        public static HashMap<String, String> update(HashMap<String, String> userData)
        public static HashMap<String, String> delete(HashMap<String, String> userData)
    */
}
