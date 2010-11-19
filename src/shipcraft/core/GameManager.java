package shipcraft.core;

import shipcraft.view.TrayManager;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.UIManager;
import shipcraft.intrfc.CorpusAI;
import shipcraft.intrfc.ShipAI;
import shipcraft.model.Action;
import shipcraft.model.Corpus;
import shipcraft.model.missile.CriticalMissile;
import shipcraft.model.Field;
import shipcraft.model.missile.HeavyMissile;
import shipcraft.model.missile.Missile;
import shipcraft.model.Ship;
import shipcraft.model.missile.BurningMissile;
import shipcraft.model.missile.StunningMissile;
import shipcraft.utils.ShiptCraftLogger;
import shipcraft.view.GameView;
import shipcraft.view.SplashScreen;
import static shipcraft.core.Config.*;
import static shipcraft.core.Constants.*;
import static shipcraft.utils.Utils.*;

/**
 * @version $Id: GameManager.java 76 2010-06-23 02:05:43Z spr1ng $
 * @author spr1ng, stream
 */
public class GameManager {

    private static Field field;
    private static GameView gameView;
    private static Timer timer;
    private static ShiptCraftLogger log;

    public static Field getField() {
        return field;
    }

    public static void setField(Field field) {
        GameManager.field = field;
    }
    private static boolean gameStarted = false;

    public static boolean isGameStarted() {
        return gameStarted;
    }

    public static void startOrPauseGame() {
        gameStarted = !gameStarted;//Переключаем датчик процесса игры
        if (!gameStarted) {
            timer.cancel();
        } else {
            log.debug("Live script: " + IS_LIVE_SCRIPT);
            log.debug("Team mode: " + IS_TEAM_MODE);
            timer = new Timer();
            TimerTask task = new TimerTask() {

                @Override
                public void run() {
                    //Берем следующее тело
                    Corpus corpus = field.getNextCorpusToMove();
                    //Если включен режим автоматической перезагрузки интеллектов
                    //из скриптов при каждом ходе корабля
                    if (IS_LIVE_SCRIPT) {
                        //Если это корабль
                        if (corpus instanceof Ship) {
                            Ship ship = (Ship) corpus;
                            //присваиваем ему CorpusAI
                            gameView.refreshAi(ship);
                        }
                    }

                    doActionWith(corpus);
                    //Если это командный режим..
                    if (IS_TEAM_MODE) {
                        //Узнаем кол-во команд, оставшихся на поле
                        ArrayList<String> teamsRemained = field.getTeamsRemained();
                        int teamsRemainedQuant = teamsRemained.size();
                        int missilesRemained = field.getMissiles().size();
                        //Если осталась 1 команда.. и улетели все ракеты..
                        if (teamsRemainedQuant == 1 && missilesRemained == 0) {
                            //определяем победителя
                            String winnerTeam = teamsRemained.get(0);
                            log.info("-----------> The Winner is team >> " + winnerTeam + " << !");
                            //Останавливаем игру
                            startOrPauseGame();
                            gameView.refreshComboboxes(gameStarted);
                        }
                        //Если обычный режим..
                    } else {
                        //Следим за кол-вом тел на поле
                        int corpusesRemain = field.getCorpuses().size();
                        if (corpusesRemain < 2) {
                            int shipsAlive = field.getShips().size();
                            //Если все корабли погибли - победителя нет
                            String message = "No winner.";
                            if (shipsAlive == 1) {
                                Ship winner = field.getShips().get(0);
                                message = winner.getId() + "[" + winner.getHp() + " hp]";
                            }
                            log.info("-----------> The Winner is >> " + message + " << !");
                            //Останавливаем игру
                            //                        timer.cancel();
                            startOrPauseGame();
                            gameView.refreshComboboxes(gameStarted);
                        }
                    }
                }
            };
            timer.schedule(task, 0, ACTION_DELAY);
        }

    }

    public static void stopGame() {
        if (gameStarted) {
            timer.cancel();
            timer.purge();
        }
        gameStarted = false;
    }

    /**
     * Выполняет действие с телом, переданное его CorpusAI
     * @param corpus
     */
    private static void doActionWith(Corpus corpus) {
        boolean actionDone = false;//Переменная, говорящая о завершении хода
        if (corpus == null) {
            return; //Если ходит никто - то ничего не делаем =)
        }
        log.debug("-------> Applying action to: [" + corpus.getId() + "]");
        String corpusId = corpus.getId();

        //Делаем копию игрового поля
        Field fieldCopy = (Field) field.clone();

        //Скрываем координаты невидимых кораблей на поле
        ArrayList<Ship> ships = field.getShips();
        for (int i = 0; i < ships.size(); i++) {
            Ship ship = ships.get(i);
            //Если корабль невидим (и не корабль аишки)
            if (!ship.isVisible() && !ship.getId().equals(corpusId)) {
                ships.get(i).setPosX(-10);
                ships.get(i).setPosY(-10);
            }
        }

        Action action = null;
        CorpusAI corpusAI = corpus.getAi();//получаем CorpusAI корабля
        //Если он непуст - получаем действие
        if (corpusAI != null) {
            if (corpus instanceof Ship) {
                ShipAI shipAI = (ShipAI) corpusAI;
                action = shipAI.getAction(corpusId, field, IS_TEAM_MODE);
            } else {
                action = corpusAI.getAction();
            }
        }
        //Если действие пусто - говорим телу стоять и ничего не делать
        if (action == null || action.getType() == null) {
            action = new Action("move", MOVE_0, MOVE_0);
            action.setAiComment("Null action from AI. Standing By..");
        }
        //На случай, если CorpusAI попытается изменить поле
        field = fieldCopy;//Переключаемся на копию

        String actionType = action.getType().toLowerCase();
        String xMove = "", yMove = "", moveInfo = "";
        int dx = action.getDx();
        int dy = action.getDy();
        int speed = action.getSpeed();
        //Если выбранная скорость перемещения не установлена,
        //то передвигаемм тело с максимальной скоростью
        if (speed == 0) {
            speed = corpus.getMaxSpeed();
        }
        if (speed > corpus.getMaxSpeed()) {
            speed = corpus.getMaxSpeed();
        }

        //Готовим переменную для логгера
        String corpusInfo = corpusId + " [hp: " + corpus.getHp() + "; arm: " + corpus.getArmor() + "] ";

        //Пишем комментарии от CorpusAI..
        String aiComment = action.getAiComment();
        if (aiComment != null && !aiComment.isEmpty()) {
            log.info(corpusInfo + "_AI: " + aiComment);
        }

        //Если это корабль..
        if (corpus instanceof Ship) {
            Ship ship = (Ship) corpus;
            //Регенерируем корабль, если можно
            if (ship.getRegenHpLevel() > 0) {
                //Если его хп - это не максимум его хп при его размере
                if (ship.getHp() < ship.getMaxHp()) {
                    log.info(corpusInfo + "regenerates hp");
                    ship.regenerateHp();
                } else {
                    log.debug(corpusInfo + "Won't regenerate. Hp are at maximum!");
                }
            }
            //Если корабль большего размера, чем может быть (после апгрейда)..
            if (ship.getHp() > ship.getMaxHp()) {
                log.info(corpusInfo + "looses it's hp");
                ship.unregenerateHp();
            }
            //TODO: //Если корабль "горит"
            if (ship.isBurned()) {
                log.info(corpusInfo + "is burned and looses it's hp");
                ship.unregenerateHp(ship.getBurningDamage());
                ship.decreaseBurningDamage();
            }
            //Если корабль парализован - придется пропустить ход
            if (ship.isStunned()) {
                ship.decreaseStunTime();
                field.setNextCorpusToMove(ship);
                log.info(corpusInfo + " is STUNNED. Standing by.");
                return;//выходим
            }
            //Если корабль невидим
            if (!ship.isVisible()) {
                //Уменьшаем время невидимости
                ship.decreaseInvisibleTime();
            }
            //Если апгрейды..
            if (actionType.contains("+") || actionType.contains("-")) {
                //Проверяем, достаточно ли поинтов для апгрейда
                if (ship.getUpgradePoints() < 1) {
                    log.debug(corpusInfo + "Failed!");
                    log.info(corpusInfo + "Failed to upgrade. Not enough uprade points!");
                    //Если достаточно, то делаем соответствующие апгрейды
                } else {
                    if (actionType.equalsIgnoreCase("invision++")) {
                        if (ship.canIncreaseInvisLevel()) {
                            ship.increaseInvisibleLevel();
                            ship.decreaseUpgradePoints();
                            log.info(corpusInfo + "Improves INVISION level");
                        } else {
                            log.info(corpusInfo + "FAILED to apply upgrade. INVISION lvl is at maximum!");
                        }
                    }
                    if (actionType.equalsIgnoreCase("evasion++")) {
                        if (ship.canIncreaseEvasion()) {
                            ship.increaseEvasionLevel();
                            ship.decreaseUpgradePoints();
                            log.info(corpusInfo + "Improves EVASION level");
                        } else {
                            log.info(corpusInfo + "FAILED to apply upgrade. EVASION lvl is at maximum!");
                        }
                    }
                    if (actionType.equalsIgnoreCase("critical++")) {
                        if (ship.canIncreaseCriticalAttack()) {
                            ship.increaseCriticalAttackLevel();
                            ship.decreaseUpgradePoints();
                            log.info(corpusInfo + "Improves MISSILES (CRITICAL)");
                        } else {
                            log.info(corpusInfo + "FAILED to apply upgrade. CRITICAL attack lvl is at maximum!");
                        }
                    }
                    if (actionType.equalsIgnoreCase("regen++")) {
                        if (ship.canIncreaseRegen()) {
                            ship.increaseRegenHpLevel();
                            ship.decreaseUpgradePoints();
                            log.info(corpusInfo + "Improves REGENERATION.");
                        }
                    }
                    if (actionType.equalsIgnoreCase("armor++")) {
                        if (ship.canIncreaseArmor()) {
                            ship.increaseArmor();
                            ship.decreaseUpgradePoints();
                            log.info(corpusInfo + "Improves ARMOR");
                        } else {
                            log.info(corpusInfo + "FAILED to apply upgrade. Has maximum armor already!");
                        }
                    }
                    if (actionType.equalsIgnoreCase("missile++")) {
//                        if (ship.getUpgradePoints() > 1){
                        ship.increaseMissileQuantity();
                        //Случайным образом даем одну из ракет
                        int missileChoice = getRanInt(2);
                        switch (missileChoice) {
                            default:
                                ship.increaseHeavyMissileQuantity();
                                break;
                            case 1:
                                ship.increaseStunningMissileQuantity();
                                break;
                            case 2:
                                ship.increaseBurningMissileQuantity();
                                break;
                        }
                        ship.decreaseUpgradePoints();
                        log.info(corpusInfo + "gets additional MISSILES");
//                        } else {
//                            log.info(corpusInfo + "FAILED to apply upgrade. Not enough APP for missile++!");
//                        }

                    }
                    if (actionType.equalsIgnoreCase("width++")) {
                        if (ship.canIncreaseWidth()) {
                            ship.increaseWidth();
                            ship.increaseHp();
                            ship.decreaseMaxSpeed();
                            ship.decreaseUpgradePoints();
                            log.info(corpusInfo + "increases WIDTH");
                        } else {
                            log.info(corpusInfo + "FAILED to increase width. Too big already!");
                        }
                    }
                    if (actionType.equalsIgnoreCase("width--")) {
                        if (ship.canDecreaseWidth()) {
                            ship.decreaseWidth();
                            ship.decreaseHp();
                            ship.increaseMaxSpeed();
                            ship.decreaseUpgradePoints();
                            log.info(corpusInfo + "Decreases width");
                        } else {
                            log.info(corpusInfo + "Failed to decrease width. Too small already!");
                        }
                    }
                    if (actionType.equalsIgnoreCase("height++")) {
                        if (ship.canIncreaseHeight()) {
                            ship.increaseHeight();
                            ship.increaseHp();
                            ship.decreaseMaxSpeed();
                            ship.decreaseUpgradePoints();
                            log.info(corpusInfo + "Increases height");
                        } else {
                            log.info(corpusInfo + "Failed to increase height. Too big already!");
                        }
                    }
                    if (actionType.equalsIgnoreCase("height--")) {
                        if (ship.canDecreaseHeight()) {
                            ship.decreaseHeight();
                            ship.decreaseHp();
                            ship.increaseMaxSpeed();
                            ship.decreaseUpgradePoints();
                            log.info(corpusInfo + "Decreases height");
                        } else {
                            log.info(corpusInfo + "Failed to decrease height. Too small already!");
                        }
                    }

                }
                field.estimateDamage(corpus);
                field.setNextCorpusToMove(corpus);
                field.removeDeadCorpuses();
                gameView.refresh();
                actionDone = true;
                //Если ракетная атака..
            } else if (actionType.contains("missile")) {
                //Проверяем ли есть ли у корабля в наличии ракеты
                if (ship.getMissileQuantity() < 1 && ship.getHeavyMissileQuantity() < 1) {
                    log.info(corpusInfo + "Not enough missiles. Standing by.");
                } else {
                    //Проверяем чтобы было задано направление ракеты хотябы по одной из осей
                    if (dx == 0 && dy == 0) {
                        log.info(corpusInfo + "Incorrect missile attack (dx=dy=0). Standing by.");
                        //Вычисляем следущее тело для хода
                       /* field.setNextCorpusToMove(ship);
                        actionDone = true;*/
                    } else {
                        //Увеличиваем индекс выпущенных ракет
                        ship.increaseMissileIdx();
                        //Создаем ракету..
                        Missile missile = new Missile(dx, dy);
                        //Если это разрушительная ракета..
                        if (actionType.equalsIgnoreCase("heavy_missile")) {
                            //Если закончились разрушительные ракеты..
                            if (ship.getHeavyMissileQuantity() < 1) {
                                actionType = "missile";
                            } else {
                                missile = new HeavyMissile(dx, dy);
                                //Уменьшаем кол-во разрушительных ракет на борту корабля
                                ship.decreaseHeavyMissileQuantity();
                            }
                        }
                        //Если это парализующая ракета..
                        if (actionType.equalsIgnoreCase("stunning_missile")) {
                            //Если закончились парализующие ракеты..
                            if (ship.getStunningMissileQuantity() < 1) {
                                actionType = "missile";
                            } else {
                                missile = new StunningMissile(dx, dy);
                                //Уменьшаем кол-во парализующих ракет на борту корабля
                                ship.decreaseStunningMissileQuantity();
                            }
                        }
                        //Если это зажигательнаая ракета..
                        if (actionType.equalsIgnoreCase("burning_missile")) {
                            //Если закончились зажигательные ракеты..
                            if (ship.getBurningMissileQuantity() < 1) {
                                actionType = "missile";
                            } else {
                                missile = new BurningMissile(dx, dy);
                                //Уменьшаем кол-во парализующих ракет на борту корабля
                                ship.decreaseBurningMissileQuantity();
                            }
                        }
                        if (actionType.equalsIgnoreCase("missile")) {
                            //Если корабль не может стрелять критическими ракетами..
                            if (ship.getCriticalAttackLevel() > 0) {
                                missile = new CriticalMissile(dx, dy, ship.getCriticalAttackLevel());
                                CriticalMissile criticalMissile = (CriticalMissile) missile;
                                //Если ракета несет критический урон - сообщаем об этом
                                if (criticalMissile.hasCriticalDamage()) {
                                    actionType = "critical_missile";
                                }
                            }

                            //Уменьшаем кол-во ракет на борту корабля
                            ship.decreaseMissileQuantity();
                        }

                        log.info(corpusInfo + "Makes a " + actionType.toUpperCase() + " SHOT");

                        //Помещаем ракету в центр корабля
                        missile.setPosX(ship.getMiddleX());
                        missile.setPosY(ship.getMiddleY());
                        //Устанавливаем id ракете
                        corpusId = corpusId + ship.getMissileIdx();
                        missile.setId(corpusId);
                        //Устанавливаем цвет ракете
                        missile.setColor(ship.getColor());
                        //Добавляем ее в список тел
                        field.getCorpuses().add(missile);
                    }
                }
                //Вычисляем следущее тело для хода
                field.setNextCorpusToMove(ship);
                actionDone = true;
                //Если корабль прячется (переходит в невидимый режим)
            } else if (actionType.equalsIgnoreCase("hide")) {
                if (ship.canHide()) {
                    ship.increaseInvisibleTime();
                    ship.decreaseUpgradePoints();
                    log.info(corpusInfo + "increases INVISIBLE TIME");
                } else {
                    log.info(corpusInfo + "FAILED to increase INVIS TIME. Can't hide now!");
                }
                //Вычисляем следущее тело для хода
                field.setNextCorpusToMove(ship);
                actionDone = true;
            }
        }

        //Если перемещение..
        if (actionType.equalsIgnoreCase("move")) {
            log.debug("corpus " + corpusId + " is moving");
            //Готовим данные для логгера хода
            if (dx == -1) {
                xMove = "left";
            }
            if (dx == 1) {
                xMove = "right";
            }
            if (dy == -1) {
                yMove = "up";
            }
            if (dy == 1) {
                yMove = "down";
            }
            if (xMove.isEmpty() && yMove.isEmpty()) {
                moveInfo = "STANDS BY";
            } else {
                moveInfo = "MOVES: " + xMove + " " + yMove + " | speed: " + speed;
            }
            //Логируем событие
            log.info(corpusInfo + moveInfo);
            //Перемещаем тело
            for (int i = 0; i < speed; i++) {
                corpus.move(dx, dy);
                //Обновляем модель таблицы и вьюшку
                gameView.refresh();
                //Подсчитываем урон после перемещения корпуса на 1 клетку
                field.estimateDamage(corpus);
                //Вычисляем следущее тело для хода
                field.setNextCorpusToMove(corpus);
                //Убираем погибшие тела
                field.removeDeadCorpuses();
                //Обновляем модель таблицы и вьюшку
                gameView.refresh();
                //Если корабль погиб в процессе перемещения - ему уже никогда не ходить =)
                if (field.getCorpusById(corpusId) == null) {
                    break;
                }
                try {
                    Thread.sleep(MOVEMENT_DELAY);
                } catch (InterruptedException ex) {
                    Logger.getLogger(GameManager.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            actionDone = true;
        }

        //Если действие неизвестно - вычисляем следующий корпус
        if (!actionDone) {
            log.info(corpusInfo + "Unknown action. Standing by..");
            field.setNextCorpusToMove(corpus);
        }

    }

    public static void main(String[] args) throws InterruptedException {
        Config.load();
        field = new Field();
        //System LookAndFeel Micro Hack ;)
        try {
            //if (System.getProperty("os.name").equals("Linux")) {
                //UIManager.setLookAndFeel("com.sun.java.swing.plaf.gtk.GTKLookAndFeel");
                UIManager.setLookAndFeel("com.jtattoo.plaf.hifi.HiFiLookAndFeel");
//              JDialog.setDefaultLookAndFeelDecorated(true);
//              JFrame.setDefaultLookAndFeelDecorated(true);
                //UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            //} /*else {
            //}*/
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        Runnable gameRunnable = new Runnable() {
            public void run() {
                gameView = new GameView();
                TrayManager.trayIt(gameView);
                log = ShiptCraftLogger.getLogger(gameView.getTextArea());
                SplashScreen.setPositionPercent(100);
            }
        };
        Runnable splashRunnable = new Runnable() {
            public void run() {
                // initialize the splash screen
                SplashScreen screen = new SplashScreen("logo_no_bar.png");
                screen.setVisible(false);
                gameView.setVisible(true);
            }
        };

        new Thread(splashRunnable).start();
        new Thread(gameRunnable).start();
        
        //Showing view
        /*EventQueue.invokeLater(new Runnable() {
            public void run() {
                gameView = new GameView();
                log = ShiptCraftLogger.getLogger(gameView.getTextArea());
            }
        });*/
    }
}

