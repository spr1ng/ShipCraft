/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package shipcraft.model.missile;

import static shipcraft.utils.Utils.*;

/**
 * Зажигательная ракета (она жжот =)
 * @author spr1ng
 * @version $Id: BurningMissile.java 76 2010-06-23 02:05:43Z spr1ng $
 */
public class BurningMissile extends Missile{
    private int burningDamage;

    public int getBurningDamage() {
        return burningDamage;
    }

    public void setBurningDamage(int burningDamage) {
        this.burningDamage = burningDamage;
    }

    public BurningMissile(int dx, int dy) {
        super(dx, dy);
        setMaxSpeed(getRanInt(12, 17));
        burningDamage = getRanInt(8, 16);
    }

}
