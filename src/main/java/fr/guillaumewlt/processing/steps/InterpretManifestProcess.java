package fr.guillaumewlt.processing.steps;

import fr.guillaumewlt.exceptionhandler.LauncherException;
import fr.guillaumewlt.model.SelectedVersion;
import fr.guillaumewlt.parser.ManifestParser;
import fr.guillaumewlt.workflow.LauncherContext;

public class InterpretManifestProcess extends Processes {

    private final LauncherContext context;

    public InterpretManifestProcess(LauncherContext context) {
        this.context = context;
    }

    @Override
    public void process() {
        try {
            SelectedVersion version = new ManifestParser(context).jsonparser();
            context.setSelectedVersion(version);
        } catch (LauncherException e) {
            stop(e.getMessage(), 1);
        }
    }
}
