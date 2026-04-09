package fr.guillaumewlt.ui.windows;

import fr.guillaumewlt.ui.eventhandler.ButtonHandler;
import fr.guillaumewlt.utils.ConsoleUtils;

import javax.swing.*;
import javax.swing.text.DefaultCaret;
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
        setSize(960, 680);
        setLayout(new GridBagLayout());
        setResizable(false);
        setLocationRelativeTo(parent);
        URL iconURL = getClass().getResource("/launcher-logo.png");
        Image icon = new ImageIcon(iconURL).getImage().getScaledInstance(32, 32, Image.SCALE_SMOOTH);
        setIconImage(icon); // Set image for ConsoleWindow
        setDefaultCloseOperation(HIDE_ON_CLOSE);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weighty = 1.0;
        // Left Panel -> 3/4
        gbc.weightx = 0.9;
        add(consoleWrapper(), gbc);

        // Right Panel -> 1/4
        gbc.weightx = 0.1;
        add(sideBarWrapper(), gbc);
    }

    private JPanel consoleWrapper() {
        JPanel wrapper = new JPanel(new BorderLayout());
        JTextPane textPane = new JTextPane();
        textPane.setEditable(false);
        textPane.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 12));
        textPane.setBackground(new Color(30, 30, 30));
        textPane.setForeground(new Color(220, 220, 220));
        textPane.setBorder(BorderFactory.createEmptyBorder(0,4,4,4));
        ConsoleUtils.register(textPane);

        DefaultCaret caret = (DefaultCaret) textPane.getCaret();
        caret.setUpdatePolicy(DefaultCaret.NEVER_UPDATE);

        JScrollPane scroll = new JScrollPane(textPane);
        scroll.putClientProperty("JScrollBar.width", 8);
        scroll.setBorder(BorderFactory.createLineBorder(Color.decode("#5C5C5C"), 1));
        wrapper.add(scroll, BorderLayout.CENTER);
        return wrapper;
    }

    private JPanel sideBarWrapper() {
        JPanel wrapper = new JPanel();
        wrapper.setLayout(new BoxLayout(wrapper, BoxLayout.Y_AXIS));
        wrapper.setBackground(new Color(40, 40, 40));
        wrapper.setBorder(BorderFactory.createEmptyBorder(10, 10, 0, 10));

        JButton clrButton = new JButton("clear console");
        clrButton.setActionCommand("clrBtn");
        clrButton.putClientProperty("JButton.buttonType", "roundRect");
        clrButton.setFont(clrButton.getFont().deriveFont(Font.BOLD, 13f));
        clrButton.setMaximumSize(new Dimension(Integer.MAX_VALUE, 30));
        clrButton.setMargin(new Insets(5,5,5,5));

        JButton scrollDownButton = new JButton("go to bottom");
        scrollDownButton.setActionCommand("toBottomBtn");
        scrollDownButton.putClientProperty("JButton.buttonType", "roundRect");
        scrollDownButton.setFont(scrollDownButton.getFont().deriveFont(Font.BOLD, 13f));
        scrollDownButton.setMaximumSize(new Dimension(Integer.MAX_VALUE, 30));
        scrollDownButton.setMargin(new Insets(5,5,5,5));

        JButton copyButton = new JButton("copy");
        copyButton.setActionCommand("copyBtn");
        copyButton.putClientProperty("JButton.buttonType", "roundRect");
        copyButton.setFont(copyButton.getFont().deriveFont(Font.BOLD, 13f));
        copyButton.setMaximumSize(new Dimension(Integer.MAX_VALUE, 30));
        copyButton.setMargin(new Insets(5,5,5,5));

        ButtonHandler buttonHandler = new ButtonHandler();
        buttonHandler.setConsoleDependencies(ConsoleUtils.getConsolePane());
        clrButton.addActionListener(buttonHandler);
        scrollDownButton.addActionListener(buttonHandler);
        copyButton.addActionListener(buttonHandler);

        wrapper.add(clrButton);
        wrapper.add(Box.createRigidArea(new Dimension(0, 10)));
        wrapper.add(scrollDownButton);
        wrapper.add(Box.createRigidArea(new Dimension(0, 10)));
        wrapper.add(copyButton);
        wrapper.add(Box.createVerticalGlue());
        return wrapper;
    }
}
