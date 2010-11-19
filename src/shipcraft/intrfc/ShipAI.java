/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package shipcraft.intrfc;

import shipcraft.model.Action;
import shipcraft.model.Field;

/**
 *
 * @author spr1ng
 */
public interface ShipAI extends CorpusAI{
    /**
     * Должен возвращать действие типа Action для корабля с id = corpusId
     * @param corpusId id тела, которым управляет интеллект
     * @param field поле со списком всех корпусов
     * @param isTeamMatch командный матч (не стрелять в корабли с одной команды)
     * @return
     */
    public Action getAction(String corpusId, Field field, boolean isTeamMatch);
    /** Должен возвращать имя корабля */
    public String getShipName();
    /** Должен возвращать название команды корабля */
    public String getTeamName();
}
