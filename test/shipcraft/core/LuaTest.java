/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package shipcraft.core;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

/**
 *
 * @author stream
 */
public class LuaTest {

    public static final void main(String args[]) throws ScriptException {
        ScriptEngineManager mgr = new ScriptEngineManager();
        ScriptEngine e = mgr.getEngineByExtension(".lua");
        try {
            e.eval(new FileReader("Main.lua"));
            System.out.println("y=" + e.get("z"));
        } catch (FileNotFoundException ex) {
            Logger.getLogger(LuaTest.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
