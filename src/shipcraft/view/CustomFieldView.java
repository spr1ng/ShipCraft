/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package shipcraft.view;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.List;
import javax.imageio.ImageIO;
import shipcraft.core.GameManager;
import shipcraft.model.Field;
import shipcraft.model.Ship;
import shipcraft.model.missile.BurningMissile;
import shipcraft.model.missile.CriticalMissile;
import shipcraft.model.missile.HeavyMissile;
import shipcraft.model.missile.Missile;
import shipcraft.model.missile.StunningMissile;
import static shipcraft.core.Config.*;

/**
 * @version $Id: CustomFieldView.java 76 2010-06-23 02:05:43Z spr1ng $
 * @author max, spr1ng
 */
public class CustomFieldView extends AbstractFieldView {

    private BufferedImage missileImage;

    public static final Color BACKGROUND_COLOR = Color.BLACK;
    public static final Color INNER_COLOR = Color.ORANGE;

    public void loadTextures() {
        try {
            missileImage = ImageIO.read(new File("images/missile.png"));
        } catch (IOException ex) {
            System.out.println("Failed to load texture image");
            ex.printStackTrace();
        }
    }
    public CustomFieldView() {
        super(true); //Double buffered by default
        setBackground(BACKGROUND_COLOR);
        loadTextures();
    }
    
    private int cellX;
    private int cellY;
    
    @Override
    public void paint(Graphics g) {

        super.paint(g);
        Dimension dim = getSize();
        Field f = GameManager.getField();
        List<Ship> ships = f.getShips();
        List<Missile> missiles = f.getMissiles();

        //Precalculate cell sizes
        cellX = dim.width / FIELD_WIDTH;
        cellY = dim.height / FIELD_HEIGHT;
        
        g.setFont(new Font(null, Font.PLAIN, 8));
            
        for (Ship s : ships) {
            //g.fillRect(s.getPosX() * cellX, s.getPosY()  * cellY, s.getWidth() * cellX, s.getHeight() * cellY);
            //Ship borders
            g.setColor(s.getColor());
            
            g.drawRoundRect(s.getPosX() * cellX, s.getPosY() * cellY, s.getWidth() * cellX, s.getHeight() * cellY, 20, 20);

            if (s.isStunned()){
                g.drawString("STUNNED (" + s.getStunTime() + ")", s.getPosX() * cellX, s.getPosY() * cellY + 25);
            }
            if (!s.isVisible()){
                g.drawString("INVISIBLE (" + s.getInvisibleTime() + ")", s.getPosX() * cellX, s.getPosY() * cellY + 45);
            }
            if (s.isBurned()){
                g.drawString("BURNS (" + s.getBurningDamage() + ")", s.getPosX() * cellX, s.getPosY() * cellY + 45);
            }
            

            //Ship id
            g.drawString(s.getId(), s.getPosX() * cellX + 5, s.getPosY() * cellY + 15);
            //HP indicator
            g.setColor(s.getHp() > 100 ? Color.GREEN : Color.RED);
            g.fillRect(s.getPosX() * cellX, s.getPosY() * cellY - 10, s.getHp() / 5, 2);
            g.drawString("HP " + s.getHp(), s.getPosX() * cellX, s.getPosY() * cellY - 14);
            
        }


        final int imageSize = 16;
        Color ovColor = null;
        for(Missile m : missiles) {
            //Labels
            if (m instanceof CriticalMissile) {
                CriticalMissile xmissile = (CriticalMissile)m;
                if (xmissile.hasCriticalDamage()){
                    g.setColor(Color.RED);
                    g.drawString(String.valueOf(xmissile.getDamage()*10), m.getPosX() * cellX, m.getPosY() * cellY - 5);
                    ovColor = Color.RED;
                }
            }
            if (m instanceof StunningMissile) {
                ovColor = Color.BLUE;
                g.setColor(ovColor);
                g.drawString("STUNN", m.getPosX() * cellX, m.getPosY() * cellY - 5);
                
            }
            if (m instanceof HeavyMissile) {
                ovColor = Color.YELLOW;
                g.setColor(ovColor);
                g.drawString("HEAVY", m.getPosX() * cellX, m.getPosY() * cellY - 5);
            }
            if (m instanceof BurningMissile) {
                ovColor = Color.WHITE;
                g.setColor(ovColor);
                g.drawString("BURNING", m.getPosX() * cellX, m.getPosY() * cellY - 5);
            }
            //Missile borders
            g.setColor(m.getColor());
            g.fillOval(m.getPosX() * cellX, m.getPosY() * cellY, m.getWidth() * cellX * 2 / 3, m.getHeight() * cellY * 2 / 3);
            g.setColor(ovColor);
            g.drawOval(m.getPosX() * cellX, m.getPosY() * cellY, m.getWidth() * cellX * 2 / 3, m.getHeight() * cellY * 2 / 3);
            /*if(m.getAngle() != -1.0)
                drawRotatedImage(missileImage, g, m.getPosX() * cellX - imageSize/2,
                    m.getPosY() * cellY - imageSize/2, imageSize, imageSize, m.getAngle());*/
            
        }
    }
    
    /*private void drawFilledOval(Corpus c, Graphics g){
        g.drawOval(c.getPosX() * cellX, c.getPosY() * cellY, c.getWidth() * cellX * 2 / 3, c.getHeight() * cellY * 2 / 3);
        g.fillOval(c.getPosX() * cellX, c.getPosY() * cellY, c.getWidth() * cellX * 2 / 3, c.getHeight() * cellY * 2 / 3);
    }*/

    public void drawRotatedImage(Image image, Graphics g, int x, int y, int width, int height, double angle) {
        
        Graphics2D g2d = (Graphics2D)g;
        AffineTransform origTransform = (AffineTransform)g2d.getTransform();
        AffineTransform newTransform = (AffineTransform)(g2d.getTransform().clone());
        int imageRotX = width/2;
        int imageRotY = height/2;
        newTransform.rotate(Math.toRadians(angle), x + imageRotX, y + imageRotY);
        g2d.setTransform(newTransform);
        g2d.drawImage(image, x, y, null);
        g2d.setTransform(origTransform);
    }

    public void update() {
        repaint();
    }
}
