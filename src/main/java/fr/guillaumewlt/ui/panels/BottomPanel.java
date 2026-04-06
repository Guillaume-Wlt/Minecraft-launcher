package fr.guillaumewlt.ui.panels;

import fr.guillaumewlt.ui.builders.PanelBuilder;
import fr.guillaumewlt.ui.eventhandler.PlayBtnHandler;
import fr.guillaumewlt.workflow.LauncherContext;

import javax.swing.*;
import java.awt.*;
import java.util.List;
import java.util.concurrent.CountDownLatch;

public class BottomPanel extends JPanel {

    private final LauncherContext context;
    private final CountDownLatch latch;

    private JComboBox<String> versionCombo;
    private JButton playBtn;
    private JTextField usernameField;
    private final JProgressBar progressBar;

    public BottomPanel(LauncherContext context, CountDownLatch latch) {
        super(new BorderLayout(0, 6));

        this.context = context;
        this.latch = latch;

        setBorder(BorderFactory.createEmptyBorder(8, 12, 8, 12));

        versionCombo  = new JComboBox<>();
        playBtn       = new JButton("PLAY");
        usernameField = new JTextField(15);

        populateVersionCombo();

        playBtn.putClientProperty("JButton.buttonType", "roundRect");
        playBtn.setFont(playBtn.getFont().deriveFont(Font.BOLD, 13f));
        playBtn.setPreferredSize(new Dimension(160, 36));
        playBtn.addActionListener(new PlayBtnHandler(context, latch, versionCombo, usernameField));

        progressBar = new JProgressBar(0, 100);
        progressBar.setStringPainted(true);
        progressBar.setString("");
        progressBar.setPreferredSize(new Dimension(0, 22));
        progressBar.setVisible(false);

        add(progressBar,    BorderLayout.NORTH);
        add(buildMainRow(), BorderLayout.CENTER);

    }

    // ----------------------------------------------------------LaunchBar

    private JPanel buildMainRow() {
        JPanel row = new JPanel(new GridLayout(1, 3, 8, 0));
        row.add(buildVersionSection());
        row.add(buildPlaySection());
        row.add(buildUsernameSection());
        return row;
    }

    private JPanel buildVersionSection() {
        JPanel inner = new JPanel(new BorderLayout(0, 2));
        inner.add(new JLabel("Version :"), BorderLayout.NORTH);
        inner.add(versionCombo,                 BorderLayout.CENTER);
        JPanel wrapper = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        wrapper.add(inner);
        return wrapper;
    }

    private void populateVersionCombo() {
        List<String> versions = context.getVersions();
        for (String version : versions) {
            String v = version.toLowerCase();
            if (!Character.isDigit(v.charAt(0))) continue; // Si la version de commence pas par un Chiffre -> Skip
            if (v.contains("w") || v.contains("snapshot") || v.contains("pre") || v.contains("rc")) continue; // Si la version contient ces mentions -> Skip
            versionCombo.addItem(version);
        }
    }

    private JPanel buildPlaySection() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.add(playBtn);
        return panel;
    }

    private JPanel buildUsernameSection() {
        JPanel inner = new JPanel(new BorderLayout(0, 2));
        inner.add(new JLabel("Username :"), BorderLayout.NORTH);
        inner.add(usernameField,                 BorderLayout.CENTER);
        JPanel wrapper = new JPanel(new FlowLayout(FlowLayout.RIGHT, 0, 0));
        wrapper.add(inner);
        return wrapper;
    }

    // ----------------------------------------------------------ProgressBar

    public JPanel buildProgressBar() {
        JPanel progressBarPanel = PanelBuilder.builder()
                .visible(false)
                .build();
        return progressBarPanel;
    }
}
