/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package shipcraft.ai.ship;

import shipcraft.model.Action;
import static shipcraft.utils.Utils.*;

/**
 * @version $Id: AttackerShipAI.java 76 2010-06-23 02:05:43Z spr1ng $
 * @author spr1ng
 */
public class AttackerShipAI extends EvasorAI {

    @Override
    public String getShipName() {
        return "AttaQU";
    }

    @Override
    public String getTeamName() {
        return "DESTR-33";
    }

    @Override
    public Action getUpgrade() {
        int decision = getRanInt(5);
        switch (decision) {
            default: return new Action("missile++");
            case 0 : return new Action("critical++");
            case 1 : return new Action("evasion++");
        }
    }
}
