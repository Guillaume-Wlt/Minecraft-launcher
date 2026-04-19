package fr.guillaumewlt.processing.steps;

import fr.guillaumewlt.downloads.DownloadProcess;
import fr.guillaumewlt.exceptionhandler.LauncherException;
import fr.guillaumewlt.processing.CheckFoldersExistence;
import fr.guillaumewlt.workflow.LauncherContext;

import java.nio.file.Path;

public class DownloadAssetsIndexProcess extends Processes{

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

    @Override
    public void process() {
        try {
            checkRequirements(this.destination);
            new DownloadProcess(context).downloadToFile(url, destination, size, assetsIndexName, exceptedHash, DownloadProcess.HashType.SHA1);
        } catch (LauncherException e) {
            stop(e.getMessage(), 1);
        }
    }

    private void checkRequirements(final Path destination) {
        CheckFoldersExistence.checkDirectories(String.valueOf(destination.getParent()));
    }
}
