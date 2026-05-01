package fr.guillaumewlt.processing.download;

import fr.guillaumewlt.annotations.Retryable;
import fr.guillaumewlt.annotations.WorkerThread;
import fr.guillaumewlt.download.DownloadProcess;
import fr.guillaumewlt.exceptions.LauncherException;
import fr.guillaumewlt.models.LibraryInfos;
import fr.guillaumewlt.processing.Processes;
import fr.guillaumewlt.utils.DirectoryCreatorUtils;
import fr.guillaumewlt.workflow.LauncherContext;

import java.nio.file.Path;
import java.util.List;

public class DownloadLibrariesProcess extends Processes {

    private final List<LibraryInfos> libraries;
    private final String librariesDir;

    public DownloadLibrariesProcess(LauncherContext context) {
        super(context);
        this.libraries = context.getLibrariesInfos();
        this.librariesDir = context.getLauncherDirs().librariesDir().path();
    }

    @Retryable(attempts = 3)
    @WorkerThread
    @Override
    public void process() {
        try {
            if (libraries == null || libraries.isEmpty()) {
                throw new LauncherException("No libraries found");
            }

            for (LibraryInfos library : libraries) {
                final Path destination = Path.of(librariesDir, library.path());
                final String url = library.url();
                final long size = library.size();
                final String exceptedHash = library.sha1();

                checkRequirements(destination);

                new DownloadProcess(context).downloadSHA1(url, destination, size, library.name(), exceptedHash);
            }
        } catch (LauncherException e) {
            stop(e.getMessage(), 1);
        }
    }

    private void checkRequirements(final Path destination) {
        DirectoryCreatorUtils.checkDirectories(String.valueOf(destination.getParent()));
    }
}
