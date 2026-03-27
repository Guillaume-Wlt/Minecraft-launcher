package fr.guillaumewlt.processing.steps;

import fr.guillaumewlt.downloads.ManifestDownload;
import fr.guillaumewlt.exceptionhandler.LauncherException;
import fr.guillaumewlt.workflow.LauncherContext;

public class DownloadManifestProcess extends Processes {

    private final LauncherContext context;

    public DownloadManifestProcess(LauncherContext context) {
        this.context = context;
    }

    @Override
    public void process() {
        try {
            new ManifestDownload(context).download();
        } catch (LauncherException e) {
            stop(e.getMessage(), 1);
        }
    }
}
