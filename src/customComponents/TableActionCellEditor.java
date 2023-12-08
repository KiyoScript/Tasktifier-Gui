/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package customComponents;

import java.awt.Component;
import javax.swing.DefaultCellEditor;
import javax.swing.JCheckBox;
import javax.swing.JTable;

/**
 *
 * @author Daniel
 */
public class TableActionCellEditor extends DefaultCellEditor{
    private TableActionEvent event;
    
    public TableActionCellEditor(TableActionEvent event){
        super(new JCheckBox());
        this.event = event;
    }
    
    @Override
    public Component getTableCellEditorComponent(JTable jtable, Object o, boolean bln, int row, int column){
        PanelAction action = new PanelAction();
        action.initEvent(event, row);
        action.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        action.setBackground(jtable.getSelectionBackground());
        return action;
    }
}
