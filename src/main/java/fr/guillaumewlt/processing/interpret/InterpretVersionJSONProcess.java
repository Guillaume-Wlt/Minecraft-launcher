package fr.guillaumewlt.processing.interpret;

import fr.guillaumewlt.annotations.WorkerThread;
import fr.guillaumewlt.exceptions.LauncherException;
import fr.guillaumewlt.models.VersionRawData;
import fr.guillaumewlt.parser.VersionJSONParser;
import fr.guillaumewlt.processing.Processes;
import fr.guillaumewlt.workflow.LauncherContext;

public class InterpretVersionJSONProcess extends Processes {

    public InterpretVersionJSONProcess(LauncherContext context){
        super(context);
    }

    @WorkerThread
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
