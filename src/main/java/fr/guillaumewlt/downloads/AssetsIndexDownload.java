package fr.guillaumewlt.downloads;

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
                ConsoleMessage.ASSETSINDEX_DOWNLOAD_LOCAL_FILE_HASH_MESSAGE.outPrintln(localFileHash);

                if (localFileHash.equals(sha1)) {
                    ConsoleMessage.ASSETSINDEX_DOWNLOAD_FILE_ALREADY_UP_TO_DATE.outPrintln();
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
                ConsoleMessage.ASSETSINDEX_DOWNLOAD_FILE_CORRUPTED_ERR.throwException();
            }
            ConsoleMessage.ASSETSINDEX_DOWNLOAD_FILE_DOWNLOAD_SUCCESSFUL.outPrintln();
            return true;
        } catch (IOException | NoSuchAlgorithmException e) {
            ConsoleMessage.ASSETSINDEX_DOWNLOAD_FILE_DOWNLOAD_ERR.throwException(e.getMessage());
            return false;
        }
    }

    private void checkRequirements() {
        if (context.getLauncherDirs().launcherDir().path() == null) {
            ConsoleMessage.LAUNCHERDIRS_LAUNCHER_DIR_NULL_ERR.throwException();
        }
        if (context.getAssetsIndex().id() == null) {
            ConsoleMessage.ASSETSINDEX_RECORD_ID_NULL_ERR.throwException();
        }
        if (context.getLauncherDirs().assetsIndexesDir().path() == null) {
            ConsoleMessage.LAUNCHERDIRS_ASSETS_INDEXES_DIR_NULL_ERR.throwException();
        }
        this.localPath = Path.of(context.getLauncherDirs().assetsIndexesDir().path() + context.getAssetsIndex().id() + ".json");
    }
}
