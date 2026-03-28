package fr.guillaumewlt.downloads;

import fr.guillaumewlt.exceptionhandler.LauncherException;
import fr.guillaumewlt.utils.LauncherUtils;
import fr.guillaumewlt.utils.console.ConsoleMessage;
import fr.guillaumewlt.workflow.LauncherContext;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.nio.file.Files;
import java.security.NoSuchAlgorithmException;

public class ManifestDownload extends Downloads {

    private String fileName;
    private String versionsDir;
    private String url;

    public ManifestDownload(LauncherContext context) {
        super(context);
        fileName = LauncherUtils.getManifestName();
        versionsDir = context.getLauncherDirs().versionsDir().path();
        url = LauncherUtils.getManifestURL();
    }


    @Override
    public boolean download() {
        try (InputStream is = URI.create(url).toURL().openStream()) {
            // Télécharge le contenu distant en mémoire
            byte[] remoteContent = is.readAllBytes();
            String remoteHash = computeMD5(remoteContent);

            File localFile = new File(versionsDir + fileName);

            if (localFile.exists()) {
                // Compare avec le fichier local
                byte[] localContent = Files.readAllBytes(localFile.toPath());
                String localHash = computeMD5(localContent);

                if (localHash.equals(remoteHash)) {
                    System.out.println(ConsoleMessage.MANIFEST_DOWNLOAD_ALREADY_UP_TO_DATE.getMessage());
                    return true;
                }
            }

            // Fichier différent ou inexistant → télécharge
            try (FileOutputStream fos = new FileOutputStream(versionsDir + fileName)) {
                fos.write(remoteContent);
                System.out.println(ConsoleMessage.MANIFEST_DOWNLOAD_SUCCESSFUL.getMessage());
                return true;
            }

        } catch (IOException | NoSuchAlgorithmException e) {
            throw new LauncherException(ConsoleMessage.MANIFEST_DOWNLOAD_ERR.format(e.getMessage()));
        }
    }
}
