/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package shipcraft.view;

import java.awt.Color;
import java.util.List;
import processing.core.PApplet;
import processing.core.PFont;
import processing.core.PImage;
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
 * @version $Id: PPApplet.java 76 2010-06-23 02:05:43Z spr1ng $
 * @author stream, spr1ng
 */
class PPApplet extends PApplet {
    private static final int ROUNDING = 6; //distance between lines (что-то типа скругления =)
    private PFont fontForId, fontForSkills;
    private PImage critImg;
    private int cellX, cellY;

    @Override
    public void setup() {
        size(FIELD_WIDTH * 10, FIELD_HEIGHT * 10, P2D);
        //Precalculate cell sizes
        cellX = width / FIELD_WIDTH;
        cellY = height / FIELD_HEIGHT;
    //loading fonts
        try {
            fontForId = loadFont("font/TheSans-Plain-12.vlw");
            fontForSkills = loadFont("font/ScalaSans-Caps-32.vlw");
        } catch (Exception e) {
            System.out.println("Warning: font is not loaded! Using default.");
        }
    //loading images
        critImg = loadImage("images/crit.png");
        frameRate(30);
        fill(128);
        smooth();
    }
    
    @Override
    public void draw() {
        try {
            //Background
            noStroke();
            fill(0, 20);//black
            rect(0, 0, width, height);

            Field f = GameManager.getField();
            List<Ship> ships = f.getShips();
            List<Missile> missiles = f.getMissiles();

            for (Ship s : ships) {
                int left = s.getPosX() * cellX;
                int right = left + (s.getWidth() - 1) * cellX;
                int top = s.getPosY() * cellY;
                int bottom = top + (s.getHeight() - 1) * cellY;

                //Ship borders
                noFill();
                int shipBorderColor = Color.GRAY.getRGB();//s.getColor().getRGB();
                stroke(shipBorderColor);
                
                line(left + ROUNDING, top, right - ROUNDING, top);        //  BC
                line(right, top + ROUNDING, right, bottom - ROUNDING);    //  DE
                line(right - ROUNDING, top, right, top + ROUNDING);       //  CD
                line(left + ROUNDING, bottom, right - ROUNDING, bottom);  //  GF
                line(right, bottom - ROUNDING, right - ROUNDING, bottom); //  FE
                line(left, top + ROUNDING, left, bottom - ROUNDING);      //  AH
                line(left, bottom - ROUNDING, left + ROUNDING, bottom);   //  HG
                line(left, top + ROUNDING, left + ROUNDING, top);         //  AB

                if (s.isStunned()) {
                    fill(Color.WHITE.getRGB());
                    text("S : " + s.getStunTime(), left - 28, top + 15);
                }
                if (!s.isVisible()) {
                    fill(Color.GREEN.getRGB());
                    text("I : " + s.getInvisibleTime(), left - 28, top + 25);
                }
                if (s.isBurned()) {
                    fill(Color.ORANGE.getRGB());
                    text("B : " + s.getBurningDamage(), left - 28, top + 35);
                }

            //Ship id
                textFont(fontForId, 11);
                fill(s.getColor().getRGB());
                text(s.getId(), s.getMiddleX() * cellX - 3, s.getMiddleY() * cellY + 4);

                int iconSize = 8;
                textFont(fontForSkills, 10);
            //Если скилы отличаются от дефолтных - показываем прокачку
                if (s.getRegenHpLevel() > REGEN_HP_LEVEL)
                    text("R : " + s.getRegenHpLevel(), right + 4, top + 15); //regen
                if (s.getArmor() > ARMOR)
                    text("A : " + s.getArmor(), right + 4, top + 25);        //armor
                if (s.getEvasionLevel() > EVASION_LEVEL)
                    text("E : " + s.getEvasionLevel(), right + 4, top + 35); //evasion
                //critical
                if (s.getCriticalAttackLevel() > 0){
                    image(critImg, left + 5, bottom + 5, iconSize, iconSize);
                    text(s.getCriticalAttackLevel(), left + 16, bottom + 12);
                }                
            //HP indicator
                fill(s.getHp() > 100 ? Color.GREEN.getRGB() : Color.RED.getRGB());
                noStroke();
                rect(left, top - 8, s.getHp() * ((s.getWidth() - 1) * 10) / s.getMaxHp(), 2);
            //Name indicator
                fill(s.getColor().getRGB());
                String teamName = s.getTeamName() != null ? s.getTeamName() : "";
                String shipName = s.getName() != null ? s.getName() : "";
//                text(teamName, left - 10, top - 12);
                text(shipName, right - 16, bottom + 12);
                text(teamName, right - 10, bottom + 20);
                
            }

//            textFont(fontForSkills, 10);
            for (Missile m : missiles) {
                noFill();
                stroke(m.getColor().getRGB());
                ellipse(m.getPosX() * cellX, m.getPosY() * cellY, 6, 6);
                if (m instanceof HeavyMissile) {
                    stroke(Color.GRAY.getRGB());
                    ellipse(m.getPosX() * cellX, m.getPosY() * cellY, 10, 10);
                }
                if (m instanceof StunningMissile) {
                    stroke(Color.WHITE.getRGB());
                    ellipse(m.getPosX() * cellX, m.getPosY() * cellY, 10, 10);
                }
                if (m instanceof BurningMissile) {
                    stroke(Color.ORANGE.getRGB());
                    ellipse(m.getPosX() * cellX, m.getPosY() * cellY, 10, 10);
                }
                if (m instanceof CriticalMissile) {
                    CriticalMissile cm = (CriticalMissile) m;
                    if (cm.hasCriticalDamage()) {
                        stroke(Color.RED.getRGB());
                        ellipse(m.getPosX() * cellX, m.getPosY() * cellY, 10, 10);
                    }
                }
            }

        } catch (Exception e) {
//            System.err.println("Warning. Exception in a draw method. (");
//            e.printStackTrace();
        }
    }

}
