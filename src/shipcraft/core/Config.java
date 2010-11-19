/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package shipcraft.core;

import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;

/**
 * Загрузка и получение настроек программы.
 * @author stream, spr1ng
 * @version $Id: Config.java 76 2010-06-23 02:05:43Z spr1ng $
 */
public class Config {
//GAME     /////////////////////////////////////////////////////////////////////
    /** Название игры */
    public static String GAME_NAME;
    /** Режим отладки */
    public static boolean IS_DEBUG_MODE;
    /** Командный режим */
    public static boolean IS_TEAM_MODE;
    /** Режим автоматической перезагрузки интеллектов из скриптов при каждом ходе корабля */
    public static boolean IS_LIVE_SCRIPT;
    /** Пауза при выполнении перемещения тела на 1 клетку поля */
    public static long MOVEMENT_DELAY;
    /** Пауза между выполнением действий кораблей */
    public static long ACTION_DELAY;
//VIEW    /////////////////////////////////////////////////////////////////////
    /** Длина фрейма */
    public static int FRAME_WIDTH;//1190;
    /** Длина фрейма */
    public static int FRAME_HEIGHT;
    /** Длина восточной панели */
    public static int EAST_PANEL_WIDTH;
    /** Длина лэйблов */
    public static int LABEL_WIDTH;
//FIELD    /////////////////////////////////////////////////////////////////////
    /** Длина поля */
    public static int FIELD_WIDTH;
    /** Высота поля */
    public static int FIELD_HEIGHT;
    /** Кол-во кораблей на поле */
    public static int SHIP_QUANTITY;
    /** Минимальный интервал между кораблями */
    public static int SHIP_INTERVAL;
    /** Ширина клетки таблицы */
    public static int CELL_WIDTH;
    /** Высота клетки таблицы */
    public static int CELL_HEIGHT;
//SHIP     /////////////////////////////////////////////////////////////////////
    /** Минимально допустимая длина корабля */
    public static int SHIP_WIDTH_MIN;
    /** Максимально допустимая длина корабля */
    public static int SHIP_WIDTH_MAX;
    /** Минимально допустимая высота корабля */
    public static int SHIP_HEIGTH_MIN;
    /** Максимально допустимая высота корабля */
    public static int SHIP_HEIGTH_MAX;
    /** Кол-во фотонных ракет на борту корабля */
    public static int MISSILE_QUANTITY;
    /** Кол-во ракет высокой разрушительной способности на борту корабля */
    public static int HEAVY_MISSILE_QUANTITY;
    /** Кол-во ракет высокой разрушительной способности на борту корабля */
    public static int STUNNING_MISSILE_QUANTITY;
    /** Кол-во доступных апгрейдов для корабля в начале игры */
    public static int UPGRADABLE_POINTS;
    /** Броня корабля по умолчанию */
    public static int ARMOR;
    /** Максимально возможная броня корабля */
    public static int ARMOR_MAX;
    /** Кол-во hp, восстанавливаемых кораблем в начале хода */
    public static int REGEN_HP_LEVEL;
    /** Максимальное кол-во hp, восстанавливаемых кораблем в начале хода */
    public static int REGEN_HP_LEVEL_MAX;
    /** Уровень критических атак корабля */
    public static int CRITICAL_ATTACK_LEVEL;
    /** Масксимальный уровень критических атак корабля */
    public static int CRITICAL_ATTACK_LEVEL_MAX; // 1 из 3-х
    /** Уровень способности корабля уклоняться от атак */
    public static int EVASION_LEVEL;
    /** Максимальный ровень способности корабля уклоняться от атак  */
    public static int EVASION_LEVEL_MAX;         // 1 из 3-х
    /** Время парализованности корабля при попадании в него парализующей ракеты */
    public static int STUN_TIME;
    /** Время максимальной парализованности корабля при попадании в него парализующей ракеты */
    public static int STUN_TIME_MAX;
    /** Максимальный уровень способности корабля быть невидимым */
    public static int INVISIBLE_LEVEL_MAX;
//AI    ////////////////////////////////////////////////////////////////////////
    /** Путь к AI */
    public static String AI_PATH;
    /** Название главного класса подгружаемого AI */
    public static String SCRIPT_MAIN;
    private static Properties prop;

    private static void init() {
    //GAME     /////////////////////////////////////////////////////////////////
        /** Название игры */
        GAME_NAME = get("GAME_NAME") != null ? getString("GAME_NAME")                           : "Ship Craft v1.0" ;
        /** Режим отладки */
        IS_DEBUG_MODE = get("IS_DEBUG_MODE") != null ? getBool("IS_DEBUG_MODE")                 : false;
        /** Командный режим */
        IS_TEAM_MODE = get("IS_TEAM_MODE") != null ? getBool("IS_TEAM_MODE")                    : true;
        /** Режим автоматической перезагрузки интеллектов из скриптов при каждом ходе корабля */
        IS_LIVE_SCRIPT = get("IS_LIVE_SCRIPT") != null ? getBool("IS_LIVE_SCRIPT")              : false;
        /** Пауза при выполнении перемещения тела на 1 клетку поля */
        MOVEMENT_DELAY = get("MOVEMENT_DELAY") != null ? getInt("MOVEMENT_DELAY")               : 30;
        /** Пауза между выполнением действий кораблей */
        ACTION_DELAY = get("ACTION_DELAY") != null ? getInt("ACTION_DELAY")                     : 300;
    //VIEW     /////////////////////////////////////////////////////////////////
        /** Длина фрейма */
        FRAME_WIDTH = get("FRAME_WIDTH") != null ? getInt("FRAME_WIDTH")                        : 1280;//1190;
        /** Длина фрейма */
        FRAME_HEIGHT = get("FRAME_HEIGHT") != null ? getInt("FRAME_HEIGHT")                     : 668;
        /** Длина восточной панели */
        EAST_PANEL_WIDTH = get("EAST_PANEL_WIDTH") != null ? getInt("EAST_PANEL_WIDTH")         : 676;//584;
        /** Длина лэйблов */
        LABEL_WIDTH = get("LABEL_WIDTH") != null ? getInt("LABEL_WIDTH")                        : 270;//172;
    //FIELD    /////////////////////////////////////////////////////////////////
        /** Длина поля */
        FIELD_WIDTH = get("FIELD_WIDTH") != null ? getInt("FIELD_WIDTH")                        : 60;
        /** Высота поля */
        FIELD_HEIGHT = get("FIELD_HEIGHT") != null ? getInt("FIELD_HEIGHT")                     : 60;
        /** Кол-во кораблей на поле */
        SHIP_QUANTITY = get("SHIP_QUANTITY") == null ? 10 : getInt("SHIP_QUANTITY");
        /** Минимальный интервал между кораблями */
        SHIP_INTERVAL = get("SHIP_INTERVAL") != null ? getInt("SHIP_INTERVAL")                  : 8;
        /** Ширина клетки таблицы */
        CELL_WIDTH = get("CELL_WIDTH") != null ? getInt("CELL_WIDTH")                           : 10;
        /** Высота клетки таблицы */
        CELL_HEIGHT = get("CELL_HEIGHT") != null ? getInt("CELL_HEIGHT")                        : 10;
    //SHIP     /////////////////////////////////////////////////////////////////
        /** Минимально допустимая длина корабля */
        SHIP_WIDTH_MIN = get("SHIP_WIDTH_MIN") != null ? getInt("SHIP_WIDTH_MIN")               : 3;
        /** Максимально допустимая длина корабля */
        SHIP_WIDTH_MAX = get("SHIP_WIDTH_MAX") != null ? getInt("SHIP_WIDTH_MAX")               : 9;
        /** Минимально допустимая высота корабля */
        SHIP_HEIGTH_MIN = get("SHIP_HEIGTH_MIN") != null ? getInt("SHIP_HEIGTH_MIN")            : 3;
        /** Максимально допустимая высота корабля */
        SHIP_HEIGTH_MAX = get("SHIP_HEIGTH_MAX") != null ? getInt("SHIP_HEIGTH_MAX")            : 9;
        /** Кол-во фотонных ракет на борту корабля */
        MISSILE_QUANTITY = get("MISSILE_QUANTITY") != null ? getInt("MISSILE_QUANTITY")         : 15;
        /** Кол-во ракет высокой разрушительной способности на борту корабля */
        HEAVY_MISSILE_QUANTITY = 
                get("HEAVY_MISSILE_QUANTITY") != null ? getInt("HEAVY_MISSILE_QUANTITY")        : 0;
        /** Кол-во ракет высокой разрушительной способности на борту корабля */
        STUNNING_MISSILE_QUANTITY = 
                get("STUNNING_MISSILE_QUANTITY") != null ? getInt("STUNNING_MISSILE_QUANTITY")  : 0;
        /** Кол-во доступных апгрейдов для корабля в начале игры */
        UPGRADABLE_POINTS = get("UPGRADABLE_POINTS") != null ? getInt("UPGRADABLE_POINTS")      : 2;
        /** Броня корабля по умолчанию */
        ARMOR = get("ARMOR") != null ? getInt("ARMOR")                                          : 0;
        /** Максимально возможная броня корабля */
        ARMOR_MAX = get("ARMOR_MAX") != null ? getInt("ARMOR_MAX")                              : 7;
        /** Кол-во hp, восстанавливаемых кораблем в начале хода */
        REGEN_HP_LEVEL = get("REGEN_HP_LEVEL") != null ? getInt("REGEN_HP_LEVEL")               : 1;
        /** Максимальное кол-во hp, восстанавливаемых кораблем в начале хода */
        REGEN_HP_LEVEL_MAX = get("REGEN_HP_LEVEL_MAX") != null ? getInt("REGEN_HP_LEVEL_MAX")   : 7;
        /** Уровень критических атак корабля */
        CRITICAL_ATTACK_LEVEL = 
                get("CRITICAL_ATTACK_LEVEL") != null ? getInt("CRITICAL_ATTACK_LEVEL")          : 0;
        /** Масксимальный уровень критических атак корабля */
        CRITICAL_ATTACK_LEVEL_MAX = 
                get("CRITICAL_ATTACK_LEVEL_MAX") != null ? getInt("CRITICAL_ATTACK_LEVEL_MAX")  : 3; // 1 из 3-х
        /** Уровень способности корабля уклоняться от атак */
        EVASION_LEVEL = get("EVASION_LEVEL") != null ? getInt("EVASION_LEVEL")                  : 0;
        /** Максимальный ровень способности корабля уклоняться от атак  */
        EVASION_LEVEL_MAX = get("EVASION_LEVEL_MAX") != null ? getInt("EVASION_LEVEL_MAX")      : 3; // 1 из 3-х
        /** Время парализованности корабля при попадании в него парализующей ракеты */
        STUN_TIME = get("STUN_TIME") != null ? getInt("STUN_TIME")                              : 2;
        /** Время максимальной парализованности корабля при попадании в него парализующей ракеты */
        STUN_TIME_MAX = get("STUN_TIME_MAX") != null ? getInt("STUN_TIME_MAX")                  : 6;
        /** Максимальный уровень способности корабля быть невидимым */
        INVISIBLE_LEVEL_MAX = get("INVISIBLE_LEVEL_MAX") != null ? getInt("INVISIBLE_LEVEL_MAX"): 5;
    //AI    ////////////////////////////////////////////////////////////////////
        /** Путь к AI */
        AI_PATH = get("AI_PATH") != null ? getString("AI_PATH")                                 : "AI";
        /** Название главного класса подгружаемого AI */
        SCRIPT_MAIN = get("SCRIPT_MAIN") != null ? getString("SCRIPT_MAIN")                     : "Main";
    }

    public static void load() {
        load("settings.conf");
    }

    public static void load(String fName) {
        prop = new Properties();
        try {
            prop.load(new FileReader(fName));
            init();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    private static Object get(String key) {
        return prop.get(key);
    }

    private static boolean getBool(String key) {
        return Boolean.parseBoolean(prop.get(key).toString());
    }

    private static int getInt(String key) {
        return Integer.parseInt(prop.get(key).toString());
    }

    private static String getString(String key) {
        return prop.get(key).toString();
    }
}
