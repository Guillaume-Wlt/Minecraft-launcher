package fr.guillaumewlt.processing.steps;

import fr.guillaumewlt.exceptions.LauncherException;
import fr.guillaumewlt.extractor.NativesLibsExtract;
import fr.guillaumewlt.processing.Processes;
import fr.guillaumewlt.workflow.LauncherContext;

public class ExtractNativesLibrariesProcess extends Processes {

    public ExtractNativesLibrariesProcess(LauncherContext context) {
        super(context);
    }

    @Override
    public void process() {
        try {
            new NativesLibsExtract(context).jarExtract();
        } catch (LauncherException e) {
            stop(e.getMessage(), 1);
        }
    }
}
