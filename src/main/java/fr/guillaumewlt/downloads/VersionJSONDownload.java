package fr.guillaumewlt.downloads;

import fr.guillaumewlt.exceptionhandler.LauncherException;
import fr.guillaumewlt.processing.CheckFoldersExistence;
import fr.guillaumewlt.utils.DirectoryPathUtils;
import fr.guillaumewlt.utils.console.ConsoleMessage;
import fr.guillaumewlt.workflow.LauncherContext;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.nio.file.Files;
import java.security.NoSuchAlgorithmException;

public class VersionJSONDownload extends Downloads {

    private String url;
    private String versionName;
    private String versionsDir;
    private String selectedVersionDir;

    public VersionJSONDownload(LauncherContext context, String url) {
        super(context);
        if (context.getSelectedVersion() != null) {
            this.url = url;
            this.versionName = context.getSelectedVersion().selectedVersion();
            this.versionsDir = context.getLauncherDirs().versionsDir().path(); // versionDir -> [...]/launcher/versions/
            this.selectedVersionDir = DirectoryPathUtils.getSelectedVersionDir(context.getLauncherDirs(), versionName); // selectedVersionDir -> [...]/launcher/versions/<selected_version>/
        } else {
            throw new LauncherException(ConsoleMessage.SELECTEDVERSION_RECORD_NAME_NULL_ERR.getMessage());
        }
    }


    @Override
    public boolean download() {
        checkRequirements();

        try (InputStream is = URI.create(url).toURL().openStream()) {
            // Télécharge le contenu du fichier distant dans la mémoire
            byte[] remoteContent = is.readAllBytes();
            String remoteHash = computeMD5(remoteContent); // utilise la méthode dans la classe Mère : Downloads.java

            File localFile = new File(selectedVersionDir + versionName + ".json");

            if (localFile.exists()) {
                byte[] localContent = Files.readAllBytes(localFile.toPath());
                String localHash = computeMD5(localContent);

                if (localHash.equals(remoteHash)) {
                    System.out.println(ConsoleMessage.VERSION_JSON_DOWNLOAD_ALREADY_UP_TO_DATE.getMessage());
                    return true;
                }
            }

            try (FileOutputStream fos = new FileOutputStream(selectedVersionDir + versionName + ".json")) {
                fos.write(remoteContent);
                System.out.println(ConsoleMessage.VERSION_JSON_DOWNLOAD_SUCCESSFUL.getMessage());
                return true;
            }

        } catch (IOException | NoSuchAlgorithmException e) {
            throw new LauncherException(ConsoleMessage.VERSION_JSON_DOWNLOAD_ERR.format(e.getMessage()));
        }
    }

    private void checkRequirements() {
        CheckFoldersExistence.checkDirectories(selectedVersionDir);
    }
}
