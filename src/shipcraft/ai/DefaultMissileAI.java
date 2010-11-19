/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package shipcraft.ai;

import shipcraft.intrfc.CorpusAI;
import shipcraft.model.Action;

/**
 * @version $Id: DefaultMissileAI.java 76 2010-06-23 02:05:43Z spr1ng $
 * @author spr1ng
 */
public class DefaultMissileAI implements CorpusAI{
    private Action action;

    public DefaultMissileAI(int dx, int dy) {
        setAction(dx, dy);
    }

    private void setAction(int dx, int dy){
        action = new Action();
        action.setType("move");
        action.setDx(dx);
        action.setDy(dy);
    }

    public Action getAction() {
        return action;
    }

}
