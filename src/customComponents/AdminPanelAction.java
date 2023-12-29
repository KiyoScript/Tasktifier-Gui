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
public class AdminPanelAction extends javax.swing.JPanel {

    /**
     * Creates new form AdminPanelAction
     */
    public AdminPanelAction() {
        initComponents();
    }
    
    public void initEvent(AdminTableActionEvent event, int row){
        UpdateButton.addActionListener((ActionEvent e) -> {
            event.updateUser(row);
        });
        DeleteButton.addActionListener((ActionEvent e) -> {
            event.deleteUser(row);
            if(TasktifierGUI.Index.getAdminInstance() != null){
                TasktifierGUI.Index.getAdminInstance().dispose();
            }
            if(TasktifierGUI.Admin.getInstance() != null){
                TasktifierGUI.Admin.getInstance().dispose();
            }
            new AdminFrameRefresher();
        });
        ChangeStatusButton.addActionListener((ActionEvent e) -> {
            event.changeUserStatus(row);
            if(TasktifierGUI.Index.getAdminInstance() != null){
                TasktifierGUI.Index.getAdminInstance().dispose();
            }
            if(TasktifierGUI.Admin.getInstance() != null){
                TasktifierGUI.Admin.getInstance().dispose();
            }
            new AdminFrameRefresher();
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

        DeleteButton = new customComponents.ActionButton();
        ChangeStatusButton = new customComponents.ActionButton();
        UpdateButton = new customComponents.ActionButton();

        DeleteButton.setForeground(new java.awt.Color(255, 51, 51));
        DeleteButton.setText("Delete");

        ChangeStatusButton.setForeground(new java.awt.Color(0, 153, 153));
        ChangeStatusButton.setText("Change Status");

        UpdateButton.setForeground(new java.awt.Color(255, 255, 51));
        UpdateButton.setText("Update");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(UpdateButton, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(DeleteButton, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(ChangeStatusButton, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(41, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(DeleteButton, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(ChangeStatusButton, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(UpdateButton, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );
    }// </editor-fold>//GEN-END:initComponents


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private customComponents.ActionButton ChangeStatusButton;
    private customComponents.ActionButton DeleteButton;
    private customComponents.ActionButton UpdateButton;
    // End of variables declaration//GEN-END:variables
}
