/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package shipcraft.core;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

/**
 *
 * @author stream
 */
public class LuaTest {
    public static final void main(String args[]) throws ScriptException{
        ScriptEngineManager mgr = new ScriptEngineManager();
	ScriptEngine e = mgr.getEngineByExtension(".lua");
	e.put("x", 25);
//	e.eval("local luaAI = luajava.createProxy('shipcraft.ai.ship.ShipAI', {getUpgrade = function(me) return luajava.newInstance('shipcraft.model.Action','','')end})");
//	System.out.println( "y="+e.get("luaAI") );

    }
}
