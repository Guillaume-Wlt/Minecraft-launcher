package fr.guillaumewlt.processing.steps;

import fr.guillaumewlt.downloads.VersionJSONDownload;
import fr.guillaumewlt.exceptionhandler.LauncherException;
import fr.guillaumewlt.utils.URLUtils;

public class DownloadVersionJSONProcess extends Processes {

    @Override
    public void process() {
        try {
            String url = URLUtils.getSelectedVersionURL();
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
