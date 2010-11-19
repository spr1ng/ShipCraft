/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package shipcraft.ai.ship;

import java.util.HashMap;
import java.util.Set;
import shipcraft.model.Action;
import shipcraft.model.Ship;

/**
 * @version $Id: SmartAI.java 76 2010-06-23 02:05:43Z spr1ng $
 * @author spr1ng
 */
public class SmartAI extends EvasorAI {

    @Override
    public String getShipName() {
        return "Smarty";
    }

    @Override
    public Action getUpgrade() {
        Ship myShip = getMyShip();
        if (myShip.canIncreaseEvasion()) {
            return new Action("evasion++");
        }
        if (myShip.canIncreaseRegen()) {
            return new Action("regen++");
        }
        if (myShip.canDecreaseWidth()) {
            return new Action("width--");
        }
        if (myShip.canDecreaseHeight()) {
            return new Action("height--");
        }
        //иначе..
        return super.getUpgrade();
    }

    @Override
    public Action getAttackAction() {
        Ship enemy, target = null;

        HashMap<Ship, Action> targets = getPossibleTargets();
        Set<Ship> shipSet = targets.keySet();
        //Получаем кол-во врагов, которых можем поразить, в сете кораблей
        int shipsQuant = shipSet.size();
        if (shipsQuant > 0) {
            //Получаем случайного врага
            //int ranEnemyIdx = getRanInt(1, shipsQuant) - 1;
            Ship[] ships = shipSet.toArray(new Ship[0]);
            target = ships[0];
            if (shipsQuant > 1) {
                for (int shipIdx = 1; shipIdx < shipsQuant; shipIdx++) {
                    enemy = ships[shipIdx];
                    if (enemy.getHp() < target.getHp()) {
                        target = enemy;
                    }
                }
            }
        }

        return targets.get(target);
    }
}
