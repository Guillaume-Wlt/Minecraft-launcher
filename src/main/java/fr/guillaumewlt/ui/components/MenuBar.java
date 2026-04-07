package fr.guillaumewlt.ui.components;

import fr.guillaumewlt.ui.windows.ConsoleWindow;

import javax.swing.*;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;

public class MenuBar extends JMenuBar {

    private JDialog consoleWindow;

    public MenuBar(JFrame parent) {
        JMenu fileMenu = new JMenu("File");
        fileMenu.add(settingsMenuItem());
        fileMenu.add(exitMenuItem());
        add(fileMenu);

        JMenu terminalMenu = new JMenu("Terminal");
        terminalMenu.add(terminalMenuItem(parent));
        add(terminalMenu);
    }

    // ----------------------------------------------------------File Items

    private JMenuItem settingsMenuItem() {
        JMenuItem settingsMenuItem = new JMenuItem("Settings");
        settingsMenuItem.addActionListener(e -> {
            JOptionPane.showMessageDialog(null, "To be implemented!", "Settings", JOptionPane.INFORMATION_MESSAGE);
        });
        return settingsMenuItem;
    }

    private JMenuItem exitMenuItem() {
        JMenuItem exitMenuItem = new JMenuItem("Exit");
        exitMenuItem.addActionListener(e -> System.exit(0));
        /*
        * VK_Q - La touche Q
        * CTRL_DOWN_MASK - La touche CTRL
        */
        exitMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Q, InputEvent.CTRL_DOWN_MASK));
        return exitMenuItem;
    }

    // ----------------------------------------------------------Terminal Items

    private JMenuItem terminalMenuItem(JFrame parent) {
        JMenuItem terminalMenuItem = new JMenuItem("Console");
        terminalMenuItem.addActionListener(e -> {
            if (consoleWindow == null) {
                consoleWindow = ConsoleWindow.getInstance(parent);
            }
            if (!consoleWindow.isVisible()) {
                consoleWindow.setVisible(true);
            }
            consoleWindow.toFront();
        });
        return terminalMenuItem;
    }
}
