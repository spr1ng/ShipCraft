/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package shipcraft.model;

import shipcraft.model.missile.Missile;
import shipcraft.model.missile.StunningMissile;
import java.text.Collator;
import java.text.ParseException;
import java.text.RuleBasedCollator;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import shipcraft.model.missile.BurningMissile;
import static shipcraft.core.Config.*;
import static shipcraft.core.Constants.*;
import static shipcraft.utils.Utils.*;

/**
 * @version $Id: Field.java 76 2010-06-23 02:05:43Z spr1ng $
 * @author spr1ng
 */
public class Field implements Cloneable {

    private ArrayList<Corpus> corpuses = new ArrayList<Corpus>();
    private Corpus nextCorpusToMove;

    @Override
    public Object clone() {
        try {
            Field fieldCopy = (Field) super.clone();
            corpuses = (ArrayList<Corpus>) corpuses.clone();
            for (int i = 0; i < corpuses.size(); i++) {
                Corpus corpus = (Corpus) corpuses.get(i).clone();
                corpuses.set(i, corpus);
            }
            return fieldCopy;
        } catch (CloneNotSupportedException ex) {
            return null; //Ибо невозможно =)
        }
    }
    
    /** Список команд, оставшихся на поле */
    public ArrayList<String> getTeamsRemained(){
        ArrayList<String> teams = new ArrayList<String>();
        ArrayList<Ship> ships = getShips();
        for (Ship ship : ships) {
            String team = ship.getTeamName();
            if (!teams.contains(team))
                teams.add(team);
        }
        return teams;
    }

    /**
     * Возвращает все корабли из списка всех тел на поле
     * @param onlyVisible включать в список только видимые корабли
     * @return
     */
    public ArrayList<Ship> getShips(boolean onlyVisible){
        ArrayList<Ship> ships = new ArrayList<Ship>();
        for (Corpus corpus : corpuses) {
            if (corpus instanceof Ship){
                Ship ship = (Ship)corpus;
                //Если выбирать только видимые
                if (onlyVisible){
                    if (ship.isVisible()) ships.add(ship);
                //Если выбирать все
                } else {
                    ships.add(ship);
                }
            }
        }
        return ships;
    }
    
    /** Возвращает все корабли из списка всех тел на поле (включая невидимые) */
    public ArrayList<Ship> getShips(){
        return getShips(false);
    }

    /** 
     * Возвращает все корабли союзников на поле
     * @param team название команды аишки
     * @param onlyVisible включать в список только видимые корабли
     * @return
     */
    public ArrayList<Ship> getAllyShips(String teamName, boolean onlyVisible){
        ArrayList<Ship> ships = getShips(onlyVisible);
        ArrayList<Ship> allyShips = new ArrayList<Ship>();
        for (Ship ship : ships) {
            String curTeamName = ship.getTeamName();
            if (curTeamName != null && curTeamName.equals(teamName)){
                allyShips.add(ship);
            }
        }
        return allyShips;
    }
    
    /** Возвращает все корабли союзников на поле (включая невидимые) */
    public ArrayList<Ship> getAllyShips(String teamName){
        return getAllyShips(teamName, false);
    }

    /**
     * Возвращает все вражеские корабли на поле
     * @param team название команды аишки
     * @param onlyVisible включать в список только видимые корабли
     * @return
     */
    public ArrayList<Ship> getEnemyShips(String teamName, boolean onlyVisible){
        ArrayList<Ship> ships = getShips(onlyVisible);
        ArrayList<Ship> enemyShips = new ArrayList<Ship>();
        for (Ship ship : ships) {
            String curTeamName = ship.getTeamName();
            if (curTeamName != null && !curTeamName.equals(teamName)){
                enemyShips.add(ship);
            }
        }
        return enemyShips;
    }
    
    /** Возвращает все вражеские корабли на поле (включая невидимые) */
    public ArrayList<Ship> getEnemyShips(String teamName){
        return getEnemyShips(teamName, false);
    }
    
    /** Возвращает все ракеты из списка всех тел на поле */
    public ArrayList<Missile> getMissiles(){
        ArrayList<Missile> missiles = new ArrayList<Missile>();
        for (Corpus corpus : corpuses) {
            if (corpus instanceof Missile){
                Missile missile = (Missile)corpus;
                missiles.add(missile);
            }
        }
        return missiles;
    }

    /**
     * Сортирует переданный список тел в алфавитном порядке
     * @param corpuses
     */
    private static void sortCorpuses(List<Corpus> corpuses){//PENDING
        try {
            //PENDING
            HashMap<String, Corpus> map = new HashMap<String, Corpus>();
            for (Corpus corpus : corpuses) {
                String id = corpus.getId();
                map.put(id, corpus);
            }
            Object[] keys = map.keySet().toArray();
            //Создаем свой собственный коллатор, ибо мы любим сортировать не по дефолту
            StringBuilder rules = new StringBuilder();
            for (int i = 0; i < 500; i++) {
                rules.append(";" + i);
            }
            //Сортируем ключи хэшмэпа
            Collator collator = new RuleBasedCollator(rules.toString());
            Arrays.sort(keys, collator);
            //Очищаем список тел
            corpuses.clear();
            //Добавляем тела согласно отсортированным ключам
            for (int i = 0; i < keys.length; i++) {
                String key = keys[i].toString();
                Corpus corpus = map.get(key);
                corpuses.add(corpus);
            }
            
        } catch (ParseException ex) {
            ex.printStackTrace();
        }
    }

    public Corpus getNextCorpusToMove() {
        if (corpuses.size() == 0) return null;
        return nextCorpusToMove != null ? nextCorpusToMove : corpuses.get(0);
    }

    /* Тело, с которым совершили действие последним */
    public void setNextCorpusToMove(Corpus corpus) {
        //Сортируем список по id-тел
        sortCorpuses(corpuses);
        //Ищем  тело, следующее за телом, с которым совершалось последнее действие
        int id = 0;//индекс текущего корпуса в списке
        for (Corpus corpusFromList : corpuses) {
            //Если id корпус, с которым совершалось последнее действие,
            //оказался последним в списке - выбираем первый
            if (corpus.getId().equals(corpusFromList.getId())) {
                //Засекаем время поиска
                long time1 = System.currentTimeMillis();
                do {
                    //Если корпус оказался последним - индекс следующего
                    //должен быть 0
                    if (id == corpuses.size() - 1) id = -1;
                    //иначе - следующий по списку
                    nextCorpusToMove = corpuses.get(id + 1);
                    //DELME:
                    /*try {
                        writeToFile("set-next-corpus-method-log.txt", "id: " + id +
                                    "; corpus-id: " + corpus.getId() +
                                    "; from-list-corpus-id: " + corpusFromList.getId() +
                                    "; next-corpus-id: " + nextCorpusToMove.getId());
                    } catch (FileNotFoundException ex) {
                        ex.printStackTrace();
                    }*/
                    
                    id++;
                    //Если живых не осталось - выходим
                    if ((System.currentTimeMillis() - time1) > 800){
                        nextCorpusToMove = null;
                        break;
                    }
                } while (nextCorpusToMove.getHp()<=0);
                break;
            }
            id++;
        }
        
    }

    public Field() {
        //Генерируем случайные корабли
        ArrayList<Ship> ships = generateShips(SHIP_QUANTITY);
        corpuses.addAll(ships);
        logCorpuses();
    }

    public ArrayList<Corpus> getCorpuses() {
        
        return corpuses;
    }

    public void setCorpuses(ArrayList<Corpus> corpuses) {
        this.corpuses = corpuses;
    }

    public int getHeight() {
        return FIELD_HEIGHT;
    }

    public int getWidth() {
        return FIELD_WIDTH;
    }

    /**
     *
     * @param quantity кол-во генерируемых кораблей
     * @return
     */
    private static ArrayList<Ship> generateShips(int quantity) {
        ArrayList<Ship> ships = new ArrayList<Ship>();

        for (int i = 0; i < quantity; i++) {
            Ship ship = new Ship(IDENTIFICATORS[i]);
            
            ship.setColor(COLORS[i]);
            //Устанавливаем размеры корабля
            int shipWidth = 0;
            int shipHeight = 0;
            //Убедимся, что длина и ширина - нечетные числа (чтобы всегда была симметричная середина)
            do {
                shipWidth = SHIP_WIDTH_MIN + 2;//getRanInt(SHIP_WIDTH_MIN, SHIP_WIDTH_MAX);
                shipHeight = SHIP_HEIGTH_MIN + 2;//getRanInt(SHIP_HEIGTH_MIN, SHIP_HEIGTH_MAX);
            } while (shipWidth % 2 == 0 || shipHeight % 2 == 0);
            ship.setWidth(shipWidth);
            ship.setHeight(shipHeight);
            
            //Устанавливаем hp
            ship.setHp(shipWidth * shipHeight * 10);
            //Назначаем координату на поле
            int x, y = 0;//Временные координаты
            boolean hasValidCoord;
            do {
                x = getRanInt(1, FIELD_WIDTH - shipWidth);
                y = getRanInt(1, FIELD_HEIGHT - shipHeight);
                //Если в спсиске уже есть корабли - проверяем, чтобы они
                //не пересекались с новым кораблем
                if (!ships.isEmpty()) {
                    hasValidCoord = true;
                    for (Ship shipFromList : ships) {
                        int posX = shipFromList.getPosX();
                        int posY = shipFromList.getPosY();
                        int width = shipFromList.getWidth();
                        int height = shipFromList.getHeight();
                        //Если пересечет корабль из списка по x
                        if (x >= posX - shipWidth - SHIP_INTERVAL && x <= posX + width + SHIP_INTERVAL) {
                            //и по y
                            if (y >= posY - shipHeight - SHIP_INTERVAL && y <= posY + height + SHIP_INTERVAL) {
                                //продолжаем цикл (генерируем другую координату)
                                hasValidCoord = false;
                            }
                        }
                    }
                    //Если список пуст - выходим
                } else {
                    break;
                }
            } while (!hasValidCoord);
            ship.setPosX(x);
            ship.setPosY(y);

            ships.add(ship);
            
        }

        return ships;
    }
    
    /**
     * Анализирует ситуацию на поле:
     *  тела, положения которых пересеклись, получают урон
     * @param corpus тело, с которым было совершено действие
     * @param withoutEvasion если true - подсчитывается урон без
     * учета способности кораблей уклоняться
     */
    public void estimateDamage(Corpus corpus) {
        String corpusId = corpus.getId();
        //Определяем границы тела
        int xMin1 = corpus.getPosX();
        int xMax1 = xMin1 + corpus.getWidth() - 1;
        int yMin1 = corpus.getPosY();
        int yMax1 = yMin1 + corpus.getHeight() - 1;
        //Анализируем поле
        //Временно удаляем тело из списка
        corpuses.remove(corpus);
        for (int i = 0; i < corpuses.size(); i++) {
            Corpus corpusFromList = corpuses.get(i);
            String corpusFromListId = corpusFromList.getId();//для логирования
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
                //И если это не ракета своего корабля
                if (!corpusFromListId.contains(corpusId) && !corpusId.contains(corpusFromListId)) {
                    log.info("-------> Corpuses clashed!! [" + corpusId + "x" + corpusFromListId + "]");
                    if (corpusFromList instanceof Ship){
                        Ship ship = (Ship)corpusFromList;
                        //Если корабль не смог уклониться..
                        //или включен режим подсчета урона без учета способностей
                        //кораблей уклоняться
                        if (!ship.hasEvasion()){
                            corpusFromList.takeDamageFrom(corpus);
                            //Если это парализующая ракета..
                            if (corpus instanceof StunningMissile){
                                //Если время парализованности не больше максимума
                                if (ship.getStunTime() < STUN_TIME_MAX-1){
                                    ship.increaseStunTime();
                                }
                                log.debug(corpusFromListId + " was stunned by " + corpusId);
                            }
                            //Если это зажигательная ракета
                            if (corpus instanceof BurningMissile){
                                BurningMissile missile = (BurningMissile) corpus;
                                int burningDamage = missile.getBurningDamage();
                                ship.setBurningDamage(burningDamage);
                                log.debug(corpusFromListId + " was burned by " + corpusId);
                            }
                        //Если уклонился..
                        } else {
                            log.info(corpusFromListId + " escaped corpus attack (EVASION)");
                        }
                    } else {
                        //Если это не корабль..
                        corpusFromList.takeDamageFrom(corpus);
                    }
                    
                    //Если тело уничтожило другое, то накидываем апгрейд поинтов его хозяину
                    if (corpusFromList instanceof Ship && corpusFromList.getHp() <= 0) {
                        String id = corpus.getId().substring(0, 1);
                        Ship ship = (Ship)getCorpusById(id);
                        log.info("-------> [" + id + "] got additional upgrade points");
                        //Если конечно хозяин сам еще жив
                        if (ship != null) ship.increaseUpgradePoints(2);
                    }
                    corpus.takeDamageFrom(corpuses.get(i));
                }

            }

        }
        //Возвращаем временно убранное тело
        corpuses.add(corpus);
    }

    /** Убирает "убитые" тела */
    public void removeDeadCorpuses() {
        for (Iterator<Corpus> it = corpuses.iterator(); it.hasNext();) {
            Corpus corpus = it.next();
            //Убираем убитые корабли из списка живых
            if (corpus.getHp() <= 0) {
                it.remove();
            }
        }
    }

    /**
     * Возвращает ссылку на тело по его имени из списка тел
     * @param name
     * @return
     */
    public Corpus getCorpusById(String id) {
        for (Corpus corpus : corpuses) {
            if (corpus.getId().equals(id)) {
                return corpus;
            }
        }
        return null;
    }

    /** Логирует данные о телах */
    public void logCorpuses() {
        for (Corpus corpus : corpuses) {
            log.info("id: " + corpus.getId() + "; hp: " + corpus.getHp());
            log.info("posX: " + corpus.getPosX() +
                    "; posY: " + corpus.getPosY());
            log.info("width: " + corpus.getWidth() +
                    "; height: " + corpus.getHeight());
            log.info("/////////////////////////////////////////////////");
        }
    }
}
