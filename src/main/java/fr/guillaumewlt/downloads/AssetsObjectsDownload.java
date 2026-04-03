package fr.guillaumewlt.downloads;

import fr.guillaumewlt.exceptionhandler.LauncherException;
import fr.guillaumewlt.model.assets.AssetInfos;
import fr.guillaumewlt.processing.DownloadProgress;
import fr.guillaumewlt.utils.LauncherUtils;
import fr.guillaumewlt.utils.console.ConsoleMessage;
import fr.guillaumewlt.workflow.LauncherContext;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Path;
import java.security.NoSuchAlgorithmException;
import java.util.List;

public class AssetsObjectsDownload extends Downloads{

    private List<AssetInfos> assets;
    private String assetsURL;

    public AssetsObjectsDownload(LauncherContext context) {
        super(context);
        assets = context.getAssetsInfos();
        assetsURL = LauncherUtils.getAssetsURL();
    }

    @Override
    public boolean download() {
        if (assets == null || assets.isEmpty()) {
            throw new LauncherException(ConsoleMessage.ASSETSOBJECTS_DOWNLOAD_ASSETS_LIST_NULL_ERR.getMessage());
        }

        for (AssetInfos asset : assets) {
            File localAssetFile = new File(context.getLauncherDirs().assetsObjectsDir().path() + asset.hash().substring(0,2) + "/" + asset.hash());
            File assetDirs = localAssetFile.getParentFile();
            if (!localAssetFile.exists() && assetDirs != null) { // Create folders for the path if doesn't exist
                assetDirs.mkdirs();
            }
            try {
                if (localAssetFile.exists()) {
                    String localAssetHash = computeSHA1(localAssetFile.toPath());
                    System.out.println(ConsoleMessage.ASSETSOBJECTS_DOWNLOAD_LOCAL_ASSET_FILE_HASH_MESSAGE.format(localAssetHash));
                    if (localAssetHash.equals(asset.hash())) {
                        System.out.println(ConsoleMessage.ASSETSOBJECTS_DOWNLOAD_ASSET_ALREADY_UP_TO_DATE.getMessage());
                        continue;
                    }
                }

                Path destination = localAssetFile.toPath();
                long totalSize = asset.size();
                DownloadProgress progress = new DownloadProgress(totalSize);

                try (InputStream is = URI.create(assetsURL + asset.hash().substring(0,2) + "/" + asset.hash()).toURL().openStream();
                     OutputStream os = Files.newOutputStream(destination)) {
                    byte[] buffer = new byte[8192];
                    int read;
                    while ((read = is.read(buffer)) != -1) {
                        os.write(buffer, 0, read);
                        progress.update(asset.name(), read);
                    }
                }
                progress.complete();

                String downloadedHash = computeSHA1(destination);
                if (!downloadedHash.equals(asset.hash())) {
                    Files.delete(destination);
                    throw new LauncherException(ConsoleMessage.ASSETSOBJECTS_DOWNLOAD_CORRUPTED_ERR.format(asset.name()));
                }
                System.out.println(ConsoleMessage.ASSETSOBJECTS_DOWNLOAD_SUCCESSFUL.getMessage());
            } catch (IOException | NoSuchAlgorithmException e) {
                throw new LauncherException(ConsoleMessage.ASSETSOBJECTS_DOWNLOAD_ERR.format(e.getMessage()), e);
            }
        }

        if (context.isVirtualAssets()) {
            for (AssetInfos asset : assets) {
                Path src = Path.of(context.getLauncherDirs().assetsObjectsDir().path()
                        + asset.hash().substring(0, 2) + "/" + asset.hash());
                Path dst = Path.of(context.getLauncherDirs().assetsDir().path()
                        + "virtual/legacy/" + asset.name());
                dst.getParent().toFile().mkdirs();
                if (!dst.toFile().exists()) {
                    try {
                        Files.copy(src, dst);
                    } catch (IOException e) {
                        throw new LauncherException(ConsoleMessage.ASSETSOBJECTS_DOWNLOAD_COPY_VIRTUAL_ASSETS_ERROR.format(e.getMessage()));
                    }
                }
            }
        }
        return true;
    }
}
