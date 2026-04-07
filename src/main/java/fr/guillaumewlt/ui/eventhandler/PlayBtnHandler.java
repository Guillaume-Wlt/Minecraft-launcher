package fr.guillaumewlt.ui.eventhandler;

import fr.guillaumewlt.model.SelectedVersion;
import fr.guillaumewlt.parser.ManifestParser;
import fr.guillaumewlt.utils.ProgressBarUtils;
import fr.guillaumewlt.workflow.LauncherContext;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.concurrent.CountDownLatch;

public class PlayBtnHandler implements ActionListener {

    private final LauncherContext context;
    private final CountDownLatch latch;

    private JComboBox<String> combo;
    private JTextField field;

    public PlayBtnHandler(LauncherContext context, CountDownLatch latch, JComboBox<String> combo, JTextField field) {
        this.context = context;
        this.latch = latch;
        this.combo = combo;
        this.field = field;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        try {
            String version = combo.getSelectedItem().toString();
            String username = field.getText().trim();

            if (username.isEmpty()) {
                JOptionPane.showMessageDialog(null, "Please enter a valid username", "Warning", JOptionPane.WARNING_MESSAGE);
                return;
            }

            SelectedVersion selectedVersion = new ManifestParser(context).jsonparser(version);
            context.setSelectedVersion(selectedVersion);

            context.setUsername(username);

            JButton btn = (JButton) e.getSource();
            btn.setEnabled(false);
            btn.setText("LAUNCHED");

            field.setEnabled(false);
            combo.setEnabled(false);

            System.out.println("Selected version: " + selectedVersion.selectedVersion());
            System.out.println("Username : " + context.getUsername());

            ProgressBarUtils.show();
            latch.countDown();
        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(null, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
