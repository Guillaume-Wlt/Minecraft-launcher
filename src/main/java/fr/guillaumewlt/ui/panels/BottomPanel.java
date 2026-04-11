package fr.guillaumewlt.ui.panels;

import fr.guillaumewlt.parser.SettingsJSONParser;
import fr.guillaumewlt.ui.components.ShimmerProgressBarUI;
import fr.guillaumewlt.ui.eventhandler.ButtonHandler;
import fr.guillaumewlt.utils.ProgressBarUtils;
import fr.guillaumewlt.workflow.LauncherContext;

import javax.swing.*;
import java.awt.*;
import java.util.List;
import java.util.concurrent.CountDownLatch;

public class BottomPanel extends JPanel {

    private final LauncherContext context;
    private final CountDownLatch latch;

    private Color backgroundColor;
    private String version;
    private String username;
    private boolean isSelected;

    private JComboBox<String> versionCombo;
    private JCheckBox rememberCheckBox;
    private JButton playBtn;
    private JTextField usernameField;
    private final JProgressBar progressBar;

    public BottomPanel(LauncherContext context, CountDownLatch latch) {
        super(new BorderLayout(0, 6));

        this.context = context;
        this.latch = latch;

        backgroundColor = Color.decode("#383838");

        SettingsJSONParser settingsJSONParser = new SettingsJSONParser(context);
        settingsJSONParser.readSettings();
        version = settingsJSONParser.getVersion();
        username = settingsJSONParser.getUsername();

        setBorder(BorderFactory.createEmptyBorder(8, 12, 8, 12));
        setBackground(backgroundColor);

        versionCombo  = new JComboBox<>();
        playBtn       = new JButton("PLAY");
        usernameField = new JTextField(15);

        populateVersionCombo();

        if (version != null && username != null) {
            isSelected = true;
            usernameField.setText(username);
            versionCombo.setSelectedItem(version);
        } else {
            isSelected = false;
        }

        rememberCheckBox = new JCheckBox("Save preferences");
        rememberCheckBox.setSelected(isSelected);

        playBtn.putClientProperty("JButton.buttonType", "roundRect");
        playBtn.setFont(playBtn.getFont().deriveFont(Font.BOLD, 13f));
        playBtn.setPreferredSize(new Dimension(160, 36));
        playBtn.setActionCommand("playBtn");

        ButtonHandler buttonHandler = new ButtonHandler();
        buttonHandler.setContext(context);
        buttonHandler.setLatch(latch);
        buttonHandler.setPlayDependencies(versionCombo, usernameField, rememberCheckBox);

        playBtn.addActionListener(buttonHandler);

        progressBar = new JProgressBar(0, 100);
        progressBar.setUI(new ShimmerProgressBarUI());
        progressBar.setForeground(new Color(0, 170, 0));
        progressBar.setOpaque(false);
        progressBar.setStringPainted(true);
        progressBar.setString("");
        progressBar.setPreferredSize(new Dimension(0, 22));
        progressBar.setVisible(false);
        ProgressBarUtils.register(progressBar);

        add(progressBar,    BorderLayout.NORTH);
        add(buildMainRow(), BorderLayout.CENTER);

    }

    // ----------------------------------------------------------LaunchBar

    private JPanel buildMainRow() {
        JPanel row = new JPanel(new GridLayout(1, 3, 8, 0));
        row.setBackground(backgroundColor);
        row.add(buildVersionSection());
        row.add(buildPlaySection());
        row.add(buildUsernameSection());
        return row;
    }

    private JPanel buildVersionSection() {
        JPanel inner = new JPanel(new BorderLayout(0, 2));
        inner.setBackground(backgroundColor);
        inner.add(new JLabel("Version :"), BorderLayout.NORTH);

        JPanel rowPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        rowPanel.setBackground(backgroundColor);
        rowPanel.add(versionCombo);
        rowPanel.add(Box.createRigidArea(new Dimension(10, 0)));
        rowPanel.add(rememberCheckBox);

        inner.add(rowPanel, BorderLayout.CENTER);

        JPanel wrapper = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        wrapper.setBackground(backgroundColor);
        wrapper.add(inner);
        return wrapper;
    }

    private void populateVersionCombo() {
        List<String> versions = context.getVersions();
        boolean latestVersion = false;
        for (String version : versions) {
            String v = version.toLowerCase();
            if (!Character.isDigit(v.charAt(0))) continue; // Si la version de commence pas par un Chiffre -> Skip
            if (v.contains("w") || v.contains("snapshot") || v.contains("pre") || v.contains("rc")) continue; // Si la version contient ces mentions -> Skip
            if (!latestVersion) {
                version += " (Latest)";
                latestVersion = true;
            }
            versionCombo.addItem(version);
        }
    }

    private JPanel buildPlaySection() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(backgroundColor);
        panel.add(playBtn);
        return panel;
    }

    private JPanel buildUsernameSection() {
        JPanel inner = new JPanel(new BorderLayout(0, 2));
        inner.setBackground(backgroundColor);
        inner.add(new JLabel("Username :"), BorderLayout.NORTH);
        inner.add(usernameField, BorderLayout.CENTER);
        JPanel wrapper = new JPanel(new FlowLayout(FlowLayout.RIGHT, 0, 0));
        wrapper.setBackground(backgroundColor);
        wrapper.add(inner);
        return wrapper;
    }
}
