package fr.guillaumewlt.processing.steps;

import fr.guillaumewlt.exceptionhandler.LauncherException;
import fr.guillaumewlt.model.VersionRawData;
import fr.guillaumewlt.parser.VersionJSONParser;
import fr.guillaumewlt.workflow.LauncherContext;

public class InterpretVersionJSONProcess extends Processes {

    private final LauncherContext context;

    public InterpretVersionJSONProcess(LauncherContext context){
        this.context = context;
    }

    @Override
    public void process() {
        try {
            VersionRawData rawData = new VersionJSONParser(context).jsonParser();
            context.setVersionRawData(rawData);
        } catch (LauncherException e) {
            stop(e.getMessage(), 1);
        }
    }
}
