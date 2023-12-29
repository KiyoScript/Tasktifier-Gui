/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package customComponents;

import javax.swing.JSpinner;
import javax.swing.SpinnerModel;
import javax.swing.SpinnerNumberModel;

/**
 *
 * @author Daniel
 */
public class IntegerSpinner extends JSpinner {
    public IntegerSpinner(){
        SpinnerModel infiniteModel = new SpinnerNumberModel(1, 1, Integer.MAX_VALUE, 1);
        this.setModel(infiniteModel);
    }
}
