/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package customComponents;

import java.awt.event.ActionEvent;

/**
 *
 * @author Daniel
 */
public class PanelAction extends javax.swing.JPanel {
    /**
     * Creates new form PanelAction
     */
    public PanelAction() {
        initComponents();
    }
    
    public void initEvent(TableActionEvent event, int row){
        DoneButton.addActionListener((ActionEvent e) -> {
            event.doneTask(row);
            if(TasktifierGUI.Index.getMainInstance() != null){
                TasktifierGUI.Index.getMainInstance().dispose();
            }
            if(TasktifierGUI.Main.getInstance() != null){
                TasktifierGUI.Main.getInstance().dispose();
            }
            new MainFrameRefresher();
        });
        DeleteButton.addActionListener((ActionEvent e) -> {
            event.deleteTask(row);
            if(TasktifierGUI.Index.getMainInstance() != null){
                TasktifierGUI.Index.getMainInstance().dispose();
            }
            if(TasktifierGUI.Main.getInstance() != null){
                TasktifierGUI.Main.getInstance().dispose();
            }
            new MainFrameRefresher();
        });
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        DoneButton = new customComponents.ActionButton();
        DeleteButton = new customComponents.ActionButton();

        DoneButton.setBackground(new java.awt.Color(135, 206, 235));
        DoneButton.setBorder(null);
        DoneButton.setForeground(new java.awt.Color(102, 255, 51));
        DoneButton.setText("Done");
        DoneButton.setOpaque(true);

        DeleteButton.setForeground(new java.awt.Color(255, 51, 51));
        DeleteButton.setText("Delete");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(13, 13, 13)
                .addComponent(DoneButton, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(25, 25, 25)
                .addComponent(DeleteButton, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(37, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addGap(4, 4, 4)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(DoneButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(DeleteButton, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );
    }// </editor-fold>//GEN-END:initComponents


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private customComponents.ActionButton DeleteButton;
    private customComponents.ActionButton DoneButton;
    // End of variables declaration//GEN-END:variables
}
