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
    private static SmartProperties prop;

    private static void init() {
    //GAME     /////////////////////////////////////////////////////////////////
        /** Название игры */
        GAME_NAME = prop.getProperty("GAME_NAME", "Ship Craft v1.0");
        /** Режим отладки */
        IS_DEBUG_MODE = prop.getProperty("IS_DEBUG_MODE", false);
        /** Командный режим */
        IS_TEAM_MODE = prop.getProperty("IS_TEAM_MODE", true);
        /** Режим автоматической перезагрузки интеллектов из скриптов при каждом ходе корабля */
        IS_LIVE_SCRIPT = prop.getProperty("IS_LIVE_SCRIPT", false);
        /** Пауза при выполнении перемещения тела на 1 клетку поля */
        MOVEMENT_DELAY = prop.getProperty("MOVEMENT_DELAY", 30);
        /** Пауза между выполнением действий кораблей */
        ACTION_DELAY = prop.getProperty("ACTION_DELAY", 300);
    //VIEW     /////////////////////////////////////////////////////////////////
        /** Длина фрейма */
        FRAME_WIDTH = prop.getProperty("FRAME_WIDTH", 1280);//1190
        /** Длина фрейма */
        FRAME_HEIGHT = prop.getProperty("FRAME_HEIGHT", 668);
        /** Длина восточной панели */
        EAST_PANEL_WIDTH = prop.getProperty("EAST_PANEL_WIDTH", 676);//584;
        /** Длина лэйблов */
        LABEL_WIDTH = prop.getProperty("LABEL_WIDTH", 270);//172;
    //FIELD    /////////////////////////////////////////////////////////////////
        /** Длина поля */
        FIELD_WIDTH = prop.getProperty("FIELD_WIDTH", 60);
        /** Высота поля */
        FIELD_HEIGHT = prop.getProperty("FIELD_HEIGHT", 60);
        /** Кол-во кораблей на поле */
        SHIP_QUANTITY = prop.getProperty("SHIP_QUANTITY", 10);
        /** Минимальный интервал между кораблями */
        SHIP_INTERVAL = prop.getProperty("SHIP_INTERVAL", 8);
        /** Ширина клетки таблицы */
        CELL_WIDTH = prop.getProperty("CELL_WIDTH", 10);
        /** Высота клетки таблицы */
        CELL_HEIGHT = prop.getProperty("CELL_HEIGHT", 10);
    //SHIP     /////////////////////////////////////////////////////////////////
        /** Минимально допустимая длина корабля */
        SHIP_WIDTH_MIN = prop.getProperty("SHIP_WIDTH_MIN", 3);
        /** Максимально допустимая длина корабля */
        SHIP_WIDTH_MAX = prop.getProperty("SHIP_WIDTH_MAX", 9);
        /** Минимально допустимая высота корабля */
        SHIP_HEIGTH_MIN = prop.getProperty("SHIP_HEIGTH_MIN", 3);
        /** Максимально допустимая высота корабля */
        SHIP_HEIGTH_MAX = prop.getProperty("SHIP_HEIGTH_MAX", 9);
        /** Кол-во фотонных ракет на борту корабля */
        MISSILE_QUANTITY = prop.getProperty("MISSILE_QUANTITY", 15);
        /** Кол-во ракет высокой разрушительной способности на борту корабля */
        HEAVY_MISSILE_QUANTITY = prop.getProperty("HEAVY_MISSILE_QUANTITY", 0);
        /** Кол-во ракет высокой разрушительной способности на борту корабля */
        STUNNING_MISSILE_QUANTITY = prop.getProperty("STUNNING_MISSILE_QUANTITY", 0);
        /** Кол-во доступных апгрейдов для корабля в начале игры */
        UPGRADABLE_POINTS = prop.getProperty("UPGRADABLE_POINTS", 2);
        /** Броня корабля по умолчанию */
        ARMOR = prop.getProperty("ARMOR", 0);
        /** Максимально возможная броня корабля */
        ARMOR_MAX = prop.getProperty("ARMOR_MAX", 7);
        /** Кол-во hp, восстанавливаемых кораблем в начале хода */
        REGEN_HP_LEVEL = prop.getProperty("REGEN_HP_LEVEL", 1);
        /** Максимальное кол-во hp, восстанавливаемых кораблем в начале хода */
        REGEN_HP_LEVEL_MAX = prop.getProperty("REGEN_HP_LEVEL_MAX", 7);
        /** Уровень критических атак корабля */
        CRITICAL_ATTACK_LEVEL = prop.getProperty("CRITICAL_ATTACK_LEVEL", 0);
        /** Масксимальный уровень критических атак корабля */
        CRITICAL_ATTACK_LEVEL_MAX = prop.getProperty("CRITICAL_ATTACK_LEVEL_MAX", 3); // 1 из 3-х
        /** Уровень способности корабля уклоняться от атак */
        EVASION_LEVEL = prop.getProperty("EVASION_LEVEL", 0);
        /** Максимальный ровень способности корабля уклоняться от атак  */
        EVASION_LEVEL_MAX = prop.getProperty("EVASION_LEVEL_MAX", 3); // 1 из 3-х
        /** Время парализованности корабля при попадании в него парализующей ракеты */
        STUN_TIME = prop.getProperty("STUN_TIME", 2);
        /** Время максимальной парализованности корабля при попадании в него парализующей ракеты */
        STUN_TIME_MAX = prop.getProperty("STUN_TIME_MAX", 6);
        /** Максимальный уровень способности корабля быть невидимым */
        INVISIBLE_LEVEL_MAX = prop.getProperty("INVISIBLE_LEVEL_MAX", 5);
    //AI    ////////////////////////////////////////////////////////////////////
        /** Путь к AI */
        AI_PATH = prop.getProperty("AI_PATH", "AI");
        /** Название главного класса подгружаемого AI */
        SCRIPT_MAIN = prop.getProperty("SCRIPT_MAIN", "Main");
    }

    public static void load() {
        load("settings.conf");
    }

    public static void load(String fName) {
        prop = new SmartProperties();
        try {
            prop.load(new FileReader(fName));
            init();
        } catch (IOException ex) {
            ex.printStackTrace(System.err);
        }
    }

    static class SmartProperties extends Properties {
        public <T> T getProperty(String key, T defaultValue) {
            String value = getProperty(key);
            if (key == null || value == null) return defaultValue;
            switch (Type.getByClass(defaultValue.getClass())) {
                case BOOLEAN: return (T) Boolean.valueOf(value);
                case INTEGER:
                    try {
                        return (T) Integer.valueOf(value);
                    } catch (Exception e) {
                        System.err.println("Warning: unparsable property value for: " + key);
                        e.printStackTrace(System.err);
                        //Passes to the default value
                    }
                default: return defaultValue;
            }
        }
    }

    enum Type {
        BOOLEAN, INTEGER, STRING;

        public static Type getByClass(Class clazz){
            if (Boolean.class.isAssignableFrom(clazz)) return BOOLEAN;
            if (Integer.class.isAssignableFrom(clazz)) return INTEGER;
            return STRING;
        }
    }

}

