package fr.guillaumewlt.processing.steps;

import fr.guillaumewlt.downloads.ManifestDownload;
import fr.guillaumewlt.exceptionhandler.LauncherException;

public class DownloadManifestProcess {

    private DownloadManifestProcess() {}

    public static void downloadManifest() {
        try {
            new ManifestDownload().download();
        } catch (LauncherException e) {
            System.err.println("Fatal Error: " + e.getMessage());
            System.exit(1);
        }
    }
}
