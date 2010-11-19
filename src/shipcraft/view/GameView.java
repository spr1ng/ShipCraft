/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package shipcraft.view;

import java.awt.event.WindowEvent;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JMenuBar;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.util.ArrayList;
import java.util.Vector;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ScrollPaneConstants;
import shipcraft.ai.ship.AttackerShipAI;
import shipcraft.ai.ship.DefaultShipAI;
import shipcraft.ai.ship.DefenderShipAI;
import shipcraft.ai.ship.EvasorAI;
import shipcraft.ai.ship.InviserShipAI;
import shipcraft.ai.ship.SmartAI;
import shipcraft.core.GameManager;
import shipcraft.core.ScriptManager;
import shipcraft.intrfc.ShipAI;
import shipcraft.model.Field;
import shipcraft.model.Ship;
import static shipcraft.utils.Utils.*;
import static shipcraft.core.Config.*;
import static shipcraft.core.Constants.*;
import shipcraft.view.TrayManager.*;

/**
 * @version $Id: GameView.java 77 2010-06-23 06:58:35Z spr1ng $
 * @author spr1ng, stream
 */
public class GameView extends JFrame {
     
    private static AbstractFieldView fieldView;
    private LoggableTextArea jTextArea;
    private JLabel[] labels;
    private JComboBox[] comboboxes;

    public LoggableTextArea getTextArea(){
        return jTextArea;
    }

    /** Обновляет модель таблицы поля согласно модели поля и перерисовывает вьюшку */
    public void refresh(){
        //TODO: обновляются лейблы с hp
        String[] labelsText = getLabelsText();
        for (int i = 0; i < labelsText.length; i++) {
            labels[i].setText(labelsText[i]);
        }
        fieldView.update();
    }
    
    public GameView() {
        super(GAME_NAME);
        setSize(FRAME_WIDTH, FRAME_HEIGHT);
        fieldView = new ProcessingFieldView();
        setJMenuBar(createMenuBar());
        setLayout(new BorderLayout());
        add(createLogPanel(), BorderLayout.WEST);
        add(createEastPanel(), BorderLayout.CENTER);
        add(createButtonPanel(), BorderLayout.SOUTH);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        setResizable(false);
//        pack();
//        setVisible(true);
    }

    private JMenuBar createMenuBar(){
        JMenuBar menuBar = new JMenuBar();
        JMenu fileMenu = new JMenu("File");
        JMenuItem exitItem = new JMenuItem("Exit");
        exitItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });
        fileMenu.add(exitItem);
        menuBar.add(fileMenu);
        return menuBar;
    }

    private JPanel createEastPanel() {
        JPanel eastPanel = new JPanel();
        eastPanel.setLayout(new BorderLayout());
        eastPanel.add(createComboBoxPanel(), BorderLayout.EAST);
        eastPanel.setPreferredSize(new Dimension(EAST_PANEL_WIDTH, 400));
        eastPanel.add(fieldView, BorderLayout.CENTER);
        return eastPanel;
    }

    private JScrollPane createLogPanel(){
        jTextArea = new LoggableTextArea(log.cache());
        jTextArea.setFont(new Font(null, Font.PLAIN, 9));
        JScrollPane shipCraftScroll = new JScrollPane(jTextArea);
        shipCraftScroll.setViewportBorder(BorderFactory.createEmptyBorder());
        shipCraftScroll.setBorder(BorderFactory.createEmptyBorder());
        shipCraftScroll.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        shipCraftScroll.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_NEVER);
        return shipCraftScroll;
    }
    
    private String[] getLabelsText(){
        String[] labelTexts = new String[SHIP_QUANTITY];
        Field field = GameManager.getField();
        for (int i = 0; i < SHIP_QUANTITY; i++) {
            String shipId = IDENTIFICATORS[i];
            Ship ship = (Ship)field.getCorpusById(shipId);
            int hp, armor, regen, missileQty, heavyMissileQty,
                upgradablePoints, stunningMissileQty, criticalAttackLevel,
                evasionLevel, invisLevel;
            String s1="",s2="",shipName="",teamName="";
            Color color = new Color(83,83,110);
            if (ship == null) {
                hp = armor = regen = missileQty = heavyMissileQty
                   = stunningMissileQty = upgradablePoints
                   = criticalAttackLevel = evasionLevel = invisLevel = 0;
                s1 = "<strike>";
                s2 = "</strike>";
                shipName = shipId;
                teamName = "";
            } else {
                hp = ship.getHp();
                armor = ship.getArmor();
                regen = ship.getRegenHpLevel();
                missileQty = ship.getMissileQuantity();
                heavyMissileQty = ship.getHeavyMissileQuantity();
                stunningMissileQty = ship.getStunningMissileQuantity();
                upgradablePoints = ship.getUpgradePoints();
                criticalAttackLevel = ship.getCriticalAttackLevel();
                evasionLevel = ship.getEvasionLevel();
                invisLevel = ship.getInvisibleLevel();
                shipName = ship.getName() != null ? ship.getName() : "";//shipId;
                teamName = ship.getTeamName() != null ? ship.getTeamName() : "";
                int nameLen = 8;//max name length
                //Names must have length less than nameLen simbles
                if (shipName.length() > nameLen) shipName = shipName.substring(0, nameLen-1);
                if (teamName.length() > nameLen) teamName = teamName.substring(0, nameLen-1);
                if (!teamName.isEmpty()) teamName = " (" + teamName + ") -";
//                color = COLORS[field.getShips().indexOf(ship)];
                for (int j = 0; j < IDENTIFICATORS.length; j++) {
                    if (IDENTIFICATORS[j].equals(shipId))
                        color = COLORS[j];
                }
            }

            String rgb = Integer.toHexString(color.getRGB());
            rgb = rgb.substring(2, rgb.length());
            String font = "<font color='#" + rgb + "' size='3'>";
            String labelText = "<html><h5>" + s1 + "<b>" + font + "&nbsp;" + shipId + "</font></b> - " + shipName + teamName +
                               " hp: ["     + hp +
//                               "; Armory: ["    + armor +
//                               "]; Regen: ["    + regen +
//                               "]; Crit: [" + criticalAttackLevel +
                               "]; Upgr: ["     + upgradablePoints +
                               "]; <br/>Mis: ["    + missileQty +
                               "]; Heavy_Mis: [" + heavyMissileQty +
                               "]; Stun_Mis: [" + stunningMissileQty +
//                               "]; Evas: [" + evasionLevel +
//                               "]; Invis: [" + invisLevel +
                               "]: "        + s2 + "</h5></html>";
            labelTexts[i] = labelText;
        }
        return labelTexts;
    }

    private JPanel createComboBoxPanel() {
        //Комбобоксы
        JPanel comboBoxPanel = new JPanel();
        JPanel labelsPanel = new JPanel();
        labelsPanel.setLayout(new VerticalLayout());
        JPanel boxesPanel = new JPanel();
        boxesPanel.setLayout(new VerticalLayout());

        labelsPanel.setBackground(Color.BLACK);//(new Color(54,54,54));
        boxesPanel.setBackground(Color.BLACK);//(new Color(54,54,54));
        
        comboBoxPanel.setLayout(new BorderLayout());

        labels = new JLabel[SHIP_QUANTITY];
        comboboxes = new JComboBox[SHIP_QUANTITY];
        String[] labelsText = getLabelsText();
        //Получаем список всех актуальных CorpusAI
        Vector<String> aiNamesList = ScriptManager.getAiNamesList();
        SplashScreen.setPositionPercent(90);
        //Добавляем в него дефолтные CorpusAI
        aiNamesList.add(0, "Default");
        aiNamesList.add(1, "Attacker");
        aiNamesList.add(2, "Defender");
        aiNamesList.add(3, "Inviser");
        aiNamesList.add(4, "Evasor");
        aiNamesList.add(5, "Smarty");
        for (int i = 0; i < SHIP_QUANTITY; i++) {
            //Лэйблы
            labels[i] = new JLabel(labelsText[i], JLabel.LEFT);
            labels[i].setPreferredSize(new Dimension(LABEL_WIDTH, 30));
//            labels[i].setFont(new Font("Courier", Font.PLAIN, 10));
            labels[i].setForeground(new Color(83,83,110));//(COLORS[i]);
            labelsPanel.add(labels[i]);
            //Комбобоксы
            comboboxes[i] = new JComboBox(aiNamesList);
            labels[i].setLabelFor(comboboxes[i]);
            comboboxes[i].setPreferredSize(new Dimension(140, 30));
            comboboxes[i].setMaximumRowCount(10);
            boxesPanel.add(comboboxes[i]);
        }
        comboBoxPanel.add(labelsPanel, BorderLayout.WEST);
        comboBoxPanel.add(boxesPanel, BorderLayout.EAST);
        return comboBoxPanel;
    }

    /** Переопределяет ShipAI для выбранного корабля */
    public void refreshAi(Ship ship){
        int idx = 0;
        String shipId = ship.getId();
        for (JLabel label : labels) {
            //Если имя корабля найдено в списке
            if (label.getText().contains(shipId + "</font>")){// -
                //и корабль еще жив
                if (!label.getText().contains("<strike>")){
                    ShipAI ai;
                    String aiName = comboboxes[idx].getSelectedItem().toString();
                    if (aiName.equalsIgnoreCase("default")){
                        ai = new DefaultShipAI();
                    } else if(aiName.equalsIgnoreCase("attacker")){
                        ai = new AttackerShipAI();
                    } else if(aiName.equalsIgnoreCase("defender")){
                        ai = new DefenderShipAI();
                    } else if(aiName.equalsIgnoreCase("inviser")){
                        ai = new InviserShipAI();
                    } else if(aiName.equalsIgnoreCase("evasor")){
                        ai = new EvasorAI();
                    } else if(aiName.equalsIgnoreCase("smarty")){
                        ai = new SmartAI();
                    } else {
                        ai = ScriptManager.getValidAi(aiName);
//                        String aiMainPath = ScriptManager.getAiMainPath(aiName);
//                        String md5 = getGeneratedMD5(aiMainPath);
//                        if (md5 != null){
//                            String actualMD5 = ScriptManager.getAiMD5(aiName);
//                            //Если изменений не произвели
//                            if (md5.equals(actualMD5))
//                                ai = null;
//                            else
//                                generateFileMD5(aiMainPath);
//                        } else {
//                            generateFileMD5(aiMainPath);
//                        }
                    }
                    
//                    if (ai != null){
                        ship.setName(ai.getShipName());
                        ship.setTeamName(ai.getTeamName());
                        ship.setAi(ai);
//                    }
                    break;
                }
            }
            idx++;
        }
        
    }

    public void refreshComboboxes(boolean isGameStarted){
        //Если это пуск игры - делаем комбобоксы неактивными
        //и наоборот
        for (int i = 0; i < comboboxes.length; i++) {
            comboboxes[i].setEnabled(!isGameStarted);
        }
        //Сопоставляем каждому кораблю CorpusAI в комбобоксе
        if (isGameStarted){
            ArrayList<Ship> ships = GameManager.getField().getShips();
            for (Ship ship : ships) {
                refreshAi(ship);
            }
        }
    }

    private JPanel createButtonPanel() {
        JPanel buttonPanel = new JPanel();
        buttonPanel.setBorder(BorderFactory.createEtchedBorder());
        buttonPanel.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.RIGHT));
        buttonPanel.setBackground(Color.BLACK);
        JButton restartGameButton = new JButton("Restart");
        restartGameButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                GameManager.stopGame();
                GameManager.setField(new Field());
                refresh();//refreshes view
                refreshComboboxes(true);
                GameManager.startOrPauseGame();
            }
        });
        JButton startGameButton = new JButton("Start | Pause");
        startGameButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                boolean isGameStarted = !GameManager.isGameStarted();
                refreshComboboxes(isGameStarted);
                GameManager.startOrPauseGame();
            }
        });
        buttonPanel.add(startGameButton);
        buttonPanel.add(restartGameButton);
        return buttonPanel;
    }
}
