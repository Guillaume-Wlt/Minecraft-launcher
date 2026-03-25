package fr.guillaumewlt.processing.steps;

import fr.guillaumewlt.downloads.ClientDownload;
import fr.guillaumewlt.exceptionhandler.LauncherException;

public class DownloadClientJarProcess extends Processes{

    @Override
    public void process() {
        try {
            new ClientDownload().download();
        } catch (LauncherException e) {
            System.err.println("Fatal Error: " + e.getMessage());
            System.exit(1);
        }
    }
}
