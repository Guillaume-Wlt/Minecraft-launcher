package fr.guillaumewlt.ui;

import fr.guillaumewlt.ui.builders.WindowBuilder;
import fr.guillaumewlt.ui.components.MenuBar;
import fr.guillaumewlt.ui.eventhandler.WindowsListener;
import fr.guillaumewlt.ui.panels.ContentPanel;
import fr.guillaumewlt.workflow.LauncherContext;

import javax.swing.*;

public class MainWindow {

    private final LauncherContext context;

    public MainWindow(LauncherContext context) {
        this.context = context;
    }

    public JFrame show() {
        JFrame frame = WindowBuilder.builder()
                .title("Minecraft Launcher")
                .size(1280,720)
                .closeOperation(JFrame.EXIT_ON_CLOSE)
                .icon("/launcher-logo.png")
                .alwaysOnTop(false)
                .resizable(false)
                .content(new ContentPanel(context))
                .windowListener(new WindowsListener())
                .build();
        frame.setJMenuBar(new MenuBar(context, frame));
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
        return frame;
    }
}
