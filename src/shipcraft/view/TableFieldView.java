/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package shipcraft.view;

import java.awt.Color;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableColumn;
import shipcraft.core.GameManager;
import shipcraft.model.FieldTableModel;
import static shipcraft.core.Config.*;
import static shipcraft.core.Constants.*;

/**
 * @version $Id: TableFieldView.java 76 2010-06-23 02:05:43Z spr1ng $
 * @author max, spr1ng
 */
public class TableFieldView extends JTable {

    //private Field field;
    private FieldTableModel fieldTableModel;

    //Transform field model representation to JTableModel
    public void updateTableModel() {
         fieldTableModel.refreshTableModel(GameManager.getField().getCorpuses());
    }

    public TableFieldView() {
        super();
        fieldTableModel = new FieldTableModel(GameManager.getField());
        setModel(fieldTableModel);
         //Делаем свой рендерер для заливки ячеек цветом
        DefaultTableCellRenderer colorRenderer = new DefaultTableCellRenderer() {
            @Override
            public void setValue(Object value) {
                if (value != null) {
                    String cellValue = value.toString();
                    if (cellValue.contains(".")) {
                        setBackground(Color.BLACK);
                    } else {
                        setBackground(Color.DARK_GRAY);
                    }
                    for (int i = 0; i < SHIP_QUANTITY; i++) {
                        if (cellValue.contains(IDENTIFICATORS[i])) {
                            setBackground(COLORS[i]);
                        }
                    }
                }
            }
        };

        setGridColor(Color.BLACK);
        setBackground(Color.BLACK);
        setRowHeight(CELL_HEIGHT);
        setBackground(Color.BLACK);

        for (int i = 0; i < FIELD_WIDTH; i++) {
            TableColumn col = getColumnModel().getColumn(i);
            col.setMinWidth(CELL_WIDTH);
            col.setMaxWidth(CELL_WIDTH);
            col.setPreferredWidth(CELL_WIDTH);
        }

         if (!IS_DEBUG_MODE) {
            //Устанавливаем поддержку наших цветов для всех столбцов таблицы
            for (int i = 0; i < getColumnCount(); i++) {
                TableColumn column = getColumn(getColumnName(i));
                column.setCellRenderer(colorRenderer);
            }
        }
    }
    public void update() {
        updateTableModel();
        repaint();
    }
}
