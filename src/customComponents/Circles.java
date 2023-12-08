/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package customComponents;

/**
 *
 * @author Daniel
 */
import javax.swing.*;
import java.awt.*;

public class Circles extends JPanel {
    private int red;
    private int green;
    private int blue;

    public Circles(int red, int green, int blue) {
        this.red = red;
        this.green = green;
        this.blue = blue;
    }
    
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g.create();

        int diameter = 20;
        int x = getWidth() / 2 - diameter / 2;
        int y = getHeight() / 2 - diameter / 2;

        g2d.setColor(new Color(red, green, blue));
        g2d.fillOval(x, y, diameter, diameter);

        g2d.dispose();
    }
}
