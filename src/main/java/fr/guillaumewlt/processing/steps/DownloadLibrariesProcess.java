package fr.guillaumewlt.processing.steps;

import fr.guillaumewlt.downloads.LibrariesDownload;
import fr.guillaumewlt.exceptionhandler.LauncherException;
import fr.guillaumewlt.workflow.LauncherContext;

public class DownloadLibrariesProcess extends Processes {

    public DownloadLibrariesProcess(LauncherContext context) {
        super(context);
    }

    @Override
    protected void process() {
        try {
            new LibrariesDownload().download();
        } catch (LauncherException e) {
            stop(e.getMessage(), 1);
        }
    }
}
