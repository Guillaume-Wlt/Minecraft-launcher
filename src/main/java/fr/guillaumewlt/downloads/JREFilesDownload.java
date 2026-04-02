package fr.guillaumewlt.downloads;

import fr.guillaumewlt.exceptionhandler.LauncherException;
import fr.guillaumewlt.model.JREFileInfos;
import fr.guillaumewlt.processing.DownloadProgress;
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
            throw new LauncherException("JRE Files List is Empty");
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
                    System.out.println("Local JRE File Hash: " + localJREFileHash);
                    if (localJREFileHash.equals(jreFileInfos.sha1())) {
                        System.out.println("File already up to date");
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

                String downloadedHash = computeSHA1(destination);
                if (!downloadedHash.equals(jreFileInfos.sha1())) {
                    Files.delete(destination);
                    throw new LauncherException("File is corrupted");
                }
            } catch (IOException | NoSuchAlgorithmException e) {
                throw new LauncherException("Error while downloading JRE Files", e);
            }
        }
        return true;
    }
}
