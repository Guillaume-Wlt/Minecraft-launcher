package fr.guillaumewlt.processing.download;

import fr.guillaumewlt.annotations.Retryable;
import fr.guillaumewlt.annotations.WorkerThread;
import fr.guillaumewlt.download.DownloadProcess;
import fr.guillaumewlt.exceptions.LauncherException;
import fr.guillaumewlt.processing.Processes;
import fr.guillaumewlt.utils.ConstantUtils;
import fr.guillaumewlt.workflow.LauncherContext;

import java.nio.file.Path;

public class DownloadManifestProcess extends Processes {

    private final String url;
    private final String manifestName;
    private final Path manifestPath;

    public DownloadManifestProcess(LauncherContext context) {
        super(context);
        url = ConstantUtils.MANIFEST_URL;
        manifestName = ConstantUtils.MANIFEST_NAME;
        manifestPath = Path.of(context.getLauncherDirs().versionsDir().path(), manifestName);
    }

    @Retryable(attempts = 3)
    @WorkerThread
    @Override
    public void process() {
        try {
            new DownloadProcess(context).downloadMD5(url, manifestPath);
        } catch (LauncherException ex) {
            stop(ex.getMessage(), 1);
        }
    }
}
