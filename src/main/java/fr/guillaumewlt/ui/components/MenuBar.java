package fr.guillaumewlt.ui.components;

import fr.guillaumewlt.ui.windows.ConsoleWindow;
import fr.guillaumewlt.ui.windows.SettingsWindow;
import fr.guillaumewlt.workflow.LauncherContext;

import javax.swing.*;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;

public class MenuBar extends JMenuBar {

    private final LauncherContext context;

    private JDialog consoleWindow;
    private JDialog settingsWindow;

    public MenuBar(LauncherContext context, JFrame parent) {
        this.context = context;
        JMenu fileMenu = new JMenu("File");
        fileMenu.add(settingsMenuItem(parent));
        fileMenu.add(exitMenuItem());
        add(fileMenu);

        JMenu terminalMenu = new JMenu("Terminal");
        terminalMenu.add(terminalMenuItem(parent));
        add(terminalMenu);
    }

    // ----------------------------------------------------------File Items

    private JMenuItem settingsMenuItem(JFrame parent) {
        JMenuItem settingsMenuItem = new JMenuItem("Settings");
        settingsMenuItem.addActionListener(e -> {
            SettingsWindow.setContext(context);
            if (settingsWindow == null) {
                settingsWindow = SettingsWindow.getInstance(parent);
            }
            if (!settingsWindow.isVisible()) {
                settingsWindow.setVisible(true);
            }
            settingsWindow.toFront();
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
