/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package shipcraft.model;

import java.util.List;
import javax.swing.table.AbstractTableModel;
import static shipcraft.core.Config.*;

/**
 * @version $Id: FieldTableModel.java 76 2010-06-23 02:05:43Z spr1ng $
 * @author spr1ng
 */
public class FieldTableModel extends AbstractTableModel{
    private static String[][] table = new String[FIELD_WIDTH][FIELD_HEIGHT];

    public FieldTableModel(Field field) {
        refreshTableModel(field.getCorpuses());
    }

    public int getRowCount() {
        return FIELD_HEIGHT;
    }

    public int getColumnCount() {
        return FIELD_WIDTH;
    }

    public Object getValueAt(int x, int y) {
        return table[y][x];
    }

    /** Обновляет значение клетки таблицы согласно модели Field */
    public void refreshTableModel(List<Corpus> corpuses){
        for (int x = 0; x < FIELD_WIDTH; x++) {
            for (int y = 0; y < FIELD_HEIGHT; y++) {
                String cellValue = ".";
                //Если начальные координаты..
                //выводим разметку
                if (x == 0) {cellValue = String.valueOf(y);}
                if (y == 0) {cellValue = String.valueOf(x);}
                //Смотрим все тела..
                for (Corpus corpus : corpuses) {
                    String corpusId = corpus.getId();
                    int posX = corpus.getPosX();
                    int posY = corpus.getPosY();
                    int shipWidth = corpus.getWidth();
                    int shipHeight = corpus.getHeight();
                    //Если текущая координата поля входит в область координат тела..
                    if (x >= posX && x < posX + shipWidth) {
                        if (y >= posY && y < posY + shipHeight) {
                            //то назначаем этой клетке значение: id тела
                            cellValue = corpusId;
                        }
                    }
                }
                //Если начальные координаты..
                //выводим разметку
                if (x == 0) {cellValue = String.valueOf(y);}
                if (y == 0) {cellValue = String.valueOf(x);}
                //иначе - назначаем значение по умолчанию //PENDING: может убрать этот комментарий? =)
                table[x][y] = cellValue;
            }
        }

    }

    public void logTableModel(){
        for (int x = 1; x < FIELD_WIDTH; x++) {
            for (int y = 1; y < FIELD_HEIGHT; y++) {
                System.out.print(table[x][y]);
            }
        }
    }
}
