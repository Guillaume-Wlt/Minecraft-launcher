package fr.guillaumewlt.processing.steps;

import fr.guillaumewlt.exceptionhandler.LauncherException;
import fr.guillaumewlt.parser.VersionJSONParser;

public class InterpretVersionJSONProcess {

    private InterpretVersionJSONProcess() {}

    public static void interpVersionJSON() {
        try {
            new VersionJSONParser();
        } catch (LauncherException e) {
            System.err.println("Fatal Error: " + e.getMessage());
            System.exit(1);
        }
    }
}
