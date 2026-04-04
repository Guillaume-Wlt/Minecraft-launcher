package fr.guillaumewlt.ui;

import fr.guillaumewlt.ui.builders.WindowBuilder;
import fr.guillaumewlt.ui.components.MenuBar;

import javax.swing.*;

public class MainWindow {
    public void show() {
        JFrame frame = WindowBuilder.builder()
                .title("Minecraft Launcher")
                .size(900,550)
                .closeOperation(JFrame.EXIT_ON_CLOSE)
                .icon("/launcher-logo.png")
                .alwaysOnTop(true)
                .resizable(false)
                .build();
        frame.setJMenuBar(new MenuBar(frame));
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
}
