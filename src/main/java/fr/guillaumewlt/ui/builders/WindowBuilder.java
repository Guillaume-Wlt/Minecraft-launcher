package fr.guillaumewlt.ui.builders;

import fr.guillaumewlt.exceptions.LauncherException;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowListener;
import java.net.URL;

public class WindowBuilder {

    private WindowBuilder() {}

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private String title = "";
        private int width = 800; // default value
        private int height = 600; // default value
        private boolean alwaysOnTop = false;
        private boolean resizable = false;
        private int closeOperation = JFrame.EXIT_ON_CLOSE;
        private String iconPath = null;
        private JMenuBar menuBar;
        private JComponent content;
        public WindowListener windowListener = null;


        public Builder title(String title) {
            this.title = title;
            return this;
        }

        public Builder size(int width, int height) {
            if (width <= 0 || height <= 0) throw new LauncherException("width and height must be positive");
            this.width = width;
            this.height = height;
            return this;
        }

        public Builder alwaysOnTop(boolean alwaysOnTop) {
            this.alwaysOnTop = alwaysOnTop;
            return this;
        }

        public Builder resizable(boolean resizable) {
            this.resizable = resizable;
            return this;
        }

        public Builder closeOperation(int closeOperation) {
            this.closeOperation = closeOperation;
            return this;
        }

        public Builder icon(String iconPath) {
            if (iconPath == null) throw new LauncherException("icon path cannot be null");
            this.iconPath = iconPath;
            return this;
        }

        public Builder menuBar(JMenuBar menuBar) {
            this.menuBar = menuBar;
            return this;
        }

        public Builder content(JComponent content) {
            this.content = content;
            return this;
        }

        public Builder windowListener(WindowListener windowListener) {
            this.windowListener = windowListener;
            return this;
        }

        public JFrame build() {
            JFrame frame = new JFrame(title);
            frame.setSize(width, height);
            frame.setResizable(resizable);
            frame.setAlwaysOnTop(alwaysOnTop);
            frame.setDefaultCloseOperation(closeOperation);
            if (iconPath != null) {
                URL iconURL = getClass().getResource(iconPath);
                if (iconURL != null) {
                    Image image = new ImageIcon(iconURL).getImage()
                            .getScaledInstance(32, 32, Image.SCALE_SMOOTH);
                    frame.setIconImage(image);
                } else {
                    System.err.println("Icon resource not found: " + iconPath);
                }
            }

            if (menuBar != null) {
                frame.setJMenuBar(menuBar);
            }

            if (content != null) {
                frame.setContentPane(content);
            }

            if (windowListener != null) {
                frame.addWindowListener(windowListener);
            }

            return frame;
        }

        public JFrame buildAndShow() {
            JFrame frame = build();
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
            return frame;
        }
    }
}
