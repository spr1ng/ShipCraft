/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package shipcraft.view;

import java.awt.GridLayout;
import javax.swing.JPanel;
import shipcraft.intrfc.FieldView;

/**
 * @version $Id: AbstractFieldView.java 76 2010-06-23 02:05:43Z spr1ng $
 * @author stream
 */
public abstract class AbstractFieldView extends JPanel implements FieldView {
    
    public AbstractFieldView(){
        super(true);
        setLayout(new GridLayout());
    }
    
    public AbstractFieldView(boolean doubleBuffering){
        super(doubleBuffering);
    }
}
