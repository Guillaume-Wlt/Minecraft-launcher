package fr.guillaumewlt.downloads;

import fr.guillaumewlt.exceptionhandler.LauncherException;
import fr.guillaumewlt.processing.DownloadProgress;
import fr.guillaumewlt.utils.DirectoryPathUtils;
import fr.guillaumewlt.utils.LauncherUtils;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Path;
import java.security.NoSuchAlgorithmException;

public class ClientDownload extends Downloads{

    private String selectedVersionName = LauncherUtils.getSelectedVersionName();
    private String selectedVersionDir = DirectoryPathUtils.getSelectedVersionDir();
    private String selectedClientHash = LauncherUtils.getSelectedClientHash();
    private String selectedClientJarPath;

    public ClientDownload() {
        defineSelectedClientJarName();
    }

    @Override
    public boolean download() {
        checkRequirements();

        try {
            File localClientJar = new File(selectedClientJarPath);

            if (localClientJar.exists()){
                String localClientJarHash = computeSHA1(localClientJar.toPath());
                System.out.println("Local Client Jar >> " + localClientJarHash);

                if (localClientJarHash.equals(selectedClientHash)){
                    System.out.println("Client JAR already exists and is correct, skipping download.");
                    return true;
                }
            }

            Path destination = Path.of(selectedClientJarPath);
            long totalSize = LauncherUtils.getSelectedClientSize(); // Size store in Launcherutils.
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
            if (!downloadedHash.equals(selectedClientHash)) {
                Files.delete(destination); // supprime le fichier corrompu
                throw new LauncherException("Client Jar is corrupted, file deleted.");
            }

            System.out.println("Client JAR >> " + selectedVersionName + ".jar has been successfully downloaded.");
            return true;

        } catch (IOException | NoSuchAlgorithmException e) {
            throw new LauncherException("Error downloading client jar", e);
        }
    }

    @Override
    protected void checkRequirements() {
        if (selectedClientJarURL == null) {
            throw new LauncherException("Selected Client Jar URL is null");
        }
    }

    private void defineSelectedClientJarName() {
        if (selectedVersionDir == null) {
            throw new LauncherException("Selected Version Directory is null");
        }
        if (selectedVersionName == null) {
            throw new LauncherException("Selected Version Name is null");
        }
        selectedClientJarPath = selectedVersionDir + selectedVersionName + ".jar";
    }
}
