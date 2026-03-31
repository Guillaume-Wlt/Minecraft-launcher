package fr.guillaumewlt.processing.steps;

import fr.guillaumewlt.downloads.RuntimeJSONDownload;
import fr.guillaumewlt.exceptionhandler.LauncherException;
import fr.guillaumewlt.workflow.LauncherContext;

public class DownloadRuntimeJSONProcess extends Processes{

    public DownloadRuntimeJSONProcess(LauncherContext context) {
        super(context);
    }

    @Override
    public void process() {
        try {
            new RuntimeJSONDownload(context).download();
        } catch (LauncherException e) {
            stop(e.getMessage(), 1);
        }
    }
}
