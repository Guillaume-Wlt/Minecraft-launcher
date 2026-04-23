package fr.guillaumewlt.processing.interpret;

import fr.guillaumewlt.annotations.WorkerThread;
import fr.guillaumewlt.exceptions.LauncherException;
import fr.guillaumewlt.parser.ManifestParser;
import fr.guillaumewlt.processing.Processes;
import fr.guillaumewlt.workflow.LauncherContext;

import java.util.List;

public class InterpretManifestProcess extends Processes {

    public InterpretManifestProcess(LauncherContext context) {
        super(context);
    }

    @WorkerThread
    @Override
    public void process() {
        try {
            List<String> versions = new ManifestParser(context).getVersions();
            context.setVersions(versions);
        } catch (LauncherException e) {
            stop(e.getMessage(), 1);
        }
    }
}
