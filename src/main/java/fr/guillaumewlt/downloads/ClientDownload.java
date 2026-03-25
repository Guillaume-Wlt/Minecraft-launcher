package fr.guillaumewlt.downloads;

import fr.guillaumewlt.exceptionhandler.LauncherException;
import fr.guillaumewlt.utils.DirectoryPathUtils;
import fr.guillaumewlt.utils.LauncherUtils;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.security.NoSuchAlgorithmException;

public class ClientDownload extends Downloads{

    private String selectedVersionName = LauncherUtils.getSelectedVersionName();
    private String selectedVersionDir = DirectoryPathUtils.getSelectedVersionDir();
    private String selectedClientHash = LauncherUtils.getSelectedClientHash();
    private String selectedClientJarName;

    public ClientDownload() {
        defineSelectedClientJarName();
    }

    @Override
    public boolean download() {
        checkRequirements();

        try (InputStream is = URI.create(selectedClientJarURL).toURL().openStream()){
            File localClientJar = new File(selectedClientJarName);

            if (localClientJar.exists()){
                String localClientJarHash = computeSHA1(localClientJar.toPath());
                System.out.println("Local Client Jar >> " + localClientJarHash);

                if (localClientJarHash.equals(selectedClientHash)){
                    System.out.println("Client JAR already exists and is correct, skipping download.");
                    return true;
                }
            }
            Path destination = Path.of(selectedClientJarName);
            Files.copy(is, destination, StandardCopyOption.REPLACE_EXISTING);
            System.out.println("Client JAR >> " + selectedVersionName + ".jar, has been successfully downloaded.");
            return true;

        } catch (IOException | NoSuchAlgorithmException e) {
            throw new LauncherException("Error Downloading Client", e);
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
        selectedClientJarName = selectedVersionDir + selectedVersionName + ".jar";
    }
}
