/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package shipcraft.view;

import java.awt.AWTException;
import java.awt.Image;
import java.awt.MenuItem;
import java.awt.PopupMenu;
import java.awt.SystemTray;
import java.awt.Toolkit;
import java.awt.TrayIcon;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JOptionPane;

/**
 * @version $Id: TrayManager.java 77 2010-06-23 06:58:35Z spr1ng $
 * @author stream
 */
public class TrayManager {

    private static GameView gameView;
    private static TrayManager trayManager = new TrayManager();

    public static TrayManager trayIt(GameView gView) {
        gameView = gView;
//        gameView.setVisible(true);
        return trayManager;
    }

    private TrayManager() {
        try {
            init();
        } catch (AWTException ex) {
        }
    }
    private MenuItem showWindowItem = new MenuItem("Hide");

    private void init() throws AWTException {
        if (!SystemTray.isSupported()) {
            System.out.println("SystemTray is not supported");
            JOptionPane.showMessageDialog(null, "You operation system is not supported SystemTray");
            return;
        }

        SystemTray tray = SystemTray.getSystemTray();
        Toolkit toolkit = Toolkit.getDefaultToolkit();
        Image image = toolkit.getImage("images/icon.png");

        PopupMenu menu = new PopupMenu();

        showWindowItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                showOrHideWindow();
            }
        });
        menu.add(showWindowItem);
        MenuItem closeWindowItem = new MenuItem("Exit");
        closeWindowItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });
        menu.add(closeWindowItem);
        TrayIcon icon = new TrayIcon(image, "ShipCraft", menu);
        icon.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                showOrHideWindow();
            }
        });
        icon.setImageAutoSize(true);
        tray.add(icon);
    }

    private void showOrHideWindow() {
        if (gameView.isVisible()) {
            showWindowItem.setLabel("Show");
            gameView.setVisible(false);
        } else {
            showWindowItem.setLabel("Hide");
            gameView.setVisible(true);
        }
    }
}
