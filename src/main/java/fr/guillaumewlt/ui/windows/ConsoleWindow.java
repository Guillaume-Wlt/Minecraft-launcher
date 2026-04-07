package fr.guillaumewlt.ui.windows;

import fr.guillaumewlt.utils.ConsoleUtils;

import javax.swing.*;
import java.awt.*;
import java.net.URL;

public class ConsoleWindow extends JDialog {

    private static ConsoleWindow instance;

    public static ConsoleWindow getInstance(JFrame parent) {
        if (instance == null) {
            instance = new ConsoleWindow(parent);
        }
        return instance;
    }

    private ConsoleWindow(JFrame parent) {
        super(parent, "Console", false);
        setSize(1080, 600);
        setLocationRelativeTo(parent);
        URL iconURL = getClass().getResource("/launcher-logo.png");
        Image icon = new ImageIcon(iconURL).getImage().getScaledInstance(32, 32, Image.SCALE_SMOOTH);
        setIconImage(icon); // Set image for ConsoleWindow
        setDefaultCloseOperation(HIDE_ON_CLOSE);

        JTextPane textPane = new JTextPane();
        textPane.setEditable(false);
        textPane.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 14));
        textPane.setBorder(BorderFactory.createEmptyBorder(0, 4, 4, 4));
        ConsoleUtils.register(textPane);
        add(new JScrollPane(textPane));
    }

}
