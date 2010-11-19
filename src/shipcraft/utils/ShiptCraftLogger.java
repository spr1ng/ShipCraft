/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package shipcraft.utils;

import shipcraft.intrfc.Loggable;
import static shipcraft.core.Config.*;

/**
 * @version $Id: ShiptCraftLogger.java 76 2010-06-23 02:05:43Z spr1ng $
 * @author stream, spr1ng
 */
public class ShiptCraftLogger {
    private static ShiptCraftLogger scl = new ShiptCraftLogger();
    private Loggable loggable;
    private String cache = "";

    private ShiptCraftLogger(){}

    public void setLoggable(Loggable newLoggable){
        this.loggable = newLoggable;
    }

    public static ShiptCraftLogger getLogger(){
        return scl;
    }

    public static ShiptCraftLogger getLogger(Loggable loggable){
        scl.setLoggable(loggable);
        return scl;
    }

    public String cache(){
        return cache;
    }

    public void info(String text){
        if (canLog(text))
            loggable.info("\n\rInfo: " + text);
    }

    public void debug(String text){
        if (IS_DEBUG_MODE)
            if (canLog(text))
                loggable.info("\n\rDebug: " + text);
    }

    public void warning(String text){
        if (canLog(text))
            loggable.info("\n\rWarning: " + text);
    }

    private boolean canLog(String text) {
        if (loggable == null) {
            cache += "\n\r" + text;
            return false;
        }
        return true;
    }

}
