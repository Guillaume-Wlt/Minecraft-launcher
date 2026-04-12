package fr.guillaumewlt.ui.panels;

import fr.guillaumewlt.workflow.LauncherContext;

import javax.swing.*;
import java.awt.*;
import java.util.concurrent.CountDownLatch;

public class ContentPanel extends JPanel {

    private final LauncherContext context;
    private final CountDownLatch latch;

    public ContentPanel(LauncherContext context, CountDownLatch latch) {
        this.context = context;
        this.latch = latch;

        setLayout(new BorderLayout());

        BackgroundPanel backgroundPanel = new BackgroundPanel();
        context.setBackgroundPanel(backgroundPanel);
        add(backgroundPanel, BorderLayout.CENTER);

        add(new BottomPanel(context, latch), BorderLayout.SOUTH);
    }
}
