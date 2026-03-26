package fr.guillaumewlt.processing.steps;

import fr.guillaumewlt.exceptionhandler.LauncherException;
import fr.guillaumewlt.parser.ClientJarInfosParser;

public class InterpretClientJarInfos extends Processes{

    @Override
    public void process() {
        try {
            new ClientJarInfosParser().jsonParser();
        } catch (LauncherException e) {
            stop(e.getMessage(), 1);
        }
    }
}
