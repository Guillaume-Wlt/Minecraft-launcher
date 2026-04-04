package fr.guillaumewlt;

import com.formdev.flatlaf.FlatDarkLaf;
import fr.guillaumewlt.ui.MainWindow;

import javax.swing.*;

public class Main {

    public static void main(String[] args) {
        // new WorkflowRunner().run();
        JFrame.setDefaultLookAndFeelDecorated(true);
        FlatDarkLaf.setup(); //thème sombre
        SwingUtilities.invokeLater(() -> new MainWindow().show());
    }
}
