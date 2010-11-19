/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package shipcraft.view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Graphics;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JWindow;

/**
 * @version $Id: SplashScreen.java 76 2010-06-23 02:05:43Z spr1ng $
 * @author stream, spr1ng
 */
public class SplashScreen extends JWindow {

    private BorderLayout borderLayout1 = new BorderLayout();
    private JLabel imageLabel = new JLabel();
    private ImageIcon imageIcon;

    /** Позиция индикатора загрузки */
    private static int pos = 0;

    public static int getPosition() {
        return pos;
    }

    public static int getPositionPercent() {
        return posToPercent(pos);
    }

    /** Устанавливает позицию индикатора в процентах */
    public static void setPositionPercent(int percent) {
        SplashScreen.pos = percentToPos(percent);
    }
    
    public static void increasePositionPercent(int percent) {
        setPositionPercent(posToPercent(pos) + percent);
    }

    private static int percentToPos(int percent){
        return posMax * percent / 100;
    }
    
    private static int posToPercent(int position){
        return position * 100 / posMax;
    }

    public SplashScreen(String imagePath) {
        this.imageIcon = new ImageIcon("images/"+imagePath);
        try {
            init();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
    
    private static int posMax = 1;
    private static int posMin = 22;
    private static int lineY = 0;
    private static Graphics g;
    void init(){
        imageLabel.setIcon(imageIcon);
        getContentPane().setLayout(borderLayout1);
        getContentPane().add(imageLabel, BorderLayout.CENTER);
        setVisible(true);
        pack();
        setLocationRelativeTo(null);
        g = getGraphics();
        g.setColor(new Color(255, 179, 128));
        posMax = getWidth() - 42;
        lineY = getHeight() - 20;
        for (pos = posMin; pos <= posMax; pos++) {
            refreshProgressBar();
            try {
                Thread.sleep(50);
            } catch (InterruptedException ignored) {
                return;
            }
        }
    }
    
    public static void refreshProgressBar(){
        g.fillRect(posMin, lineY, pos, 3);
    }

}
