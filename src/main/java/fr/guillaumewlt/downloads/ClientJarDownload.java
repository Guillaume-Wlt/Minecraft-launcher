package fr.guillaumewlt.downloads;

import fr.guillaumewlt.exceptionhandler.LauncherException;
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
                System.out.println(ConsoleMessage.CLIENT_JAR_DOWNLOAD_LOCAL_CLIENT_HASH_MESSAGE.format(localClientJarHash));

                if (localClientJarHash.equals(selectedClientJarHash)){
                    System.out.println(ConsoleMessage.CLIENT_JAR_DOWNLOAD_CLIENT_ALREADY_UP_TO_DATE.getMessage());
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

            // Vérification intégrité post-téléchargement
            String downloadedHash = computeSHA1(destination);
            if (!downloadedHash.equals(selectedClientJarHash)) {
                Files.delete(destination); // supprime le fichier corrompu
                throw new LauncherException(ConsoleMessage.CLIENT_JAR_DOWNLOAD_CLIENT_JAR_CORRUPTED_ERR.getMessage());
            }

            System.out.println(ConsoleMessage.CLIENT_JAR_DOWNLOAD_SUCCESSFUL.format(selectedVersionName));
            return true;

        } catch (IOException | NoSuchAlgorithmException e) {
            throw new LauncherException(ConsoleMessage.CLIENT_JAR_DOWNLOAD_ERR.format(e.getMessage()));
        }
    }

    @Override
    protected void checkRequirements() {
        if (selectedClientJarURL == null) {
            throw new LauncherException(ConsoleMessage.CLIENTJARINFOS_RECORD_SELECTED_CLIENT_URL_NULL_ERR.getMessage());
        }
    }

    private void defineSelectedClientJarName() {
        if (selectedVersionDir == null) {
            throw new LauncherException(ConsoleMessage.DIRECTORYPATH_UTILS_SELECTED_VERSION_DIR_PATH_NULL_ERR.getMessage());
        }
        if (selectedVersionName == null) {
            throw new LauncherException(ConsoleMessage.SELECTEDVERSION_RECORD_NAME_NULL_ERR.getMessage());
        }
        selectedClientJarPath = selectedVersionDir + selectedVersionName + ".jar";
    }
}
