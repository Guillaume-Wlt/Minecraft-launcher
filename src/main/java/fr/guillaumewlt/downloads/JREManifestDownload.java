package fr.guillaumewlt.downloads;

import fr.guillaumewlt.utils.console.ConsoleMessage;
import fr.guillaumewlt.workflow.LauncherContext;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.nio.file.Files;
import java.security.NoSuchAlgorithmException;

public class JREManifestDownload extends Downloads{

    private String manifestURL;

    private String runtimeDirPath;
    private String jreName;

    public JREManifestDownload(LauncherContext context) {
        super(context);
        manifestURL = context.getRuntimeRawData().url();

        runtimeDirPath = context.getLauncherDirs().runtimeDir().path();
        jreName = context.getRuntimeRawData().name();
    }

    @Override
    public boolean download() {
        try (InputStream is = URI.create(manifestURL).toURL().openStream()) {
            byte[] remoteContent = is.readAllBytes();
            String remoteHash = computeMD5(remoteContent);

            File localFile = new File(runtimeDirPath + jreName + "/" + jreName + ".json");
            if (localFile.exists()) {
                byte[] localContent = Files.readAllBytes(localFile.toPath());
                String localHash = computeMD5(localContent);
                if (localHash.equals(remoteHash)) {
                    ConsoleMessage.JREMANIFEST_DOWNLOAD_ALREADY_UP_TO_DATE.outPrintln();
                    return true;
                }
            }

            localFile.getParentFile().mkdirs();
            try (FileOutputStream fos = new FileOutputStream(localFile)) {
                fos.write(remoteContent);
                ConsoleMessage.JREMANIFEST_DOWNLOAD_SUCCESSFUL.outPrintln();
                return true;
            }
        } catch (IOException | NoSuchAlgorithmException e) {
            ConsoleMessage.JREMANIFEST_DOWNLOAD_ERR.throwException(e.getMessage());
            return false;
        }
    }
}
