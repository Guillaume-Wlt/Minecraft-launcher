package fr.guillaumewlt.downloads;

import fr.guillaumewlt.exceptionhandler.LauncherException;
import fr.guillaumewlt.model.LibraryInfos;
import fr.guillaumewlt.processing.DownloadProgress;
import fr.guillaumewlt.utils.DirectoryPathUtils;
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
            throw new LauncherException("No libraries found");
        }

        for (LibraryInfos library : libraries) {
            File localLibraryFile = new File(DirectoryPathUtils.getLauncherDir() + "libraries/" + library.path());
            if (!localLibraryFile.exists()) { // Create Folder for the Path if doesn't existe
                localLibraryFile.getParentFile().mkdirs();
            }
            try {
                if (localLibraryFile.exists())  {
                    String localLibraryFileHash = computeSHA1(localLibraryFile.toPath());
                    System.out.println("local library file hash: " + localLibraryFileHash);
                    if (localLibraryFileHash.equals(library.sha1())) {
                        System.out.println("Library already up to date, skipping download");
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
                    throw new LauncherException("Download failed");
                }
                System.out.println(library.name() + " has been downloaded");
            } catch (IOException | NoSuchAlgorithmException e) {
                throw new LauncherException(e.getMessage());
            }
        }
        return true;
    }

    @Override
    protected void checkRequirements() {

    }
}
