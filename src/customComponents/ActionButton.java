/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package customComponents;

/**
 *
 * @author Daniel
 */
import javax.swing.JButton;
import javax.swing.border.EmptyBorder;

public class ActionButton extends JButton {
    public ActionButton(){
        setContentAreaFilled(false);
        setBorder(new EmptyBorder(3,3,3,3));
    }
}
