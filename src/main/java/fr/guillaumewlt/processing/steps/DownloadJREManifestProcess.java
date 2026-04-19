package fr.guillaumewlt.processing.steps;

import fr.guillaumewlt.downloads.DownloadProcess;
import fr.guillaumewlt.exceptionhandler.LauncherException;
import fr.guillaumewlt.processing.CheckFoldersExistence;
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

    @Override
    public void process() {
        try {
            checkRequirements(destination);
            new DownloadProcess(context).downloadToFile(manifestURL, destination, 0, jreName, null, DownloadProcess.HashType.MD5);
        } catch (LauncherException e) {
            stop(e.getMessage(), 1);
        }
    }

    private void checkRequirements(final Path destination) {
        CheckFoldersExistence.checkDirectories(String.valueOf(destination.getParent()));
    }
}
