package fr.guillaumewlt.processing.steps;

import fr.guillaumewlt.exceptionhandler.LauncherException;
import fr.guillaumewlt.parser.VersionJSONParser;

public class InterpretVersionJSONProcess extends Processes {

    @Override
    public void process() {
        try {
            new VersionJSONParser().jsonParser();
        } catch (LauncherException e) {
            stop(e.getMessage(), 1);
        }
    }
}
