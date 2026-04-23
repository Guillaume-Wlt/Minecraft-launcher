package fr.guillaumewlt.processing.download;

import fr.guillaumewlt.annotations.Retryable;
import fr.guillaumewlt.annotations.WorkerThread;
import fr.guillaumewlt.download.DownloadProcess;
import fr.guillaumewlt.exceptions.LauncherException;
import fr.guillaumewlt.processing.Processes;
import fr.guillaumewlt.utils.DirectoryCreatorUtils;
import fr.guillaumewlt.workflow.LauncherContext;

import java.nio.file.Path;

public class DownloadAssetsIndexProcess extends Processes {

    private final String url;
    private final long size;
    private final String exceptedHash;
    private final String assetsIndexName;

    private final Path destination;

    public DownloadAssetsIndexProcess(LauncherContext context) {
        super(context);
        this.url = context.getAssetsIndex().url();
        this.size = context.getAssetsIndex().size();
        this.exceptedHash = context.getAssetsIndex().sha1();
        this.assetsIndexName = context.getAssetsIndex().id() + ".json";

        this.destination = Path.of(context.getLauncherDirs().assetsIndexesDir().path(), assetsIndexName);
    }

    @Retryable(attempts = 3)
    @WorkerThread
    @Override
    public void process() {
        try {
            checkRequirements(this.destination);
            new DownloadProcess(context).downloadSHA1(url, destination, size, assetsIndexName, exceptedHash);
        } catch (LauncherException e) {
            stop(e.getMessage(), 1);
        }
    }

    private void checkRequirements(final Path destination) {
        DirectoryCreatorUtils.checkDirectories(String.valueOf(destination.getParent()));
    }
}
