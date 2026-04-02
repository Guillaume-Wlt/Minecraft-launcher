package fr.guillaumewlt.processing.steps;

import fr.guillaumewlt.downloads.JREFilesDownload;
import fr.guillaumewlt.exceptionhandler.LauncherException;
import fr.guillaumewlt.workflow.LauncherContext;

public class DownloadJREFilesProcess extends Processes{

    public DownloadJREFilesProcess(LauncherContext context) {
        super(context);
    }

    @Override
    public void process() {
        try {
            new JREFilesDownload(context).download();
        } catch (LauncherException e) {
            stop(e.getMessage(), 1);
        }
    }
}
