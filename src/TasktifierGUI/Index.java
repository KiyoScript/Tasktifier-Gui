/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package TasktifierGUI;

import com.formdev.flatlaf.FlatDarkLaf;
import java.sql.SQLException;
import javax.swing.*;
/**
 *
 * @author Daniel
 */
public class Index {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws SQLException {
        SQLMethods.testConnection();
        
        try{
            UIManager.setLookAndFeel(new FlatDarkLaf());
        } catch (UnsupportedLookAndFeelException e){}
        Login loginFrame = new Login();
        
        java.awt.EventQueue.invokeLater(() -> {
            loginFrame.setVisible(true);
        });
    }
}
