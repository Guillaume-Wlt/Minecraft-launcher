package fr.guillaumewlt.ui.windows;

import javax.swing.*;

public class ConsoleWindow extends JDialog {

    public ConsoleWindow(JFrame parent) {
        super(parent, "Console", false);
        setSize(800, 600);
        setLocationRelativeTo(parent);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        JTextPane textPane = new JTextPane();
        textPane.setEditable(false);
        add(new JScrollPane(textPane));
    }

}
