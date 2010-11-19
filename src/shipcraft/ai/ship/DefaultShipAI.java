/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package shipcraft.ai.ship;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;
import shipcraft.intrfc.CorpusAI;
import shipcraft.intrfc.ShipAI;
import shipcraft.model.Action;
import shipcraft.model.Corpus;
import shipcraft.model.Field;
import shipcraft.model.Ship;
import shipcraft.model.missile.Missile;
import static shipcraft.core.Constants.*;
import static shipcraft.core.Config.*;
import static shipcraft.utils.Utils.*;

/**
 *
 * @author spr1ng
 * @version $Id: DefaultShipAI.java 62 2010-06-17 02:47:34Z stream $
 */
public class DefaultShipAI implements ShipAI{
    private Action action;
    private Field field;
    private Ship myShip;
    /** Visible enemy ships without this AI ship */
    private ArrayList<Ship> enemies;
    /** Visible ally ships without this AI ship */
    private ArrayList<Ship> allies;
    /** All ships (enemies + allies) without this AI ship */
    private ArrayList<Ship> allShips;
    /** All corpuses (enemies + allies) without this AI ship */
    private ArrayList<Corpus> allCorpuses;

    public Field getField() {
        return field;
    }

    public Ship getMyShip() {
        return myShip;
    }
    /** Получает все видимые корабли врагов без корабля аишки */
    public ArrayList<Ship> getEnemies() {
        return enemies;
    }
    /** Получает все корабли союзников без корабля аишки */
    public ArrayList<Ship> getAllies() {
        return allies;
    }
    /** Получает все корабли союзников и видимые корабли врагов без корабля аишки */
    public ArrayList<Ship> getAllShips() {
        return allShips;
    }

    public ArrayList<Corpus> getAllCorpuses() {
        return allCorpuses;
    }
    
    public Action getAction() {
        return action;
    }

    public void setAction(Action action) {
        this.action = action;
    }

    public void initAI(String myShipId, Field field, boolean isTeamMatch) {
        this.field = field;
        this.myShip = (Ship) field.getCorpusById(myShipId);
        this.allCorpuses = field.getCorpuses();
        
        if (isTeamMatch) {
            //Только видимые корабли противников
            enemies = field.getEnemyShips(getTeamName(), true);
            allies = field.getAllyShips(getTeamName());
            allies.remove(myShip);
        } else {
            //Все видимые корабли
            enemies = field.getShips(true);
            enemies.remove(myShip);
            allies = null;//кругом враги
        }

        allShips = new ArrayList<Ship>();
        if (allies != null) {allShips.addAll(allies);}
        allShips.addAll(enemies);
        
    }

    /** Первым делом пытаемся уклониться */
    public Action getEvaseAction(){
        return null;
    }

    public Action getAction(String myShipId, Field field, boolean isTeamMatch) {
        initAI(myShipId, field, isTeamMatch);

        action = null;
        String aiComment = null;
        //Если можно - уклоняемся
        action = getEvaseAction();
        if (action != null) return action;
        //Если есть возможность для апгрейда, то делаем случайный апгрейд
        if (myShip.getUpgradePoints() > 0){
            //Если маловато ракет..
            if (myShip.getMissileQuantity() < 10)
                action = new Action("missile++");
            else
                action = getUpgrade();
        }
        //Если можем ХОРОШО спрятаться - прячемся (invision)
        if (action == null && myShip.canHide() && myShip.getInvisibleLevel() > 2)
            action = new Action("hide");
        //Если можем кого-нибудь подстрелить (если, конечно, есть ракеты)
        if (action == null && myShip.getMissileQuantity() > 0)
            action = getAttackAction();
        //Если решили куда-то стрелять..
        if (action != null) {
            //Если командная игра и этим выстрелом мы попадем в союзника..
            if (isTeamMatch && isAllyDamaged(action)){
                action = null;//сбрасываем действие
                aiComment = "Missile attack will cause ally-damage. Desided not to attack.";
            }
        }
        //Иначе - идем на врага
        if (action == null) {
            action = getMoveAction();
            //Если решили куда-то пойти..
            if (action != null) {
                //Если командная игра и этим перемещением мы протараним союзника..
                if (isTeamMatch && isAllyDamaged(action)){
                    action = null;//сбрасываем действие
                    aiComment = "Movement will cause ally-damage. Desided not to move.";
                }
            }
            //Если этим перемещением мы протараним себя..
            if (action != null && isMyShipDamaged()){
                //Если у нас все еще есть ракеты..
                if (myShip.getMissileQuantity() > 0){
                    aiComment = "Movement will cause self-damage. Desided not to move. "+
                            "dx: " + action.getDx() + "; dy: " + action.getDy() + "; speed: "+ action.getSpeed();
                    action = null;//сбрасываем действие
                }
            }
        }
//        if (action == null) action = getMoveAction();
        
        //Если все спрятались или погибли ТТ
        if (action == null) action = new Action();
        //Если врагов не видно..
        if (enemies.size() == 0) aiComment = "See no enemies!";
        //Комментируем действие..
        action.setAiComment(aiComment);

        return action;
    }

    /**
     * Анализирует пострадают ли корабли союзников
     * в результате выполненного кораблем AI'шки
     * @param action
     * @return
     */
    public boolean isAllyDamaged(Action action){
        if (action == null) {
            System.out.println("Warning isAllyDamaged was called with incorrect params.");
            return false;
        }
        String actionType = action.getType();

        int dx = action.getDx();
        int dy = action.getDy();
        int speed = action.getSpeed();
        
        Corpus corpus = myShip;
        corpus.getAi().getAction().setDx(dx);
        corpus.getAi().getAction().setDy(dy);
        if (speed == 0) speed = corpus.getMaxSpeed();
        corpus.getAi().getAction().setSpeed(speed);
        //Если это ракетная атака
        if (actionType.equalsIgnoreCase("missile") || actionType.contains("_missile")){
            String missileId = myShip.getId() + (myShip.getMissileIdx()+1);
            int missilePosX = myShip.getMiddleX();
            int missilePosY = myShip.getMiddleY();
            corpus = new Missile(missileId, missilePosX, missilePosY, dx, dy);
        }

        //Проверяем, не заденем ли союзника своим перемещением
        Corpus damagedCorpus = getFirstDamagedCorpus(corpus, allCorpuses);
        //Если это корабль
        if (damagedCorpus instanceof Ship){//damagedCorpus !=null &&
            //смотрим название его команды..
            Ship damagedShip = (Ship)damagedCorpus;
            String damagedShipTeamName = damagedShip.getTeamName();
            if (damagedShipTeamName.equals(getTeamName()))
                return true;
            else
                return false;
        }
        
        //Если никого не задело..
        return false;
    }

    /**
     * Анализирует пострадает ли корабль AI'шки
     * @return
     */
    public boolean isMyShipDamaged(){
        if (action == null || !action.getType().equalsIgnoreCase("move")) {
            System.err.println(myShip.getId() + " __ Warning: incorrect action set before isMyShipDamaged() method was called.");
            return false;
        }
        //Проверяем, не заденем ли союзника своим перемещением
        Corpus damagedCorpus = getFirstDamagedCorpus(myShip, allCorpuses);
        //Если можем протаранить кого-то..
        if (damagedCorpus != null) return true;
        
        //Если не пострадает..
        return false;
    }

    /**
     * Получает первое тело из списка тел, пересеченного телом (corpus)
     * @param corpus
     * @param corpuses
     * @return
     */
    public Corpus getFirstDamagedCorpus(Corpus corpus, List<Corpus> corpuses){
        boolean isCorpusRemoved = false; //Убрали ли мы этот корпус из списка всех корпусов?
        if (corpuses.contains(corpus)){
            corpuses.remove(corpus);
            isCorpusRemoved = true;
        }
        if (corpus == null) {
            System.err.println("Warning: getFirstDamagedCorpus(..) was called with a null argument!");
            return null;
        }
        CorpusAI ai = corpus.getAi();
        if (ai == null) {
            System.err.println("Warning: getFirstDamagedCorpus(..) corpus ai is null!");
            return null;
        }
        Action corpusAction = ai.getAction();
        int speed = 0;
        if (corpusAction != null) speed = corpusAction.getSpeed();
        if (speed == 0) speed = corpus.getMaxSpeed();
        int dx = ai.getAction().getDx();
        int dy = ai.getAction().getDy();

        //Получаем дефолтное расположение тела
        int defX = corpus.getPosX();
        int defY = corpus.getPosY();
        for (int i = 1; i <= speed; i++) {
            corpus.move(dx*i, dy*i);
            //Определяем границы тела
            int xMin1 = corpus.getPosX();
            int xMax1 = xMin1 + corpus.getWidth() - 1; //PENDING:
            int yMin1 = corpus.getPosY();
            int yMax1 = yMin1 + corpus.getHeight() - 1; //PENDING:
            for (Corpus corpusFromList : corpuses) {
                boolean isCrossedX = false;//Повреждено ли по оси х
                boolean isCrossedY = false;//Повреждено ли по оси у
                //Определяем границы для тела из списка
                int xMin2 = corpusFromList.getPosX();
                int xMax2 = xMin2 + corpusFromList.getWidth() - 1;
                int yMin2 = corpusFromList.getPosY();
                int yMax2 = yMin2 + corpusFromList.getHeight() - 1;
                //тело пересекло другое справа по х?
                if (xMax1 >= xMin2 && xMax1 <= xMax2) {
                    isCrossedX = true;
                }
                //тело пересекло другое слева по х?
                if (xMin1 <= xMax2 && xMin1 >= xMin2) {
                    isCrossedX = true;
                }
                //тело пересекло другое сверху по y?
                if (yMax1 >= yMin2 && yMax1 <= yMax2) {
                    isCrossedY = true;
                }
                //тело пересекло другое снизу по y?
                if (yMin1 <= yMax2 && yMin1 >= yMin2) {
                    isCrossedY = true;
                }
                //Если тела пересеклись и по х и по y
                if (isCrossedX && isCrossedY) {
                    //Если это ракета своего корабля
                    if (corpus instanceof Missile//PENDING: распутать логику
                            && corpus.getId().substring(0, 1).equalsIgnoreCase(myShip.getId())
                            && corpusFromList.getId().equalsIgnoreCase(myShip.getId())){
                        //Ничего не делать, ибо ракета своего корабля.
                        //TODO: improve
                    } else {
                        //Если тело убирали - возвращаем его обратно
                        if (isCorpusRemoved) corpuses.add(corpus);
                        //Ресетим положение тела
                        corpus.setPosX(defX);
                        corpus.setPosY(defY);
                        return corpusFromList;
                    }
                }
            }
            //Ресетим положение тела
            corpus.setPosX(defX);
            corpus.setPosY(defY);
        }
        
        //Если тело убирали - возвращаем его обратно
        if (isCorpusRemoved) corpuses.add(corpus);
        
        //Если не пересеклись
        return null;
    }

    /** Выбирает апгрейд */
    public Action getUpgrade(){
        int decision = getRanInt(9);
        switch (decision){
            default : return new Action("width++");
            case 1 : return new Action("width--");
            case 2 : return new Action("height++");
            case 3 : return new Action("height--");
            case 4 : return new Action("missile++");
            case 5 : return new Action("armor++");
            case 6 : return new Action("regen++");
            case 7 : return new Action("critical++");
            case 8 : return new Action("evasion++");
            case 9 : return new Action("invision++");
        }
    }

    /** Тип ракеты для атаки */
    public String getMissileType(){
        String missileType = "missile";
        if (myShip.getStunningMissileQuantity()>0)
            missileType = "stunning_missile";
        else if (myShip.getBurningMissileQuantity()>0)
            missileType = "burning_missile";
        else if (myShip.getHeavyMissileQuantity()>0)
            missileType = "heavy_missile";
        return missileType;
    }

    /** Если есть в кого стрелять - стреляем в соотв. направлении */
    public Action getAttackAction(){
        HashMap<Ship, Action> targets = getPossibleTargets();
        Set<Ship> shipSet = targets.keySet();
        //Получаем кол-во врагов, которых можем поразить, в сете кораблей
        int shipsQuant = shipSet.size();
        if (shipsQuant > 0) {
            //Получаем случайного врага
            int ranEnemyIdx = getRanInt(1, shipsQuant) - 1;
            Ship[] ships = shipSet.toArray(new Ship[0]);
//            for (Ship ship : ships) {

//            }
            Ship enemy = ships[ranEnemyIdx];
            action = targets.get(enemy);
            return action;
        }
        
        //Иначе никого не атакуем :(
        return null;
    }

    /**
     * Возвращает список врагов, которых можно поразить и действие,
     * необходимое для этого (HashMap<Enemy, Action>)
     * @return
     */
    public HashMap<Ship, Action> getPossibleTargets(){
        int myWidth = myShip.getWidth();
        int myHeight = myShip.getHeight();
        int myLeftBorder = myShip.getPosX();
        int myRightBorder = myLeftBorder + myWidth-1;
        int myTopBorder = myShip.getPosY();
        int myBottomBorder = myTopBorder + myHeight-1;
        int myMiddleX = myShip.getMiddleX();
        int myMiddleY = myShip.getMiddleY();

//        enemies.remove(myShip);
        HashMap<Ship, Action> targets = new HashMap<Ship, Action>();
        for (Ship ship : enemies) {
            int shipLeftBorder = ship.getPosX();
            int shipRightBorder = ship.getPosX() + ship.getWidth()-1;
            int shipTopBorder = ship.getPosY();
            int shipBottomBorder = ship.getPosY() + ship.getHeight()-1;
            int shipMiddleX = ship.getMiddleX();
            int shipMiddleY = ship.getMiddleY();

            String missileType = getMissileType();

            ////////////////////////////////////////////////////////////////////
            //Кто-то есть по вертикали..
            ////////////////////////////////////////////////////////////////////
            if(myMiddleX >= shipLeftBorder && myMiddleX <= shipRightBorder){
                //Сверху
                if (shipMiddleY < myTopBorder){
                    targets.put(ship, new Action(missileType, MOVE_0, MOVE_UP));
                //Снизу
                } else {
                    targets.put(ship, new Action(missileType, MOVE_0, MOVE_DOWN));
                }
            }
            ////////////////////////////////////////////////////////////////////
            //Кто-то есть по горизонтали..
            ////////////////////////////////////////////////////////////////////
            if(myMiddleY >= shipTopBorder && myMiddleY <= shipBottomBorder){
                //Сверху
                if (shipMiddleX < myLeftBorder){
                    targets.put(ship, new Action(missileType, MOVE_LEFT, MOVE_0));
                //Снизу
                } else {
                    targets.put(ship, new Action(missileType, MOVE_RIGHT, MOVE_0));
                }
            }
            ////////////////////////////////////////////////////////////////////
            //Кто-то есть по диагонали СЕВЕРО-ЗАПАД--ЮГО-ВОСТОК
            ////////////////////////////////////////////////////////////////////
            //Находим расстояние до края по оси X
            int dx = FIELD_WIDTH - myMiddleX - 1;
            //Находим расстояние до края по оси Y
            int dy = FIELD_HEIGHT - myMiddleY - 1;
            //Находим разницу м/у расстояниями
            int z = dx - dy;
            //Если расстояние по горизонтали > расстояния по вертикали
            if (z >= 0) {
                dx = FIELD_WIDTH -1 - z;
                dy = FIELD_HEIGHT - 1;
            //иначе
            } else {
                dx = FIELD_WIDTH - 1;
                dy = FIELD_HEIGHT - 1 + z;
            }
            while (dx > 0 && dy > 0){
                //Если траектория полета ракеты пересечет врага по оси ОХ
                if (dx >= shipLeftBorder && dx <= shipRightBorder)
                    //и по ОУ
                    if (dy >= shipTopBorder && dy <= shipBottomBorder){
                        if (shipMiddleX > myMiddleX) {
                            targets.put(ship, new Action(missileType, MOVE_RIGHT, MOVE_DOWN));
                        } else {
                            targets.put(ship, new Action(missileType, MOVE_LEFT, MOVE_UP));
                        }
                    }
                dx--; dy--;
            }
            ////////////////////////////////////////////////////////////////////
            //Кто-то есть по диагонали СЕВЕРО-ВОСТОК--ЮГО-ЗАПАД
            ////////////////////////////////////////////////////////////////////
            //Находим расстояние до края по оси X
            dx = myMiddleX - 1;
            //Находим расстояние до края по оси Y
            dy = FIELD_HEIGHT - myMiddleY - 1;
            //Находим разницу м/у расстояниями
            z = dx - dy;
            //Если расстояние по горизонтали > расстояния по вертикали
            if (z >= 0) {
                dx = z + 1;
                dy = FIELD_HEIGHT - 1;
            //иначе
            } else {
                dx = 1;
                dy = FIELD_HEIGHT - 1 + z;
            }
            while (dx < FIELD_WIDTH && dy > 0){
                //Если траектория полета ракеты пересечет врага по оси ОХ
                if (dx >= shipLeftBorder && dx <= shipRightBorder)
                    //и по ОУ
                    if (dy >= shipTopBorder && dy <= shipBottomBorder){
                        if (shipMiddleX > myMiddleX) {
                            targets.put(ship, new Action(missileType, MOVE_RIGHT, MOVE_UP));
                        } else {
                            targets.put(ship, new Action(missileType, MOVE_LEFT, MOVE_DOWN));
                        }
                    }
                dx++; dy--;
            }
            ////////////////////////////////////////////////////////////////////
        }

        return targets;
    }

    public Action getMoveAction(){
        //Если все погибли (или спрятались) - стоять и наслаждаться победой! ;)
        if (enemies == null || enemies.size() == 0) return null;
        Ship enemy = enemies.get(0);

        int enemyMiddleX = enemy.getMiddleX();
        int enemyMiddleY = enemy.getMiddleY();
        int myMiddleX = myShip.getMiddleX();
        int myMiddleY = myShip.getMiddleY();
        
        int xMove = 0, yMove = 0;
        //Если враг правее
        if (enemyMiddleX >= myMiddleX) {
            xMove = MOVE_RIGHT;//идем вправо
        } else {
            xMove = MOVE_LEFT;//идем влево
        }    
        //Если враг ниже
        if (enemyMiddleY >= myMiddleY){
            yMove = MOVE_DOWN;//идем вниз
        //Если враг выше   
        } else {
            yMove = MOVE_UP;//идем вверх
        }
        
        int speed = getRanInt(1, myShip.getMaxSpeed());
        
        return new Action("move", speed, xMove, yMove);
    }

    public String getShipName() {
        return "Defa";
    }

    public String getTeamName() {
        return "Defos";
    }



    
    /*private String getRandomMissileType(){
        int decision = getRanInt(2);
        switch (decision){
            default : return "missile";
            case 1 : return "heavy_missile";
            case 2 : return "stunning_missile";
        }
    }*/
    
    /*private Action getRndMoveAction(){
        Action action = new Action();
        action.setDx(getRanInt(-1, 1));
        action.setDy(getRanInt(-1, 1));
        action.setType("move");
        return action;
    }
    
    private Action getRndAttackAction(String type){
        Action action = new Action();
        int moveX = 0, moveY = 0;
        do {            
           moveX = getRanInt(-1,1); 
           moveY = getRanInt(-1,1); 
        } while (moveX == 0 && moveY == 0);
        action.setDx(moveX);
        action.setDy(moveY);
        action.setType(type);
        return action;
    }*/

}
