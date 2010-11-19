/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package shipcraft.intrfc;

import java.awt.MenuContainer;

/**
 * @version $Id: FieldView.java 76 2010-06-23 02:05:43Z spr1ng $
 * @author spr1ng
 */
public interface FieldView extends MenuContainer{
    /** Перерисовка состояния поля */
    void update();
}
