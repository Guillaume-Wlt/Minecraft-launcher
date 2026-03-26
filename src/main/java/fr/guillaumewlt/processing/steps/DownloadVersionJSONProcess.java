package fr.guillaumewlt.processing.steps;

import fr.guillaumewlt.downloads.VersionJSONDownload;
import fr.guillaumewlt.exceptionhandler.LauncherException;
import fr.guillaumewlt.utils.URLUtils;
import fr.guillaumewlt.utils.console.ConsoleMessage;

public class DownloadVersionJSONProcess extends Processes {

    @Override
    public void process() {
        try {
            String url = URLUtils.getSelectedVersionURL();
            if (url != null) {
                new VersionJSONDownload(url).download();
            } else {
                throw new LauncherException(ConsoleMessage.URL_UTILS_SELECTED_VERSION_URL_NULL_ERR.getMessage());
            }
        } catch (LauncherException e) {
            stop(e.getMessage(), 1);
        }
    }
}
