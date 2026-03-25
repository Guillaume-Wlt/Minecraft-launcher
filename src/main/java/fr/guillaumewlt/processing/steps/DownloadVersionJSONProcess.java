package fr.guillaumewlt.processing.steps;

import fr.guillaumewlt.downloads.VersionJSONDownload;
import fr.guillaumewlt.exceptionhandler.LauncherException;
import fr.guillaumewlt.utils.LauncherUtils;

public class DownloadVersionJSONProcess {

    private DownloadVersionJSONProcess() {}

    public static void downloadVersionJSON() {
        try {
            String url = LauncherUtils.getVersionUrl();
            if (url != null) {
                new VersionJSONDownload(url).download();
            } else {
                throw new LauncherException("No version url found");
            }
        } catch (LauncherException e) {
            System.err.println("Fatal Error: " + e.getMessage());
            System.exit(1);
        }
    }
}
