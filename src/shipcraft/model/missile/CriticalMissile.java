/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package shipcraft.model.missile;

import static shipcraft.utils.Utils.*;

/**
 * @version $Id: CriticalMissile.java 76 2010-06-23 02:05:43Z spr1ng $
 * @author spr1ng
 */
public class CriticalMissile extends Missile{
    private boolean criticalDamage;
    
    public CriticalMissile(int dx, int dy, int criticalAttackLevel) {
        super(dx, dy);
        setMaxSpeed(getRanInt(22, 30));
        //Шанс, что 1 ракета из 5 будет нести критический урон
        if (getRanInt(5 - criticalAttackLevel) == 0) {
            setDamage(getRanInt(15, 17)); // *10
            criticalDamage = true;
        }
            
    }
    
    public boolean hasCriticalDamage(){
        return criticalDamage;
    }
    
}
