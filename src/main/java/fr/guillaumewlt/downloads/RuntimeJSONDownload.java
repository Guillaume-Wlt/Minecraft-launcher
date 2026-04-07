package fr.guillaumewlt.downloads;

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

public class RuntimeJSONDownload extends Downloads{

    private String runtimeName;
    private String runtimeURL;

    public RuntimeJSONDownload(LauncherContext context) {
        super(context);
        runtimeName = LauncherUtils.getRuntimeName();
        runtimeURL = LauncherUtils.getRuntimeJreURL();
    }

    @Override
    public boolean download() {
        try (InputStream is = URI.create(runtimeURL).toURL().openStream()){
            byte[] remoteContent = is.readAllBytes();
            String remoteHash = computeMD5(remoteContent);

            File localFile = new File(context.getLauncherDirs().runtimeDir().path() + runtimeName);
            if (localFile.exists()) {
                byte[] localContent = Files.readAllBytes(localFile.toPath());
                String localHash = computeMD5(localContent);
                if (localHash.equals(remoteHash)) {
                    ConsoleMessage.RUNTIMEJSON_DOWNLOAD_ALREADY_UP_TO_DATE.outPrintln();
                    return true;
                }
            }

            try (FileOutputStream fos = new FileOutputStream(localFile)) {
                fos.write(remoteContent);
                ConsoleMessage.RUNTIMEJSON_DOWNLOAD_SUCCESSFUL.outPrintln();
                return true;
            }
        } catch (IOException | NoSuchAlgorithmException e) {
            ConsoleMessage.RUNTIMEJSON_DOWNLOAD_ERR.throwException(e.getMessage());
            return false;
        }
    }
}
