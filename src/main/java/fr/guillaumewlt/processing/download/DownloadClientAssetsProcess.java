package fr.guillaumewlt.processing.download;

import fr.guillaumewlt.annotations.Retryable;
import fr.guillaumewlt.annotations.WorkerThread;
import fr.guillaumewlt.console.ConsoleMessage;
import fr.guillaumewlt.download.DownloadProcess;
import fr.guillaumewlt.exceptions.LauncherException;
import fr.guillaumewlt.models.assets.AssetInfos;
import fr.guillaumewlt.processing.Processes;
import fr.guillaumewlt.utils.ConstantUtils;
import fr.guillaumewlt.utils.DirectoryCreatorUtils;
import fr.guillaumewlt.workflow.LauncherContext;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

public class DownloadClientAssetsProcess extends Processes {

    private final List<AssetInfos> assets;
    private final String assetsURL;

    public DownloadClientAssetsProcess(LauncherContext context) {
        super(context);
        this.assets = context.getAssetsInfos();
        this.assetsURL = ConstantUtils.ASSETS_URL;
    }

    @Retryable(attempts = 3)
    @WorkerThread
    @Override
    public void process() {
        try {
            if (assets == null || assets.isEmpty()) {
                throw new LauncherException("No assets found");
            }

            for (AssetInfos asset : assets) {
                final String name = asset.name();
                final String exceptedHash = asset.hash();
                final long size = asset.size();
                final String url = assetsURL + exceptedHash.substring(0,2) + "/" + exceptedHash;
                final Path destination = Path.of(context.getLauncherDirs().assetsObjectsDir().path(), exceptedHash.substring(0,2), exceptedHash);

                checkRequirements(destination);

                new DownloadProcess(context).downloadSHA1(url, destination, size, name, exceptedHash);

                if (context.isVirtualAssets()) {
                    Path newDestination = Path.of(context.getLauncherDirs().assetsDir().path()
                            + "virtual/legacy/" + asset.name());
                    checkRequirements(newDestination);
                    if (!newDestination.toFile().exists()) {
                        try {
                            Files.copy(destination, newDestination);
                        } catch (IOException e) {
                            ConsoleMessage.ASSETSOBJECTS_DOWNLOAD_COPY_VIRTUAL_ASSETS_ERROR.throwException(e.getMessage());
                        }
                    }
                }
            }
        } catch (LauncherException e) {
            stop(e.getMessage(), 1);
        }
    }

    private void checkRequirements(final Path destination) {
        DirectoryCreatorUtils.checkDirectories(String.valueOf(destination.getParent()));
    }
}
