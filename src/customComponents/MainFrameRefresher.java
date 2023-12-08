/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package customComponents;

/**
 *
 * @author Daniel
 */
public class MainFrameRefresher {
    public MainFrameRefresher(){
        TasktifierGUI.Main currentMainFrame = TasktifierGUI.Index.getInstance();
        currentMainFrame.dispose();
            
        TasktifierGUI.Main newMainFrame = new TasktifierGUI.Main();
        TasktifierGUI.Index.setMainFrame(newMainFrame);
        newMainFrame.setVisible(true);
    }
}
