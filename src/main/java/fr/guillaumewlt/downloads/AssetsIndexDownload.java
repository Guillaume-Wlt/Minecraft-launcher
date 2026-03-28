package fr.guillaumewlt.downloads;

import fr.guillaumewlt.exceptionhandler.LauncherException;
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

public class AssetsIndexDownload extends Downloads{

    private String url;
    private String sha1;
    private Path localPath;

    public AssetsIndexDownload(LauncherContext context) {
        super(context);
        this.url = context.getAssetsIndex().url();
        this.sha1 = context.getAssetsIndex().sha1();
    }

    @Override
    public boolean download() {
        checkRequirements();

        try {
            File localFile = new File(this.localPath.toString());

            if (localFile.exists()){
                String localFileHash = computeSHA1(localPath);
                System.out.println(ConsoleMessage.ASSETSINDEX_DOWNLOAD_LOCAL_FILE_HASH_MESSAGE.format(localFileHash));

                if (localFileHash.equals(sha1)) {
                    System.out.println(ConsoleMessage.ASSETSINDEX_DOWNLOAD_FILE_ALREADY_UP_TO_DATE.getMessage());
                    return true;
                }
            }

            try (InputStream is = URI.create(url).toURL().openStream();
                 OutputStream os = Files.newOutputStream(localPath)) {
                is.transferTo(os);
            }

            String downloadedFileHash = computeSHA1(localPath);
            if (!downloadedFileHash.equals(sha1)) {
                Files.delete(localPath);
                throw new LauncherException(ConsoleMessage.ASSETSINDEX_DOWNLOAD_FILE_CORRUPTED_ERR.getMessage());
            }
            System.out.println(ConsoleMessage.ASSETSINDEX_DOWNLOAD_FILE_DOWNLOAD_SUCCESSFUL.getMessage());
            return true;
        } catch (IOException | NoSuchAlgorithmException e) {
            throw new LauncherException(ConsoleMessage.ASSETSINDEX_DOWNLOAD_FILE_DOWNLOAD_ERR.format(e.getMessage()));
        }
    }

    private void checkRequirements() {
        if (context.getLauncherDirs().launcherDir().path() == null) {
            throw new LauncherException(ConsoleMessage.LAUNCHERDIRS_LAUNCHER_DIR_NULL_ERR.getMessage());
        }
        if (context.getAssetsIndex().id() == null) {
            throw new LauncherException(ConsoleMessage.ASSETSINDEX_RECORD_ID_NULL_ERR.getMessage());
        }
        if (context.getLauncherDirs().assetsIndexesDir().path() == null) {
            throw new LauncherException(ConsoleMessage.LAUNCHERDIRS_ASSETS_INDEXES_DIR_NULL_ERR.getMessage());
        }
        this.localPath = Path.of(context.getLauncherDirs().assetsIndexesDir().path() + context.getAssetsIndex().id() + ".json");
    }
}
