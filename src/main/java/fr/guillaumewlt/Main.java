package fr.guillaumewlt;

import com.formdev.flatlaf.FlatDarkLaf;
import fr.guillaumewlt.exceptions.LauncherException;
import fr.guillaumewlt.workflow.WorkflowRunner;

import javax.swing.*;

public class Main {

    public static void main(String[] args) {
        JFrame.setDefaultLookAndFeelDecorated(true);
        FlatDarkLaf.setup(); //thème sombre
        new Thread(() -> {
            try {
                new WorkflowRunner().run();
            } catch (LauncherException ex) {
                System.err.println(ex.getMessage());
                System.exit(ex.getExitCode());
            }
        }).start(); // Start the workflow
    }
}
