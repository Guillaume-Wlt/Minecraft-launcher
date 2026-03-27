package fr.guillaumewlt.processing.steps;

import fr.guillaumewlt.exceptionhandler.LauncherException;
import fr.guillaumewlt.model.VersionRawData;
import fr.guillaumewlt.parser.VersionJSONParser;
import fr.guillaumewlt.workflow.LauncherContext;

public class InterpretVersionJSONProcess extends Processes {

    public InterpretVersionJSONProcess(LauncherContext context){
        super(context);
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
