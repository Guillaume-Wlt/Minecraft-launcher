package fr.guillaumewlt.processing.steps;

import fr.guillaumewlt.downloads.ManifestDownload;
import fr.guillaumewlt.exceptionhandler.LauncherException;

public class DownloadManifestProcess extends Processes {

    @Override
    public void process() {
        try {
            new ManifestDownload().download();
        } catch (LauncherException e) {
            stop(e.getMessage(), 1);
        }
    }
}
