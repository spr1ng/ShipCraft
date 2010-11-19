/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package shipcraft.core;

import java.awt.Color;
import shipcraft.model.Action;

/**
 * @version $Id: Constants.java 76 2010-06-23 02:05:43Z spr1ng $
 * @author spr1ng, stream
 */
public class Constants {

    //CORPUS   /////////////////////////////////////////////////////////////////////
    /** Переместить тело вверх */
    public static final int MOVE_UP = -1;
    /** Переместить тело вниз */
    public static final int MOVE_DOWN = 1;
    /** Переместить тело влево */
    public static final int MOVE_LEFT = -1;
    /** Переместить тело вправо */
    public static final int MOVE_RIGHT = 1;
    /** Не перемещать тело */
    public static final int MOVE_0 = 0;
    //OTHER    /////////////////////////////////////////////////////////////////////
    /** Набор латинских букв (A-N) */
    public static String IDENTIFICATORS[] = {"A","B","C","D","E","F","G",
                                                   "H","I","J","K","L","M","N",};
    /** Набор цветов для кораблей */
    public static Color COLORS[] = {Color.ORANGE,Color.GREEN,Color.YELLOW,
                                          Color.GRAY,  Color.CYAN, new Color(73, 98, 225),
                                          Color.LIGHT_GRAY,Color.MAGENTA,Color.PINK,
                                          Color.RED};

    //ACTION ALIASES   /////////////////////////////////////////////////////////////
    public static final Action M0 = new Action("move");

    public static final Action MU = new Action("move", MOVE_0, MOVE_UP);
    public static final Action MD = new Action("move", MOVE_0, MOVE_DOWN);
    public static final Action ML = new Action("move", MOVE_LEFT, MOVE_0);
    public static final Action MLU = new Action("move", MOVE_LEFT, MOVE_UP);
    public static final Action MLD = new Action("move", MOVE_LEFT, MOVE_DOWN);
    public static final Action MR = new Action("move", MOVE_RIGHT, MOVE_0);
    public static final Action MRU = new Action("move", MOVE_RIGHT, MOVE_UP);
    public static final Action MRD = new Action("move", MOVE_RIGHT, MOVE_DOWN);

    public static final Action MIU = new Action("missile", MOVE_0, MOVE_UP);
    public static final Action MID = new Action("missile", MOVE_0, MOVE_DOWN);
    public static final Action MIL = new Action("missile", MOVE_LEFT, MOVE_0);
    public static final Action MILU = new Action("missile", MOVE_LEFT, MOVE_UP);
    public static final Action MILD = new Action("missile", MOVE_LEFT, MOVE_DOWN);
    public static final Action MIR = new Action("missile", MOVE_RIGHT, MOVE_0);
    public static final Action MIRU = new Action("missile", MOVE_RIGHT, MOVE_UP);
    public static final Action MIRD = new Action("missile", MOVE_RIGHT, MOVE_DOWN);
    
    public static final Action ARM = new Action("armor++");
    public static final Action REG = new Action("regen++");
    public static final Action MIPLUS = new Action("missile++");
    public static final Action WPLUS = new Action("width++");
    public static final Action WMINUS = new Action("width--");
    public static final Action HPLUS = new Action("height++");
    public static final Action HMINUS = new Action("height--");
           
}
