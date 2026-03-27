package fr.guillaumewlt.downloads;

import fr.guillaumewlt.exceptionhandler.LauncherException;
import fr.guillaumewlt.model.LibraryInfos;
import fr.guillaumewlt.processing.CheckFoldersExistence;
import fr.guillaumewlt.processing.DownloadProgress;
import fr.guillaumewlt.utils.DirectoryPathUtils;
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
        libraries = context.getLibrariesInfos();
    }

    @Override
    public boolean download() {
        checkRequirements();

        if (libraries.isEmpty()) {
            throw new LauncherException(ConsoleMessage.LIBRARIESDOWNLOAD_LIBRARIES_LIST_NULL_ERR.getMessage());
        }

        for (LibraryInfos library : libraries) {
            File localLibraryFile = new File(DirectoryPathUtils.getLauncherDir() + "libraries/" + library.path());
            if (!localLibraryFile.exists()) { // Create Folder for the Path if doesn't existe
                localLibraryFile.getParentFile().mkdirs();
            }
            try {
                if (localLibraryFile.exists())  {
                    String localLibraryFileHash = computeSHA1(localLibraryFile.toPath());
                    System.out.println(ConsoleMessage.LIBRARIESDOWNLOAD_LOCAL_LIBRARY_FILE_HASH_MESSAGE.format(localLibraryFileHash));
                    if (localLibraryFileHash.equals(library.sha1())) {
                        System.out.println(ConsoleMessage.LIBRARIESDOWNLOAD_ALREAY_UP_TO_DATE.format(library.name()));
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

                String downloadedHash = computeSHA1(destination);
                if (!downloadedHash.equals(library.sha1())) {
                    Files.delete(destination);
                    throw new LauncherException(ConsoleMessage.LIBRARIESDOWNLOAD_CORRUPT_ERR.format(library.name()));
                }
                System.out.println(ConsoleMessage.LIBRARIESDOWNLOAD_SUCCESSFUL.format(library.name()));
            } catch (IOException | NoSuchAlgorithmException e) {
                throw new LauncherException(ConsoleMessage.LIBRARIESDOWNLOAD_ERR.format(library.name(), e.getMessage()));
            }
        }
        return true;
    }

    @Override
    protected void checkRequirements() {
        CheckFoldersExistence.checkDirectories(launcherDir + "libraries/");
    }
}
