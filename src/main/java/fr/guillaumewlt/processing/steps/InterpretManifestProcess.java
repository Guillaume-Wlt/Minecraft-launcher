package fr.guillaumewlt.processing.steps;

import fr.guillaumewlt.exceptionhandler.LauncherException;
import fr.guillaumewlt.parser.ManifestParser;

public class InterpretManifestProcess extends Processes {

    @Override
    public void process() {
        try {
            new ManifestParser().jsonParser();
        } catch (LauncherException e) {
            stop(e.getMessage(), 1);
        }
    }
}
