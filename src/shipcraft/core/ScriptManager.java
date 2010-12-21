/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package shipcraft.core;

import java.io.FileReader;
import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.Vector;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import static shipcraft.core.Config.*;
import static shipcraft.utils.Utils.*;
import shipcraft.intrfc.ShipAI;
import shipcraft.view.SplashScreen;

/**
 * @version $Id: ScriptManager.java 76 2010-06-23 02:05:43Z spr1ng $
 * @author stream, spr1ng
 */
public class ScriptManager {

    private ScriptManager() {
    }

    /**
     * Регистрируем открытый API для всех доступных скриптовых движков.
     * @param engine
     */
    @SuppressWarnings("CallToThreadDumpStack")
    public static void registerAPI(ScriptEngine engine) {
        
            //            engine.put("ShipMoveLeft", new Action("move", MOVE_LEFT, MOVE_0));
            //            engine.put("ShipMoveLeftUp", new Action("move", MOVE_LEFT, MOVE_UP));
            //            engine.put("ShipMoveLeftDown", new Action("move", MOVE_LEFT, MOVE_DOWN));
            //            engine.put("ShipMoveUp", new Action("move", MOVE_0, MOVE_UP));
            //            engine.put("ShipMoveDown", new Action("move", MOVE_0, MOVE_DOWN));
            //            engine.put("ShipMoveRight", new Action("move", MOVE_RIGHT, MOVE_0));
            //            engine.put("ShipMoveRightUp", new Action("move", MOVE_RIGHT, MOVE_UP));
            //            engine.put("ShipMoveRightDown", new Action("move", MOVE_RIGHT, MOVE_DOWN));
            //            engine.put("ShipStandBy", new Action("move", MOVE_0, MOVE_0));
            //
            //            engine.put("MissileMoveLeft", new Action("missile", MOVE_LEFT, MOVE_0));
            //            engine.put("MissileMoveLeftUp", new Action("missile", MOVE_LEFT, MOVE_UP));
            //            engine.put("MissileMoveLeftDown", new Action("missile", MOVE_LEFT, MOVE_DOWN));
            //            engine.put("MissilepMoveUp", new Action("missile", MOVE_0, MOVE_UP));
            //            engine.put("MissileMoveDown", new Action("missile", MOVE_0, MOVE_DOWN));
            //            engine.put("MissileMoveRight", new Action("missile", MOVE_RIGHT, MOVE_0));
            //            engine.put("MissileMoveRightUp", new Action("missile", MOVE_RIGHT, MOVE_UP));
            //            engine.put("MissileMoveRightDown", new Action("missile", MOVE_RIGHT, MOVE_DOWN));
            //            engine.put("MissileStandBy", new Action("missile", MOVE_0, MOVE_0));
            //
            //            engine.put("IncreaseArmor", new Action("armor++"));
            //            engine.put("IncreaseRegen", new Action("regen++"));
            //
            //            engine.put("IncreaseWidth", new Action("width++"));
            //            engine.put("DecreaseWidth", new Action("width--"));
            //
            //            engine.put("IncreaseHeight", new Action("height++"));
            //            engine.put("DecreaseHeight", new Action("height--"));
            //            engine.put("IncreaseMissile", new Action("missile++"));
            //            engine.put("IncreaseMissile", new Action("missile++"));
    }

    /**
     * Возвращает путь к главному классу указанного интеллекта
     * @param aiFolderPath
     * @return
     */
    public static String getAiMainName(String aiFolderPath){
        FilenameFilter filter = new FilenameFilter() {
            public boolean accept(File dir, String name) {
                name = name.replaceFirst("[.].*", "");
                return name.equalsIgnoreCase(SCRIPT_MAIN);
            }
        };
        String[] fileNameList = new File(aiFolderPath).list(filter);
        
        return  fileNameList[0];
    }

    public static String getAiMainPath(String aiName){
        String aiFolderPath = getAiFolderPath(aiName);
        String aiMainName = getAiMainName(aiFolderPath);
        String aiMainPath = aiFolderPath + "/" + aiMainName;
        return aiMainPath;
    }

    /**
     * Возвращает путь к папке указанного AI
     * @param aiName
     * @return
     */
    public static String getAiFolderPath(String aiName){
        return AI_PATH + "/" + aiName;
    }

    /**
     *
     * @param aiName
     * @deprecated пока решили отказаться от md5 проверок =)
     * @return
     */
    public static String getAiMD5(String aiName){
        String aiMainPath = getAiMainPath(aiName);
        String fileContext = getFileContext(aiMainPath);
        String md5 = getMD5digest(fileContext);
        return md5;
    }

    /**
     * Возвращает объект, реализующий интерфейс ShipAI
     * @param aiName путь к папке с CorpusAI
     * @return
     * @throws java.lang.Exception
     */
    public static ShipAI getValidAi(String aiName) {
        FileReader fReader = null;
        try {
            String aiFolderPath = getAiFolderPath(aiName);
            String aiMainName = getAiMainName(aiFolderPath);
            String extension = aiMainName.replaceFirst(".*[.]", "");
            if (extension.equalsIgnoreCase("lua")) extension = ".lua";//PENDING 
            ScriptEngineManager factory = new ScriptEngineManager();
            ScriptEngine engine = factory.getEngineByExtension(extension);
            registerAPI(engine);
            fReader = new FileReader(aiFolderPath + "/" + aiMainName);
            System.out.println("Engine " + engine);
            ShipAI ai = (ShipAI) engine.eval(fReader);
            if (ai != null) System.out.println("--> " + aiName + " [loaded]");
            //Invocable invocable = (Invocable) engine;
            //ShipAI ai = invocable.getInterface(ShipAI.class);
            //ai.getAction(null, null, false);//new shipcraft.model.Field());
            return ai;
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        } catch (Error er){
            er.printStackTrace();
            return null;
        } finally {
            try {
                if (fReader != null) {
                    fReader.close();
                }
            } catch (IOException ex) {
//                Logger.getLogger(ScriptManager.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    private static String[] getFolders(String path) {
        File folders = new File(path);
        if (!folders.exists()) {
            log.warning("Path to AI folder is not set");
            return null;
        }
        return folders.list();
    }

    public static Vector<String> getAiNamesList() {
        Vector<String> aiNameList = new Vector<String>();
        String aiFolders[] = getFolders(AI_PATH);
        int maxProgr = 50;//Кол-во процентов (на прогрес баре), отводимых под загрузку скриптовых АИ
        int length = aiFolders.length != 0 ? aiFolders.length : 0;
        int dPos = maxProgr / length;//прирощение прогрес бара
        if (dPos == 0) dPos = 1;
        if (aiFolders != null) {
            for (String folderPath : aiFolders) {
                //Если найден корректный SCRIPT_MAIN класс
                ShipAI aiName = getValidAi(folderPath);
                if (aiName != null) {
                    aiNameList.add(folderPath);
                }
                SplashScreen.increasePositionPercent(dPos);
            }
        }
        return aiNameList;
    }

    /*  public static void main(String args[]) throws Exception{

    System.out.println(getAiNamesList());
    System.out.println(getValidAi("GROOVY_AI"));
    }*/
}
