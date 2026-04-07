package fr.guillaumewlt;

import com.formdev.flatlaf.FlatDarkLaf;
import fr.guillaumewlt.workflow.WorkflowRunner;

import javax.swing.*;

public class Main {

    public static void main(String[] args) {
        JFrame.setDefaultLookAndFeelDecorated(true);
        FlatDarkLaf.setup(); //thème sombre
        new Thread(() -> new WorkflowRunner().run()).start(); // Start the workflow
    }
}
