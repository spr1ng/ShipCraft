/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package shipcraft.ai.ship;

import shipcraft.model.Action;
import shipcraft.model.Ship;

/**
 * @author spr1ng
 * @version $Id: DefenderShipAI.java 76 2010-06-23 02:05:43Z spr1ng $
 */
public class DefenderShipAI extends EvasorAI{
    @Override
    public String getShipName() {
        return "4KiriLL";
    }
        
    @Override
    public String getTeamName() {
        return "DFNDR";
    }

    @Override
    public Action getUpgrade(){
        Ship myShip = getMyShip();

        if (myShip.canIncreaseArmor())
            return new Action("armor++");
        if (myShip.canIncreaseRegen())
            return new Action("regen++");
        if (myShip.canIncreaseWidth())
            return new Action("width++");
        if (myShip.canIncreaseHeight())
            return new Action("height++");

        /*int maxIdx = 20;
        for (int i = 0; i <= maxIdx; i++) {
            int decision = getRanInt(2);
            switch (decision){
                //Увеличиваем способность регенирироваться
                default : {
                    if (myShip.getRegenHpLevel() != REGEN_HP_LEVEL_MAX)
                        return new Action("regen++");
                }
                //Увеличиваем ширину
                case 1  : 
                    if (myShip.getWidth() != SHIP_WIDTH_MAX) {
                        return new Action("width++");
                }
                //Увеличиваем высоту
                case 2  : {
                    if (myShip.getHeight() != SHIP_HEIGTH_MAX) 
                        return new Action("height++");
                }
                //Увеличиваем броню
                case 3  : {
                    if (myShip.getArmor() != ARMOR_MAX)
                        return new Action("armor++");
                }
            } 
        }*/
        //Берем ракеты
        return new Action("missile++");
    }



}
