package fr.guillaumewlt.downloads;

import fr.guillaumewlt.processing.DownloadProgress;
import fr.guillaumewlt.utils.DirectoryPathUtils;
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

public class ClientJarDownload extends Downloads{

    private final String selectedVersionName;
    private final String selectedVersionDir;
    private final String selectedClientJarHash;
    private final String selectedClientJarURL;
    private final long selectedClientJarSize;
    private String selectedClientJarPath;

    public ClientJarDownload(LauncherContext context) {
        super(context);
        selectedVersionName = context.getSelectedVersion().selectedVersion();
        selectedVersionDir = DirectoryPathUtils.getSelectedVersionDir(context.getLauncherDirs(), selectedVersionName);
        selectedClientJarHash = context.getClientJarInfos().sha1();
        selectedClientJarURL = context.getClientJarInfos().url();
        selectedClientJarSize = context.getClientJarInfos().size();
        defineSelectedClientJarName();
    }

    @Override
    public boolean download() {
        checkRequirements();

        try {
            File localClientJar = new File(selectedClientJarPath);

            if (localClientJar.exists()){
                String localClientJarHash = computeSHA1(localClientJar.toPath());
                ConsoleMessage.CLIENT_JAR_DOWNLOAD_LOCAL_CLIENT_HASH_MESSAGE.outPrintln(localClientJarHash);

                if (localClientJarHash.equals(selectedClientJarHash)){
                    ConsoleMessage.CLIENT_JAR_DOWNLOAD_CLIENT_ALREADY_UP_TO_DATE.outPrintln();
                    return true;
                }
            }

            Path destination = Path.of(selectedClientJarPath);
            long totalSize = selectedClientJarSize; // Size store in Launcherutils.
            DownloadProgress progress = new DownloadProgress(totalSize);

            try (InputStream is = URI.create(selectedClientJarURL).toURL().openStream();
                 OutputStream os = Files.newOutputStream(destination)) {
                byte[] buffer = new byte[8192];
                int read;
                while ((read = is.read(buffer)) != -1) {
                    os.write(buffer, 0, read);
                    progress.update(selectedVersionName + ".jar", read);
                }
            }
            progress.complete();

            // Vérification intégrité post-téléchargement
            String downloadedHash = computeSHA1(destination);
            if (!downloadedHash.equals(selectedClientJarHash)) {
                Files.delete(destination); // supprime le fichier corrompu
                ConsoleMessage.CLIENT_JAR_DOWNLOAD_CLIENT_JAR_CORRUPTED_ERR.throwException();
            }

            ConsoleMessage.CLIENT_JAR_DOWNLOAD_SUCCESSFUL.outPrint(selectedVersionName);
            return true;

        } catch (IOException | NoSuchAlgorithmException e) {
            ConsoleMessage.CLIENT_JAR_DOWNLOAD_ERR.throwException(e.getMessage());
            return false;
        }
    }

    private void checkRequirements() {
        if (selectedClientJarURL == null) {
            ConsoleMessage.CLIENTJARINFOS_RECORD_SELECTED_CLIENT_URL_NULL_ERR.throwException();
        }
    }

    private void defineSelectedClientJarName() {
        if (selectedVersionDir == null) {
            ConsoleMessage.DIRECTORYPATH_UTILS_SELECTED_VERSION_DIR_PATH_NULL_ERR.throwException();
        }
        if (selectedVersionName == null) {
            ConsoleMessage.SELECTEDVERSION_RECORD_NAME_NULL_ERR.throwException();
        }
        selectedClientJarPath = selectedVersionDir + selectedVersionName + ".jar";
    }
}
