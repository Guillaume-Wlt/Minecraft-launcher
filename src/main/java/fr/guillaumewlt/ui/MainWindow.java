package fr.guillaumewlt.ui;

import fr.guillaumewlt.ui.builders.WindowBuilder;
import fr.guillaumewlt.ui.components.MenuBar;
import fr.guillaumewlt.ui.eventhandler.MainWindowsHandler;
import fr.guillaumewlt.ui.panels.ContentPanel;
import fr.guillaumewlt.workflow.LauncherContext;

import javax.swing.*;
import java.util.concurrent.CountDownLatch;

public class MainWindow {

    private final LauncherContext context;
    private final CountDownLatch latch;

    public MainWindow(LauncherContext context, CountDownLatch latch) {
        this.context = context;
        this.latch = latch;
    }

    public void show() {
        JFrame frame = WindowBuilder.builder()
                .title("Minecraft Launcher")
                .size(1280,720)
                .closeOperation(JFrame.EXIT_ON_CLOSE)
                .icon("/launcher-logo.png")
                .alwaysOnTop(false)
                .resizable(false)
                .content(new ContentPanel(context, latch))
                .windowListener(new MainWindowsHandler())
                .build();
        frame.setJMenuBar(new MenuBar(context, frame));
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
}
