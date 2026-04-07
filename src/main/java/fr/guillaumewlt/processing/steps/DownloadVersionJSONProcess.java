package fr.guillaumewlt.processing.steps;

import fr.guillaumewlt.downloads.VersionJSONDownload;
import fr.guillaumewlt.exceptionhandler.LauncherException;
import fr.guillaumewlt.utils.console.ConsoleMessage;
import fr.guillaumewlt.workflow.LauncherContext;

public class DownloadVersionJSONProcess extends Processes {

    public DownloadVersionJSONProcess(LauncherContext context) {
        super(context);
    }

    @Override
    public void process() {
        try {
            if (context.getSelectedVersion() == null) {
                ConsoleMessage.SELECTEDVERSION_RECORD_NULL_ERR.throwException();
            }

            String url = context.getSelectedVersion().url();
            if (url == null) {
                ConsoleMessage.SELECTEDVERSION_RECORD_URL_NULL_ERR.throwException();
            }
            new VersionJSONDownload(context, url).download();

        } catch (LauncherException e) {
            stop(e.getMessage(), 1);
        }
    }
}
