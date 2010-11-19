/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package shipcraft.ai.ship;

import shipcraft.model.Action;
import shipcraft.model.Ship;
import static shipcraft.utils.Utils.*;

/**
 * @version $Id: InviserShipAI.java 76 2010-06-23 02:05:43Z spr1ng $
 * @author spr1ng
 */
public class InviserShipAI extends EvasorAI{

    @Override
    public String getShipName() {
        return "Inva";
    }

    @Override
    public String getTeamName() {
        return "Invisors";
    }

    @Override
    public Action getUpgrade() {
        Ship myShip = getMyShip();
        
        int decision = getRanInt(3);
        Action action = null;
        switch (decision){
            default : if (myShip.canHide()){
                action = new Action("hide"); break;
            }
            case 1  : if (myShip.canIncreaseInvisLevel()){
                action = new Action("invision++"); break;
            }
                            
        }

        if (action == null) action = super.getUpgrade();

        return action;
    }



}
