/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package shipcraft.model.missile;

import static shipcraft.utils.Utils.*;

/**
 * @version $Id: StunningMissile.java 76 2010-06-23 02:05:43Z spr1ng $
 * @author spr1ng
 */
public class StunningMissile extends Missile{

    public StunningMissile(int dx, int dy) {
        super(dx, dy);
        setDamage(getRanInt(1, 2)); // *10
        setMaxSpeed(getRanInt(30, 40));
    }
    
}
