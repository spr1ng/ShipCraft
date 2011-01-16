/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package shipcraft.view;

import java.awt.Color;
import java.awt.Font;
import javax.swing.JTextArea;
import shipcraft.intrfc.Loggable;

/**
 * @version $Id: LoggableTextArea.java 76 2010-06-23 02:05:43Z spr1ng $
 * @author stream, spr1ng
 */
public class LoggableTextArea extends JTextArea implements Loggable {

    public LoggableTextArea() {
        setLineWrap(true);
        setWrapStyleWord(true);
        setBackground(Color.BLACK);
        setForeground(new Color(83,83,110));
        setFont(new Font("Courier", Font.PLAIN, 10));
        setSize(262, WIDTH);
    }

    public LoggableTextArea(String text){
        this();
        setText(text);
    }

    public void info(String info) {
        append(info);
        setCaretPosition(getDocument().getLength());
    }

    public void error(String err) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void debug(String dbg) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void warning(String war) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

}
