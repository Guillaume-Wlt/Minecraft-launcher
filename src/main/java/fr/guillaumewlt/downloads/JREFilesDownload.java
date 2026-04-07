package fr.guillaumewlt.downloads;

import fr.guillaumewlt.model.JREFileInfos;
import fr.guillaumewlt.processing.DownloadProgress;
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
import java.util.List;

public class JREFilesDownload extends Downloads{

    private List<JREFileInfos> jreFilesInfos;

    public JREFilesDownload(LauncherContext context) {
        super(context);
        jreFilesInfos = context.getJreFilesInfos();
    }

    @Override
    public boolean download() {
        if (jreFilesInfos == null || jreFilesInfos.isEmpty()) {
            ConsoleMessage.JREFILES_DOWNLOAD_JRE_FILES_LIST_EMPTY_ERR.throwException();
        }

        for (JREFileInfos jreFileInfos : jreFilesInfos) {
            if (jreFileInfos.type().equals("directory")) {
                File newJREDir = new File(context.getLauncherDirs().runtimeDir().path() + context.getVersionRawData().javaVersion() + "/" + jreFileInfos.path());
                if (!newJREDir.exists()) {
                    newJREDir.mkdirs();
                    continue;
                }
                continue;
            }

            File localJREFile = new File(context.getLauncherDirs().runtimeDir().path() + context.getVersionRawData().javaVersion() + "/" + jreFileInfos.path());
            File localJREFileDirs = localJREFile.getParentFile();
            if (localJREFileDirs != null && !localJREFileDirs.exists()) {
                localJREFileDirs.mkdirs();
            }

            try {
                if (localJREFile.exists()) {
                    String localJREFileHash = computeSHA1(localJREFile.toPath());
                    ConsoleMessage.JREFILES_DOWNLOAD_LOCAL_JRE_FILE_MESSAGE.outPrintln(localJREFileHash);
                    if (localJREFileHash.equals(jreFileInfos.sha1())) {
                        ConsoleMessage.JREFILES_DOWNLOAD_ALREADY_UP_TO_DATE.outPrintln(jreFileInfos.path());
                        continue;
                    }
                }

                Path destination = localJREFile.toPath();
                long totalSize = jreFileInfos.size();

                if (totalSize == 0) { // create the file without downloading it (file size = 0)
                    Files.createFile(destination);
                    continue;
                }
                DownloadProgress progress = new DownloadProgress(totalSize);

                try (InputStream is = URI.create(jreFileInfos.url()).toURL().openStream();
                     OutputStream os = Files.newOutputStream(destination)) {
                    byte[] buffer = new byte[8192];
                    int read;
                    while ((read = is.read(buffer)) != -1) {
                        os.write(buffer, 0, read);
                        progress.update(jreFileInfos.path(), read);
                    }
                }
                progress.complete();

                String downloadedHash = computeSHA1(destination);
                if (!downloadedHash.equals(jreFileInfos.sha1())) {
                    Files.delete(destination);
                    ConsoleMessage.JREFILES_DOWNLOAD_CORRUPTED_FILE_ERR.throwException(jreFileInfos.path());
                }
            } catch (IOException | NoSuchAlgorithmException e) {
                ConsoleMessage.JREFILES_DOWNLOAD_ERR.throwException(jreFileInfos.path(), e.getMessage());
            }
        }
        return true;
    }
}
