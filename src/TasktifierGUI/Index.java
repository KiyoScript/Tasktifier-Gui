/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package TasktifierGUI;

import user.SQLMethods;
import com.formdev.flatlaf.FlatDarkLaf;
import java.sql.SQLException;
import javax.swing.*;
/**
 *
 * @author Daniel
 */
public class Index {
    static {
        try {
            UIManager.setLookAndFeel(new FlatDarkLaf());
        } catch (UnsupportedLookAndFeelException e) {}
    }
    
    private static Main mainFrame = new Main();
    
    public static Main getMainInstance() {
        if (mainFrame == null) {
            mainFrame = new Main();
        }
        return mainFrame;
    }
    
    public static void setMainFrame(Main mainFrame) {
        Index.mainFrame = mainFrame;
    }
    
    private static Admin adminFrame = new Admin();
    
    public static Admin getAdminInstance() {
        if (adminFrame == null) {
            adminFrame = new Admin();
        }
        return adminFrame;
    }
    
    public static void setAdminFrame(Admin adminFrame) {
        Index.adminFrame = adminFrame;
    }
    
    /**
     * @param args the command line arguments
     * @throws java.sql.SQLException
     */
    
    public static void main(String[] args) throws SQLException {
        user.Authenticator loginChecking = new user.Authenticator();
        SQLMethods.testConnection();
        
        try{
            UIManager.setLookAndFeel(new FlatDarkLaf());
        } catch (UnsupportedLookAndFeelException e){}
        
        Login loginFrame = Login.getInstance();
        
        if(loginChecking.hasLoggedInUsers()){
            if(loginChecking.isAdminLogin()){
                java.awt.EventQueue.invokeLater(() -> {
                    adminFrame.setVisible(true);
                });
            } else {
                java.awt.EventQueue.invokeLater(() -> {
                    mainFrame.setVisible(true);
                });
            }
        } else{
            java.awt.EventQueue.invokeLater(() -> {
                loginFrame.setVisible(true);
            });
        }
    }
}
