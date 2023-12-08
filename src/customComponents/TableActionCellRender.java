/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package customComponents;

import java.awt.Color;
import java.awt.Component;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableCellRenderer;

/**
 *
 * @author Daniel
 */
public class TableActionCellRender extends DefaultTableCellRenderer {
    @Override
    public Component getTableCellRendererComponent(JTable jtable, Object o, boolean isSelected, boolean bln1, int row, int column){
        Component com = super.getTableCellRendererComponent(jtable, o, isSelected, bln1, row, column);

        PanelAction action = new PanelAction();
        
        setHorizontalAlignment(SwingConstants.CENTER);
        
        if(isSelected == false && row % 2 == 0){
            action.setBackground(new Color(50, 50, 50));
        } else {
            action.setBackground(new Color(30, 30, 30));
        }
        if(row % 2 == 0){
            action.setBackground(new Color(50, 50, 50));
        } else {
            action.setBackground(new Color(30, 30, 30));
        }
        return action;  
    }
}
