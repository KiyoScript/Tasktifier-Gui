/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package customComponents;

/**
 *
 * @author Daniel
 */
public class AdminFrameRefresher {
    public AdminFrameRefresher(){
        TasktifierGUI.Admin currentAdminFrame = TasktifierGUI.Index.getAdminInstance();
        currentAdminFrame.dispose();
            
        TasktifierGUI.Admin newAdminFrame = new TasktifierGUI.Admin();
        TasktifierGUI.Index.setAdminFrame(newAdminFrame);
        newAdminFrame.setVisible(true);
    }
}
