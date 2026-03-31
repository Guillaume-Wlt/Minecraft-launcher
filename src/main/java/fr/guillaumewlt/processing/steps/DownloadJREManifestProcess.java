package fr.guillaumewlt.processing.steps;

import fr.guillaumewlt.downloads.JREManifestDownload;
import fr.guillaumewlt.exceptionhandler.LauncherException;
import fr.guillaumewlt.workflow.LauncherContext;

public class DownloadJREManifestProcess extends Processes {

    public DownloadJREManifestProcess(LauncherContext context) {
        super(context);
    }

    @Override
    public void process() {
        try {
            new JREManifestDownload(context).download();
        } catch (LauncherException e) {
            stop(e.getMessage(), 1);
        }
    }
}
