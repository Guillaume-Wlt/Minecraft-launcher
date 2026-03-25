package fr.guillaumewlt.downloads;

import fr.guillaumewlt.exceptionhandler.LauncherException;
import fr.guillaumewlt.processing.CheckFoldersExistence;
import fr.guillaumewlt.utils.LauncherUtils;

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

    public ManifestDownload() {
        fileName = LauncherUtils.getManifestName();
        versionsDir = LauncherUtils.getVersionsDir();
        url = LauncherUtils.getManifestUrl();
    }


    @Override
    public boolean download() {
        checkRequirements();

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
                    System.out.println("Manifest already up to date, skipping download.");
                    return true;
                }
            }

            // Fichier différent ou inexistant → télécharge
            try (FileOutputStream fos = new FileOutputStream(versionsDir + fileName)) {
                fos.write(remoteContent);
                System.out.println("Manifest download >> Successfull");
                return true;
            }

        } catch (IOException | NoSuchAlgorithmException e) {
            throw new LauncherException("Error downloading manifest: " + e.getMessage());
        }
    }

    @Override
    protected void checkRequirements() {
        CheckFoldersExistence.checkDirectories(launcherDir);
        CheckFoldersExistence.checkDirectories(versionsDir);
    }
}
