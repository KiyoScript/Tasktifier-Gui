/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package user;

<<<<<<< HEAD
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.ResultSet;
=======
>>>>>>> main
import java.util.HashMap;

/**
 *
 * @author Daniel
 */
public class Authenticator {
    public boolean isValidLogin(String email, String password) {
<<<<<<< HEAD
        user.SQLMethods authentication = new user.SQLMethods();
        HashMap<String, String> resultData = authentication.findUser(email);

        if (resultData == null) { return false; }

        return !resultData.isEmpty() && password.equals(resultData.get("password"));
    }
    
    public void updateLoginStatus(int id, boolean isLoggedIn) {
        user.SQLMethods updateLogin = new user.SQLMethods();
        String query = "UPDATE users SET isLoggedIn = ? WHERE id = ?";

        try (Connection connection = updateLogin.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setBoolean(1, isLoggedIn);
            statement.setInt(2, id);

            int rowsAffected = statement.executeUpdate();

            if (rowsAffected > 0) {
                System.out.println("Login status updated for user with ID " + id);
            } else {
                System.out.println("User with ID " + id + " not found.");
            }
        } catch (SQLException e) {
            System.out.println(e);
        }
    }
    
    public boolean hasLoggedInUsers() {
        String query = "SELECT COUNT(*) FROM users WHERE isLoggedIn = 1";

        try (Connection connection = user.SQLMethods.getConnection();
             PreparedStatement statement = connection.prepareStatement(query);
             ResultSet resultSet = statement.executeQuery()) {

            if (resultSet.next()) {
                int count = resultSet.getInt(1);
                return count > 0;
            }
        } catch (SQLException e) {
            return false;
        }
        return false;
    }
    
    public int getLoggedInUserID() {
        String query = "SELECT id FROM users WHERE isLoggedIn = 1";

        try (Connection connection = user.SQLMethods.getConnection();
            PreparedStatement statement = connection.prepareStatement(query);
            ResultSet resultSet = statement.executeQuery()) {

            if (resultSet.next()) {
                return resultSet.getInt("id");
            }
        } catch (SQLException e) {}
        return -1;
    }
    
    public int getLoggedInUserID(String email) {
        String query = "SELECT id FROM users WHERE email = ?";

        try (Connection connection = user.SQLMethods.getConnection();
            PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setString(1, email);

            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return resultSet.getInt("id");
                }
            }
        } catch (SQLException e) {}
        return -1;
    }
=======
        TasktifierGUI.SQLMethods authentication = new TasktifierGUI.SQLMethods();
        HashMap<String, String> resultData = authentication.find(email);

        return !resultData.isEmpty() && password.equals(resultData.get("password"));
    }
>>>>>>> main
}
