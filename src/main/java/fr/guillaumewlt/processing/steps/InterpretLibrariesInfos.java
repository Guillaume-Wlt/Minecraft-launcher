package fr.guillaumewlt.processing.steps;

import fr.guillaumewlt.exceptionhandler.LauncherException;
import fr.guillaumewlt.model.LibraryInfos;
import fr.guillaumewlt.parser.LibrariesInfosParser;
import fr.guillaumewlt.workflow.LauncherContext;

import java.util.List;

public class InterpretLibrariesInfos extends Processes{

    public InterpretLibrariesInfos(LauncherContext context) {
        super(context);
    }

    @Override
    public void process() {
        try {
            List<LibraryInfos> librariesInfos = new LibrariesInfosParser(context).jsonParser();
            context.setLibrariesInfos(librariesInfos);
        } catch (LauncherException e) {
            stop(e.getMessage(), 1);
        }
    }
}
