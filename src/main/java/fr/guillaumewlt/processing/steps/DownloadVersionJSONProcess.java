package fr.guillaumewlt.processing.steps;

import fr.guillaumewlt.downloads.VersionJSONDownload;
import fr.guillaumewlt.exceptionhandler.LauncherException;
import fr.guillaumewlt.utils.console.ConsoleMessage;
import fr.guillaumewlt.workflow.LauncherContext;

public class DownloadVersionJSONProcess extends Processes {

    private final LauncherContext context;

    public DownloadVersionJSONProcess(LauncherContext context) {
        this.context = context;
    }

    @Override
    public void process() {
        try {
            String url = context.getSelectedVersion().url();
            if (url != null) {
                new VersionJSONDownload(context, url).download();
            } else {
                throw new LauncherException(ConsoleMessage.URL_UTILS_SELECTED_VERSION_URL_NULL_ERR.getMessage());
            }
        } catch (LauncherException e) {
            stop(e.getMessage(), 1);
        }
    }
}
