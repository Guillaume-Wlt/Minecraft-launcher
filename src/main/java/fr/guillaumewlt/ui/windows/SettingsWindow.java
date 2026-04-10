package fr.guillaumewlt.ui.windows;

import fr.guillaumewlt.ui.eventhandler.ButtonHandler;
import fr.guillaumewlt.utils.SettingsUtils;
import fr.guillaumewlt.workflow.LauncherContext;
import lombok.Setter;

import javax.swing.*;
import java.awt.*;
import java.net.URL;

public class SettingsWindow extends JDialog {

    @Setter
    private static LauncherContext context;
    private static SettingsWindow instance;

    private String minRam;
    private String maxRam;
    private boolean isSelected;

    private JTextField minRamTextField;
    private JTextField maxRamTextField;
    private JTextPane minRamStatusPane;
    private JTextPane maxRamStatusPane;
    private JCheckBox saveTickBox;

    public static SettingsWindow getInstance(JFrame parent) {
        if (instance == null) {
            instance = new SettingsWindow(parent);
        }
        return instance;
    }

    public SettingsWindow(JFrame parent) {
        super(parent, "Settings", false);

        SettingsUtils settingsUtils = new SettingsUtils(context);
        settingsUtils.readSettings();
        minRam = settingsUtils.getMinRam();
        maxRam = settingsUtils.getMaxRam();

        if (!minRam.equals("512") && !maxRam.equals("2048")) {
            isSelected = true;
        } else {
            isSelected = false;
        }

        setSize(300, 400);
        setResizable(false);
        setLocationRelativeTo(parent);
        URL iconURL = getClass().getResource("/launcher-logo.png");
        Image icon = new ImageIcon(iconURL).getImage().getScaledInstance(32, 32, Image.SCALE_SMOOTH);
        setIconImage(icon);
        setDefaultCloseOperation(HIDE_ON_CLOSE);

        add(contentPanel());
    }

    private JPanel contentPanel() {
        JPanel wrapper = new JPanel();
        wrapper.setLayout(new BoxLayout(wrapper, BoxLayout.Y_AXIS));
        wrapper.setBackground(new Color(40, 40, 40));
        wrapper.setBorder(BorderFactory.createEmptyBorder(10,10,10,10));

        JLabel minRamLabel = new JLabel("Minimum RAM (Default : 512)");
        minRamLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel maxRamLabel = new JLabel("Maximum RAM (Default : 2048)");
        maxRamLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        wrapper.add(minRamLabel);
        wrapper.add(Box.createRigidArea(new Dimension(0, 5)));
        wrapper.add(minRamPanel());
        wrapper.add(Box.createRigidArea(new Dimension(0, 10)));
        wrapper.add(maxRamLabel);
        wrapper.add(Box.createRigidArea(new Dimension(0, 5)));
        wrapper.add(maxRamPanel());
        wrapper.add(Box.createRigidArea(new Dimension(0, 5)));
        wrapper.add(tickBoxWrapper());
        wrapper.add(Box.createRigidArea(new Dimension(0, 20)));
        wrapper.add(btnWrapper());
        wrapper.add(Box.createVerticalGlue());
        return wrapper;
    }

    private JPanel minRamPanel() {
        JPanel minRamPanel = new JPanel(new BorderLayout());
        minRamPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        minRamPanel.setBackground(new Color(40, 40, 40));
        minRamPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 30));

        JTextField minRamField = new JTextField();
        if (minRam == null) {
            minRamField.setText("512");
        } else {
            minRamField.setText(minRam);
        }
        minRamField.setLayout(new BorderLayout());

        JTextPane ramStatusPane = new JTextPane();
        ramStatusPane.setEditable(false);
        ramStatusPane.setVisible(false);
        ramStatusPane.setPreferredSize(new Dimension(20, 0));
        ramStatusPane.setBackground(new Color(40, 40, 40));
        ramStatusPane.setText("");

        JLabel sizeUnitLabel = new JLabel("MB");
        sizeUnitLabel.setOpaque(false);

        minRamTextField = minRamField;
        minRamStatusPane = ramStatusPane;

        minRamField.add(sizeUnitLabel, BorderLayout.EAST);

        minRamPanel.add(minRamField, BorderLayout.CENTER);
        minRamPanel.add(ramStatusPane, BorderLayout.EAST);
        return minRamPanel;
    }

    private JPanel maxRamPanel() {
        JPanel maxRamPanel = new JPanel(new BorderLayout());
        maxRamPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        maxRamPanel.setBackground(new Color(40, 40, 40));
        maxRamPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 30));

        JTextField maxRamField = new JTextField();
        if (maxRam == null) {
            maxRamField.setText("2048");
        } else {
            maxRamField.setText(maxRam);
        }
        maxRamField.setLayout(new BorderLayout());

        JTextPane ramStatusPane = new JTextPane();
        ramStatusPane.setEditable(false);
        ramStatusPane.setVisible(false);
        ramStatusPane.setPreferredSize(new Dimension(20, 0));
        ramStatusPane.setBackground(new Color(40, 40, 40));
        ramStatusPane.setText("");

        JLabel sizeUnitLabel = new JLabel("MB");
        sizeUnitLabel.setOpaque(false);

        maxRamTextField = maxRamField;
        maxRamStatusPane = ramStatusPane;

        maxRamField.add(sizeUnitLabel, BorderLayout.EAST);

        maxRamPanel.add(maxRamField, BorderLayout.CENTER);
        maxRamPanel.add(ramStatusPane, BorderLayout.EAST);

        return maxRamPanel;
    }

    private JPanel tickBoxWrapper() {
        JPanel tickBoxPanel = new JPanel(new BorderLayout());
        tickBoxPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        tickBoxPanel.setBackground(new Color(40, 40, 40));
        tickBoxPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 30));

        JCheckBox saveCheckBox = new JCheckBox("Remember Settings");
        saveCheckBox.setSelected(isSelected);

        saveTickBox = saveCheckBox;

        tickBoxPanel.add(saveCheckBox, BorderLayout.WEST);

        return tickBoxPanel;
    }

    private JPanel btnWrapper() {
        JPanel buttonsWrapper = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        buttonsWrapper.setAlignmentX(Component.LEFT_ALIGNMENT);
        buttonsWrapper.setBackground(new Color(40, 40, 40));
        buttonsWrapper.setMaximumSize(new Dimension(Integer.MAX_VALUE, 30));

        JButton saveBtn = new JButton("Save");
        saveBtn.setActionCommand("saveSettings");
        saveBtn.putClientProperty("JButton.buttonType", "roundRect");

        JButton DirBtn = new JButton("Check Local files");
        DirBtn.setActionCommand("openDir");
        DirBtn.putClientProperty("JButton.buttonType", "roundRect");

        ButtonHandler buttonHandler = new ButtonHandler();
        buttonHandler.setContext(context);
        buttonHandler.setSaveSettingsDependencies(minRamTextField, maxRamTextField, minRamStatusPane, maxRamStatusPane, saveTickBox);
        DirBtn.addActionListener(buttonHandler);
        saveBtn.addActionListener(buttonHandler);

        buttonsWrapper.add(saveBtn);
        buttonsWrapper.add(Box.createRigidArea(new Dimension(10,0)));
        buttonsWrapper.add(DirBtn);

        return buttonsWrapper;
    }
}
