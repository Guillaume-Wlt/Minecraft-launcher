package fr.guillaumewlt.processing.download;

import fr.guillaumewlt.annotations.Retryable;
import fr.guillaumewlt.annotations.WorkerThread;
import fr.guillaumewlt.download.DownloadProcess;
import fr.guillaumewlt.exceptions.LauncherException;
import fr.guillaumewlt.processing.Processes;
import fr.guillaumewlt.utils.DirectoryCreatorUtils;
import fr.guillaumewlt.workflow.LauncherContext;

import java.nio.file.Path;

public class DownloadJREManifestProcess extends Processes {

    private final String manifestURL;
    private final String jreName;
    private final Path destination;

    public DownloadJREManifestProcess(LauncherContext context) {
        super(context);
        this.manifestURL = context.getRuntimeRawData().url();
        this.jreName = context.getRuntimeRawData().name();
        this.destination = Path.of(context.getLauncherDirs().runtimeDir().path(), jreName, jreName + ".json");
    }

    @Retryable(attempts = 3)
    @WorkerThread
    @Override
    public void process() {
        try {
            checkRequirements(destination);
            new DownloadProcess(context).downloadMD5(manifestURL, destination);
        } catch (LauncherException e) {
            stop(e.getMessage(), 1);
        }
    }

    private void checkRequirements(final Path destination) {
        DirectoryCreatorUtils.checkDirectories(String.valueOf(destination.getParent()));
    }
}
