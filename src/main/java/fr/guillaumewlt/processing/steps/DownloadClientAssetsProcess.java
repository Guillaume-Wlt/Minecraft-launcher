package fr.guillaumewlt.processing.steps;

import fr.guillaumewlt.downloads.AssetsObjectsDownload;
import fr.guillaumewlt.exceptionhandler.LauncherException;
import fr.guillaumewlt.workflow.LauncherContext;

public class DownloadClientAssetsProcess extends Processes{

    public DownloadClientAssetsProcess(LauncherContext context) {
        super(context);
    }

    @Override
    public void process() {
        try {
            new AssetsObjectsDownload(context).download();
        } catch (LauncherException e) {
            stop(e.getMessage(), 1);
        }
    }
}
