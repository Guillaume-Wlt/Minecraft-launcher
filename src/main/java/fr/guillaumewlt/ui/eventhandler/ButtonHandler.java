package fr.guillaumewlt.ui.eventhandler;

import fr.guillaumewlt.model.SelectedVersion;
import fr.guillaumewlt.parser.ManifestParser;
import fr.guillaumewlt.utils.ProgressBarUtils;
import fr.guillaumewlt.workflow.LauncherContext;
import lombok.Setter;

import javax.swing.*;
import javax.swing.text.BadLocationException;
import java.awt.*;
import java.awt.datatransfer.StringSelection;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.concurrent.CountDownLatch;

public class ButtonHandler implements ActionListener {

    @Setter
    private LauncherContext context;
    @Setter
    private CountDownLatch latch;

    private JComboBox<String> versionsComboBox;
    private JTextField usernameField;
    private JTextPane consolePane;

    @Override
    public void actionPerformed(ActionEvent e) {
        JButton button = (JButton) e.getSource();
        String actionCommand = button.getActionCommand();
        switch (actionCommand) {
            case "playBtn":
                try {
                    if (context == null || latch == null) return;
                    if (versionsComboBox == null || usernameField == null) return;
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

                    ProgressBarUtils.show();
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
        }
    }

    public void setPlayDependencies(JComboBox<String> versionsComboBox, JTextField usernameField) {
        this.versionsComboBox = versionsComboBox;
        this.usernameField = usernameField;
    }

    public void setConsoleDependencies(JTextPane consolePane) {
        this.consolePane = consolePane;
    }
}
