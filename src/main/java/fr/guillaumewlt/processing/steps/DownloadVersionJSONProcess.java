package fr.guillaumewlt.processing.steps;

import fr.guillaumewlt.downloads.DownloadProcess;
import fr.guillaumewlt.exceptionhandler.LauncherException;
import fr.guillaumewlt.processing.CheckFoldersExistence;
import fr.guillaumewlt.utils.console.ConsoleMessage;
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

    @Override
    public void process() {
        try {
            if (url == null) {
                ConsoleMessage.SELECTEDVERSION_RECORD_URL_NULL_ERR.throwException();
            }
            checkRequirements();
            new DownloadProcess(context).downloadToFile(url, destination, 0, versionName, null, DownloadProcess.HashType.MD5);

        } catch (LauncherException e) {
            stop(e.getMessage(), 1);
        }
    }

    private void checkRequirements() {
        CheckFoldersExistence.checkDirectories(selectedVersionDir);
    }
}
