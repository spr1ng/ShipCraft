/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package shipcraft.utils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Random;
import java.util.Scanner;

/**
 * @version $Id: Utils.java 76 2010-06-23 02:05:43Z spr1ng $
 * @author spr1ng
 */
public class Utils {
    //public static Logger log = Logger.getAnonymousLogger();
    public static ShiptCraftLogger log = ShiptCraftLogger.getLogger();

    /**
     * Возвращает случайное от min (включительно) до max (включительно)
     * @param min натуральное число (или 0)
     * @param max натуральное число (или 0)
     * @return
     */
    public static int getRanInt(int min, int max){
        return min + new Random().nextInt(max-min+1);
    }
    
    /**
     * Возвращает случайное от 0 до max (включительно)
     * @param max натуральное число (или 0)
     * @return
     */
    public static int getRanInt(int max){
        return getRanInt(0, max);
    }

    /**
     * Дописывает переданную строку к указанному файлу
     * @param pathName путь к файлу
     * @param line строка с данными
     */
    public static void appendToFile(String pathName, String line) throws FileNotFoundException{
        PrintWriter out = new PrintWriter(new FileOutputStream(pathName, true));
        out.println(line);
        out.close();
    }
    
    /**
     * Перезаписывает указанный файл переданной строкой
     * @param pathName путь к файлу
     * @param line строка с данными
     */
    public static void writeToFile(String pathName, String line) throws FileNotFoundException{
        PrintWriter out = new PrintWriter(new FileOutputStream(pathName, false));
        out.println(line);
        out.close();
    }
    
    /**
     * Генерирует MD5 дайджест для файла
     * @deprecated пока решили отказаться от md5 проверок
     * @param folderPath
     */
    public static void generateFileMD5(String filePath){
        String context = getFileContext(filePath);
        String md5 = getMD5digest(context);
        try {
            writeToFile(filePath + ".md5", md5);
        } catch (FileNotFoundException ex) {
            ex.printStackTrace();
        }
    }

    /**
     * Возвращает содержимое файла строкой
     * @param file
     * @return
     * @throws FileNotFoundException
     */
    public static String getFileContext(File file){
        Scanner in;
        String context = "";
        try {
            in = new Scanner(file);
            while (in.hasNext())
                context += in.nextLine();
            in.close();
        } catch (FileNotFoundException ex) {
            ex.printStackTrace();
        }

        return context;
    }
    
    /**
     * Возвращает содержимое файла строкой
     * @param filePath
     * @return
     * @throws FileNotFoundException
     */
    public static String getFileContext(String filePath){
        return getFileContext(new File(filePath));
    }

    /**
     * Генерирует MD5 дайджест для указанного файла
     * @param filePath
     * @deprecated пока решили отказаться от md5 проверок
     * @return
     */
    public static String getGeneratedMD5(String filePath){
        if (isMD5Exists(filePath)){
            return getFileContext(filePath + ".md5");
        }
        return null;
    }

    /**
     * Возвращает цифровой дайджест для переданной строки
     * @param message
     * @deprecated пока решили отказаться от md5 проверок
     * @return String
     */
    public static String getMD5digest(String message) {
        try {
            MessageDigest digest = java.security.MessageDigest.getInstance("MD5");
            digest.update(message.getBytes());
            return new BigInteger(1, digest.digest()).toString(16);
        } catch (NoSuchAlgorithmException ex) {
            System.err.println(ex.getMessage());
            return null;
        }
    }

    /**
     * Проверяет, существует ли md5 для данного файла
     * @param fileName
     * @deprecated пока решили отказаться от md5 проверок
     * @return
     */
    public static boolean isMD5Exists(String fileName){
        File md5 = new File(fileName + ".md5");
        if (md5.exists())
            return true;
        else
            return false;
    }


}
