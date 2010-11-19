/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package shipcraft.ai.ship;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import shipcraft.model.Action;
import shipcraft.model.Corpus;
import shipcraft.model.Ship;
import shipcraft.model.missile.Missile;
import static shipcraft.core.Config.*;
import static shipcraft.core.Constants.*;
import static shipcraft.utils.Utils.*;

/**
 * @version $Id: EvasorAI.java 76 2010-06-23 02:05:43Z spr1ng $
 * @author spr1ng
 */
public class EvasorAI extends DefaultShipAI{

    @Override
    public String getShipName() {
        return "Evasor";
    }

    @Override
    public String getTeamName() {
        return "Evasors";
    }

    @Override
    public Action getUpgrade() {
        Ship myShip = getMyShip();
        if (myShip.canIncreaseEvasion())
            return new Action("evasion++");
        if (myShip.getInvisibleLevel() < 2)
            return new Action("invision++");
        //иначе..
        return null;//super.getUpgrade();
    }

    @Override
    public Action getEvaseAction() {//TODO: усилить анализ
        ArrayList<Action> allActions = new ArrayList<Action>();
        ArrayList<Action> copyAllActions = new ArrayList<Action>();
        ArrayList<Missile> missiles = getDangerousMissiles();
        for (Missile missile : missiles) {
            Set<Action> actions = getEvaseActions(missile);
            allActions.addAll(actions);
        }
        copyAllActions.addAll(allActions);
        for (Action action1 : allActions) {
            int idx1 = allActions.indexOf(action1);
            for (Action action2 : copyAllActions) {
                int idx2 = copyAllActions.indexOf(action2);
                if (idx1 != idx2){
                    if (action1.getDx() == action2.getDx())
                        if (action1.getDy() == action2.getDy())
                            if (action1.getSpeed() == action2.getSpeed())
                                action1.increaseRating();
                }
            }
        }

        ArrayList<Action> maxRatedActions = getMaxRatedActions(allActions);
        
        //Возвращаем случайное действие из списка
        if (maxRatedActions.size() > 0) {
            /*System.out.println("------------------------------------------ALL");
            for (Action action : allActions) {
                System.out.println("dx: " + action.getDx()*action.getSpeed() +
                                   "; dy: " + action.getDy()*action.getSpeed() +
                                   "; rating: " + action.getRating());
            }
            System.out.println("---------------------------------------------");
            System.out.println("------------------------------------------RATED");
            for (Action action : maxRatedActions) {
                System.out.println("dx: " + action.getDx()*action.getSpeed() +
                                   "; dy: " + action.getDy()*action.getSpeed() +
                                   "; rating: " + action.getRating());
            }
            System.out.println("---------------------------------------------");*/
            return maxRatedActions.get(getRanInt(maxRatedActions.size()-1));
        }

        return super.getEvaseAction();
    }

    /**
     * Возвращает список действий с максимальным рейтингом с точки зрения AI
     * @param ratedActions
     * @return
     */
    public ArrayList<Action> getMaxRatedActions(List<Action> ratedActions){
        //Получаем максимальный рейтинг
        int maxRating = 0;
        for (Action action : ratedActions) {
            int rating = action.getRating();
            if (rating > maxRating) maxRating = rating;
        }
        //Добавляем действия с максимальным рейтингом
        ArrayList<Action> maxRatedActions = new ArrayList<Action>();
        for (Action action : ratedActions) {
            int rating = action.getRating();
            if (rating == maxRating) maxRatedActions.add(action);
        }

        return maxRatedActions;
    }

    /**
     * Возвращает список ракет, которые могут попасть в корабль аишки в случае,
     * если корабль останется на месте
     * @return
     */
    public ArrayList<Missile> getDangerousMissiles(){
        ArrayList<Missile> dangerousMissiles = new ArrayList<Missile>();
        ArrayList<Missile> missiles = getField().getMissiles();
        for (Missile missile : missiles) {
            Corpus firstDamagedCorpus = getFirstDamagedCorpus(missile, getAllCorpuses());
            if (firstDamagedCorpus != null && firstDamagedCorpus.equals(getMyShip()))
                dangerousMissiles.add(missile);
        }
        return dangerousMissiles;
    }

    public Set<Action> getEvaseActions(Missile missile){
        Set<Action> evaseActions = new HashSet<Action>();//   new ArrayList<Action>();
        Ship myShip = getMyShip();
        //Данный для установки дефолтного положения корабля
        int myDefPosX = myShip.getPosX();
        int myDefPosY = myShip.getPosY();

        //Перебираем скорость..
        for (int dx = MOVE_LEFT; dx <= MOVE_RIGHT; dx++)
            for (int dy = MOVE_UP; dy <= MOVE_DOWN; dy++) {
                for (int speed = 1; speed <= myShip.getMaxSpeed(); speed++) {
                    //Дефолтим положение корабля
                    myShip.setPosX(myDefPosX);
                    myShip.setPosY(myDefPosY);
                    myShip.move(dx*speed, dy*speed);
                    Action action = new Action("move", speed, dx, dy);
                    //Если корабль не получит урон при перемещении
                    setAction(action);
                    if (!isMyShipDamaged()){
                        int myWidth = myShip.getWidth();
                        int myHeight = myShip.getHeight();
                        int myLeftBorder = myShip.getPosX();
                        int myRightBorder = myLeftBorder + myWidth-1;
                        int myTopBorder = myShip.getPosY();
                        int myBottomBorder = myTopBorder + myHeight-1;
//                        int myMiddleX = myShip.getMiddleX(); //DELME:
//                        int myMiddleY = myShip.getMiddleY(); //DELME: 

                        //Контролируем положение корабля на поле
                        if (myLeftBorder > 1 && myRightBorder < FIELD_WIDTH)
                            if (myTopBorder > 1 && myBottomBorder < FIELD_HEIGHT){
                                ArrayList<Corpus> list = new ArrayList<Corpus>(); list.add(myShip);
                                Corpus firstDamagedCorpus = getFirstDamagedCorpus(missile, list);
                                if (firstDamagedCorpus == null || !firstDamagedCorpus.getId().equals(myShip.getId()))
                                    evaseActions.add(action);
                            }
                    }
                }
            }
        
        setAction(null);//Ресетим action

        return evaseActions;
    }

}
