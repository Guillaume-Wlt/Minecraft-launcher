package fr.guillaumewlt.processing.steps;

import fr.guillaumewlt.downloads.DownloadProcess;
import fr.guillaumewlt.exceptionhandler.LauncherException;
import fr.guillaumewlt.utils.LauncherUtils;
import fr.guillaumewlt.workflow.LauncherContext;

import java.nio.file.Path;

public class DownloadManifestProcess extends Processes {

    private final String url;
    private final String manifestName;
    private final Path manifestPath;

    public DownloadManifestProcess(LauncherContext context) {
        super(context);
        url = LauncherUtils.getManifestURL();
        manifestName = LauncherUtils.getManifestName();
        manifestPath = Path.of(context.getLauncherDirs().versionsDir().path(), manifestName);
    }

    @Override
    public void process() {
        try {
            new DownloadProcess(context).downloadToFile(url, manifestPath, 0, manifestName, null, DownloadProcess.HashType.MD5);
        } catch (LauncherException ex) {
            stop(ex.getMessage(), 1);
        }
    }
}
