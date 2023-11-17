/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package user;

import java.util.HashMap;

/**
 *
 * @author Daniel
 */
public class Authenticator {
    public boolean isValidLogin(String email, String password) {
        TasktifierGUI.SQLMethods authentication = new TasktifierGUI.SQLMethods();
        HashMap<String, String> resultData = authentication.find(email);

        return !resultData.isEmpty() && password.equals(resultData.get("password"));
    }
}
