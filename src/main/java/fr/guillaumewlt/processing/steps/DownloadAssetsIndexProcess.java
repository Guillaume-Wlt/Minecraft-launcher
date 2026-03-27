package fr.guillaumewlt.processing.steps;

import fr.guillaumewlt.downloads.AssetsIndexDownload;
import fr.guillaumewlt.exceptionhandler.LauncherException;
import fr.guillaumewlt.workflow.LauncherContext;

public class DownloadAssetsIndexProcess extends Processes{

    public DownloadAssetsIndexProcess(LauncherContext context) {
        super(context);
    }

    @Override
    public void process() {
        try {
            new AssetsIndexDownload(context).download();
        } catch (LauncherException e) {
            stop(e.getMessage(), 1);
        }
    }
}
