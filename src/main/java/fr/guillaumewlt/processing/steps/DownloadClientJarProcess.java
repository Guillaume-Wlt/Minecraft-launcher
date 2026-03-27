package fr.guillaumewlt.processing.steps;

import fr.guillaumewlt.downloads.ClientJarDownload;
import fr.guillaumewlt.exceptionhandler.LauncherException;
import fr.guillaumewlt.workflow.LauncherContext;

public class DownloadClientJarProcess extends Processes{

    private final LauncherContext context;

    public DownloadClientJarProcess(LauncherContext context) {
        this.context = context;
    }

    @Override
    public void process() {
        try {
            new ClientJarDownload(context).download();
        } catch (LauncherException e) {
            stop(e.getMessage(), 1);
        }
    }
}
