package fr.guillaumewlt.downloads;

import fr.guillaumewlt.model.LibraryInfos;
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

public class LibrariesDownload extends Downloads {

    private List<LibraryInfos> libraries;

    public LibrariesDownload(LauncherContext context) {
        super(context);
        libraries = context.getLibrariesInfos();
    }

    @Override
    public boolean download() {
        if (libraries == null || libraries.isEmpty()) {
            ConsoleMessage.LIBRARIESDOWNLOAD_LIBRARIES_LIST_NULL_ERR.throwException();
        }

        for (LibraryInfos library : libraries) {
            File localLibraryFile = new File(context.getLauncherDirs().librariesDir().path() + library.path());
            File parentFile = localLibraryFile.getParentFile();
            if (!localLibraryFile.exists() && parentFile != null) { // Create Folder for the Path if doesn't existe
                parentFile.mkdirs();
            }
            try {
                if (localLibraryFile.exists())  {
                    String localLibraryFileHash = computeSHA1(localLibraryFile.toPath());
                    ConsoleMessage.LIBRARIESDOWNLOAD_LOCAL_LIBRARY_FILE_HASH_MESSAGE.outPrintln(localLibraryFileHash);
                    if (localLibraryFileHash.equals(library.sha1())) {
                        ConsoleMessage.LIBRARIESDOWNLOAD_ALREAY_UP_TO_DATE.outPrintln(library.name());
                        continue;
                    }
                }

                Path destination = localLibraryFile.toPath();
                long totalSize = library.size();
                DownloadProgress progress = new DownloadProgress(totalSize);

                try (InputStream is = URI.create(library.url()).toURL().openStream();
                     OutputStream os = Files.newOutputStream(destination)) {
                    byte[] buffer = new byte[8192];
                    int read;
                    while ((read = is.read(buffer)) != -1) {
                        os.write(buffer, 0, read);
                        progress.update(library.name(), read);
                    }
                }
                progress.complete();

                String downloadedHash = computeSHA1(destination);
                if (!downloadedHash.equals(library.sha1())) {
                    Files.delete(destination);
                    ConsoleMessage.LIBRARIESDOWNLOAD_CORRUPT_ERR.throwException(library.name());
                }
                ConsoleMessage.LIBRARIESDOWNLOAD_SUCCESSFUL.outPrintln(library.name());
            } catch (IOException | NoSuchAlgorithmException e) {
                ConsoleMessage.LIBRARIESDOWNLOAD_ERR.throwException(library.name(), e.getMessage());
            }
        }
        return true;
    }
}
