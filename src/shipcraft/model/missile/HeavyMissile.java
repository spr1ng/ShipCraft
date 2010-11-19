/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package shipcraft.model.missile;

import static shipcraft.utils.Utils.*;

/**
 * @version $Id: HeavyMissile.java 76 2010-06-23 02:05:43Z spr1ng $
 * @author spr1ng
 */
public class HeavyMissile extends Missile{

    public HeavyMissile(int dx, int dy) {
        super(dx, dy);
        setDamage(getRanInt(4, 11));    // *10
        setMaxSpeed(getRanInt(20, 28));
    }
    
}
