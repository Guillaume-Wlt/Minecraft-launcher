package fr.guillaumewlt.processing.steps;

import fr.guillaumewlt.downloads.DownloadProcess;
import fr.guillaumewlt.exceptionhandler.LauncherException;
import fr.guillaumewlt.model.LibraryInfos;
import fr.guillaumewlt.processing.CheckFoldersExistence;
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

                new DownloadProcess(context).downloadToFile(url, destination, size, library.name(), exceptedHash, DownloadProcess.HashType.SHA1);
            }
        } catch (LauncherException e) {
            stop(e.getMessage(), 1);
        }
    }

    private void checkRequirements(final Path destination) {
        CheckFoldersExistence.checkDirectories(String.valueOf(destination.getParent()));
    }
}
