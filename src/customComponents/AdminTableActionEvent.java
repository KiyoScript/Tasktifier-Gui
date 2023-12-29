/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package customComponents;

/**
 *
 * @author Daniel
 */
public interface AdminTableActionEvent {
    public void updateUser(int row);
    public void deleteUser(int row);
    public void changeUserStatus(int row);
}
