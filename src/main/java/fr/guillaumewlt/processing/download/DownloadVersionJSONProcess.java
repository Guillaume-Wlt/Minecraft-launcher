package fr.guillaumewlt.processing.download;

import fr.guillaumewlt.annotations.Retryable;
import fr.guillaumewlt.annotations.WorkerThread;
import fr.guillaumewlt.console.ConsoleMessage;
import fr.guillaumewlt.download.DownloadProcess;
import fr.guillaumewlt.exceptions.LauncherException;
import fr.guillaumewlt.processing.Processes;
import fr.guillaumewlt.utils.DirectoryCreatorUtils;
import fr.guillaumewlt.workflow.LauncherContext;

import java.nio.file.Path;

public class DownloadVersionJSONProcess extends Processes {

    private final String url;
    private final String versionName;
    private final String selectedVersionDir;
    private final Path destination;

    public DownloadVersionJSONProcess(LauncherContext context) {
        super(context);
        this.url = context.getSelectedVersion().url();
        this.versionName = context.getSelectedVersion().selectedVersion();
        this.selectedVersionDir = context.getLauncherDirs().versionsDir().path() + versionName + "/";
        this.destination = Path.of(selectedVersionDir, versionName + ".json");
    }

    @Retryable(attempts = 3)
    @WorkerThread
    @Override
    public void process() {
        try {
            if (url == null) {
                ConsoleMessage.SELECTEDVERSION_RECORD_URL_NULL_ERR.throwException();
            }
            checkRequirements();
            new DownloadProcess(context).downloadMD5(url, destination);

        } catch (LauncherException e) {
            stop(e.getMessage(), 1);
        }
    }

    private void checkRequirements() {
        DirectoryCreatorUtils.checkDirectories(selectedVersionDir);
    }
}
