package fr.guillaumewlt.processing.steps;

import fr.guillaumewlt.exceptionhandler.LauncherException;
import fr.guillaumewlt.model.RuntimeRawData;
import fr.guillaumewlt.parser.RuntimeJSONParser;
import fr.guillaumewlt.workflow.LauncherContext;

public class InterpretRuntimeProcess extends Processes {

    public InterpretRuntimeProcess(LauncherContext context) {
        super(context);
    }

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
