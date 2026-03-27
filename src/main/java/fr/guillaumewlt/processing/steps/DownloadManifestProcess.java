package fr.guillaumewlt.processing.steps;

import fr.guillaumewlt.downloads.ManifestDownload;
import fr.guillaumewlt.exceptionhandler.LauncherException;
import fr.guillaumewlt.workflow.LauncherContext;

public class DownloadManifestProcess extends Processes {

    public DownloadManifestProcess(LauncherContext context) {
        super(context);
    }

    @Override
    public void process() {
        try {
            new ManifestDownload().download();
        } catch (LauncherException e) {
            stop(e.getMessage(), 1);
        }
    }
}
