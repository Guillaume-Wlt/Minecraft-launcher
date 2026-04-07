package fr.guillaumewlt.utils;

import javax.swing.*;

public class ProgressBarUtils {

    private static JProgressBar progressBar;

    public static void register(JProgressBar bar) {
        progressBar = bar;
    }

    public static void show() {
        if (progressBar == null) return;
        // Accède à l'EDT avec "SwingUtilities"
        SwingUtilities.invokeLater(() -> progressBar.setVisible(true));
    }
    public static void hideAndReset() {
        if (progressBar == null) return;
        SwingUtilities.invokeLater(() -> {
            progressBar.setVisible(false);
            progressBar.setValue(0);
            progressBar.setString("");
        });
    }
    public static void update(int value, String label) {
        if (progressBar == null) return;
        SwingUtilities.invokeLater(() -> {
            progressBar.setValue(value);
            progressBar.setString(label + " — " + value + "%");
        });
    }
}
