package fr.guillaumewlt.ui.windows;

import javax.swing.*;
import java.awt.*;
import java.net.URL;

public class SettingsWindow extends JDialog {

    private static SettingsWindow instance;

    public static SettingsWindow getInstance(JFrame parent) {
        if (instance == null) {
            instance = new SettingsWindow(parent);
        }
        return instance;
    }

    public SettingsWindow(JFrame parent) {
        super(parent, "Settings", false);
        setSize(300, 400);
        setResizable(false);
        setLocationRelativeTo(parent);
        URL iconURL = getClass().getResource("/launcher-logo.png");
        Image icon = new ImageIcon(iconURL).getImage().getScaledInstance(32, 32, Image.SCALE_SMOOTH);
        setIconImage(icon);
        setDefaultCloseOperation(HIDE_ON_CLOSE);
    }
}
