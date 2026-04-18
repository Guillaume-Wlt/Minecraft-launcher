package fr.guillaumewlt.ui.eventhandler;

import fr.guillaumewlt.model.SelectedVersion;
import fr.guillaumewlt.parser.ManifestParser;
import fr.guillaumewlt.ui.panels.BackgroundPanel;
import fr.guillaumewlt.utils.ProgressBarUtils;
import fr.guillaumewlt.workflow.LauncherContext;
import lombok.Setter;
import org.json.JSONObject;

import javax.swing.*;
import javax.swing.text.BadLocationException;
import java.awt.*;
import java.awt.datatransfer.StringSelection;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.util.concurrent.CountDownLatch;

public class ButtonsListener implements ActionListener {

    @Setter
    private LauncherContext context;
    @Setter
    private BackgroundPanel backgroundPanel;

    private JComboBox<String> versionsComboBox;
    private JTextField usernameField;
    private JCheckBox saveCheckBox;

    private JTextPane consolePane;

    private JTextField minRamField;
    private JTextField maxRamField;
    private JTextPane minRamStatusPane;
    private JTextPane maxRamStatusPane;
    private JCheckBox saveSettingsCheckBox;

    @Override
    public void actionPerformed(ActionEvent e) {
        JButton button = (JButton) e.getSource();
        String actionCommand = button.getActionCommand();
        switch (actionCommand) {
            case "playBtn":
                try {
                    if (context == null) return;
                    if (versionsComboBox == null || usernameField == null || saveCheckBox == null) return;
                    String version = versionsComboBox.getSelectedItem().toString();
                    String username = usernameField.getText().trim();

                    if (username.isEmpty()) {
                        JOptionPane.showMessageDialog(null, "Please enter a valid username", "Warning", JOptionPane.WARNING_MESSAGE);
                        return;
                    }

                    SelectedVersion selectedVersion = new ManifestParser(context).jsonparser(version);
                    context.setSelectedVersion(selectedVersion);

                    context.setUsername(username);

                    button.setEnabled(false);
                    button.setText("LAUNCHED");

                    usernameField.setEnabled(false);
                    versionsComboBox.setEnabled(false);

                    System.out.println("Selected version: " + selectedVersion.selectedVersion());
                    System.out.println("Username : " + context.getUsername());

                    if (saveCheckBox.isSelected()) {
                        File settingsFile = new File(context.getLauncherDirs().configDir().path() + "settings.json");
                        try {
                            JSONObject settings;
                            if (settingsFile.exists()) {
                                String content = Files.readString(settingsFile.toPath());
                                settings = new JSONObject(content);
                            } else {
                                settings = new JSONObject();
                            }

                            try (FileWriter fw = new FileWriter(settingsFile)){
                                settings.put("version", selectedVersion.selectedVersion());
                                settings.put("username", context.getUsername());
                                fw.write(settings.toString());
                            }
                        } catch (IOException ex) {
                            ex.printStackTrace();
                        }
                    }

                    ProgressBarUtils.show();
                    CountDownLatch latch = context.getLatch();
                    latch.countDown();
                } catch (Exception ex) {
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(null, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                }
                break;
            case "clrBtn":
                if (consolePane == null) return;
                consolePane.setText("");
                break;
            case "toBottomBtn":
                if (consolePane == null) return;
                SwingUtilities.invokeLater(() -> {
                    JScrollPane scrollPane = (JScrollPane) consolePane.getParent().getParent();
                    JScrollBar bar = scrollPane.getVerticalScrollBar();
                    bar.setValue(bar.getMaximum());
                });
                break;
            case "copyBtn":
                if (consolePane == null) return;
                try {
                    String text = consolePane.getDocument().getText(0, consolePane.getDocument().getLength());
                    Toolkit.getDefaultToolkit().getSystemClipboard().setContents(new StringSelection(text), null);
                } catch (BadLocationException ex) {
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(null, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                }
                break;
            case "saveSettings":
                if (context == null || minRamField == null || maxRamField == null || minRamStatusPane == null || maxRamStatusPane == null || saveSettingsCheckBox == null) return;
                String minRam = minRamField.getText().trim();
                String maxRam = maxRamField.getText().trim();

                if (minRam.isEmpty() || maxRam.isEmpty()) {
                    minRam = "512";
                    maxRam = "2048";
                }

                context.setMinRam(minRam);
                context.setMaxRam(maxRam);

                minRamStatusPane.setText("✔");
                minRamStatusPane.setForeground(Color.GREEN);
                minRamStatusPane.setVisible(true);

                maxRamStatusPane.setText("✔");
                maxRamStatusPane.setForeground(Color.GREEN);
                maxRamStatusPane.setVisible(true);

                if (saveSettingsCheckBox.isSelected()) {
                    File settingsFile = new File(context.getLauncherDirs().configDir().path() + "settings.json");
                    try {
                        JSONObject settings;
                        if (settingsFile.exists()) {
                            String content = Files.readString(settingsFile.toPath());
                            settings = new JSONObject(content);
                        } else {
                            settings = new JSONObject();
                        }

                        try (FileWriter fw = new FileWriter(settingsFile)){
                            settings.put("minRam", minRam);
                            settings.put("maxRam", maxRam);
                            fw.write(settings.toString());
                        }
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }
                }
                break;
            case "openDir":
                try {
                    if (context == null) return;
                    File launcherDir = new File(context.getLauncherDirs().launcherDir().path());
                    Desktop.getDesktop().open(launcherDir);
                } catch (IOException ex) {
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(null, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                }
                break;
            case "playPause":
                if (backgroundPanel == null) return;
                backgroundPanel.togglePlayPause();
                break;
            case "volume":
                if (backgroundPanel == null) return;
                backgroundPanel.toggleVolumeMenu();
                break;
        }
    }

    public void setPlayDependencies(JComboBox<String> versionsComboBox, JTextField usernameField, JCheckBox saveCheckBox) {
        this.versionsComboBox = versionsComboBox;
        this.usernameField = usernameField;
        this.saveCheckBox = saveCheckBox;
    }

    public void setConsoleDependencies(JTextPane consolePane) {
        this.consolePane = consolePane;
    }

    public void setSaveSettingsDependencies(JTextField minRamField, JTextField maxRamField, JTextPane minRamStatusPane, JTextPane maxRamStatusPane, JCheckBox saveSettingsCheckBox) {
        this.minRamField = minRamField;
        this.maxRamField = maxRamField;
        this.minRamStatusPane = minRamStatusPane;
        this.maxRamStatusPane = maxRamStatusPane;
        this.saveSettingsCheckBox = saveSettingsCheckBox;
    }
}
