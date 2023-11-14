/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package TasktifierGUI;

import static TasktifierGUI.Login.getConnection;
import com.formdev.flatlaf.FlatDarkLaf;
import java.sql.*;
import javax.swing.*;
/**
 *
 * @author Daniel
 */
public class Index {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        try (Connection connection = getConnection()) {
            if (connection != null) {
                System.out.println("Connected to the database!");
            }
        } catch (SQLException e) {
            System.out.println("Not connected to the database! Make sure that MySQL server is open and `tasktifier_db` database exists.");
            System.exit(0);
        }
        
        try{
            UIManager.setLookAndFeel(new FlatDarkLaf());
        } catch (UnsupportedLookAndFeelException e){}
        Login loginFrame = new Login();
        
        java.awt.EventQueue.invokeLater(() -> {
            loginFrame.setVisible(true);
        });
    }
    
}
