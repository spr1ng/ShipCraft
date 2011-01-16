/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package shipcraft.intrfc;

import shipcraft.model.Action;

/**
 * @version $Id: CorpusAI.java 76 2010-06-23 02:05:43Z spr1ng $
 * @author stream
 */
public interface CorpusAI {

    /**
     * Должен возвращать действие типа Action для корабля с id = corpusId
     * @return
     */
    public Action getAction();
    
}
