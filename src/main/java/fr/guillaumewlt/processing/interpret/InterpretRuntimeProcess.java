package fr.guillaumewlt.processing.interpret;

import fr.guillaumewlt.annotations.WorkerThread;
import fr.guillaumewlt.exceptions.LauncherException;
import fr.guillaumewlt.models.RuntimeRawData;
import fr.guillaumewlt.parser.RuntimeJSONParser;
import fr.guillaumewlt.processing.Processes;
import fr.guillaumewlt.workflow.LauncherContext;

public class InterpretRuntimeProcess extends Processes {

    public InterpretRuntimeProcess(LauncherContext context) {
        super(context);
    }

    @WorkerThread
    @Override
    public void process() {
        try {
            RuntimeRawData runtimeRawData = new RuntimeJSONParser(context).jsonParser();
            context.setRuntimeRawData(runtimeRawData);
        } catch (LauncherException e) {
            stop(e.getMessage(), 1);
        }
    }
}
