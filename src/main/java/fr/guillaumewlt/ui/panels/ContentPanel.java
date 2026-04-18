package fr.guillaumewlt.ui.panels;

import fr.guillaumewlt.workflow.LauncherContext;

import javax.swing.*;
import java.awt.*;

public class ContentPanel extends JPanel {

    private final LauncherContext context;

    public ContentPanel(LauncherContext context) {
        this.context = context;
        setLayout(new BorderLayout());

        BackgroundPanel backgroundPanel = new BackgroundPanel(context);
        context.setBackgroundPanel(backgroundPanel);
        add(backgroundPanel, BorderLayout.CENTER);

        BottomPanel bottomPanel = new BottomPanel(context);
        context.setBottomPanel(bottomPanel);
        add(bottomPanel, BorderLayout.SOUTH);
    }
}
