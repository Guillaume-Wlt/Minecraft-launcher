package fr.guillaumewlt.downloads;

import fr.guillaumewlt.exceptionhandler.LauncherException;
import fr.guillaumewlt.model.LibraryInfos;
import fr.guillaumewlt.model.directory.LauncherDir;
import fr.guillaumewlt.model.directory.LauncherDirs;
import fr.guillaumewlt.workflow.LauncherContext;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collections;
import java.util.List;
import java.util.Comparator;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class LibrariesDownloadTest {

    // Small library from 1.6.4
    private static final LibraryInfos TEST_LIBRARY = new LibraryInfos(
            "com.mojang:authlib:1.5.21",
            "a223667b28f2e5bc2b61a914fe23cc5b8e25e000",
            "com/mojang/authlib/1.5.21/authlib-1.5.21.jar",
            153302L,
            "https://libraries.minecraft.net/com/mojang/authlib/1.5.21/authlib-1.5.21.jar"
    );

    private LauncherContext context;
    private Path tempDir;

    @BeforeEach
    void setUp() throws Exception {
        tempDir = Files.createTempDirectory("launcher-test-libraries-");

        LauncherDir launcherDir   = new LauncherDir("launcher",   tempDir + "/");
        LauncherDir librariesDir  = new LauncherDir("libraries",  tempDir + "/libraries/");
        LauncherDirs launcherDirs = new LauncherDirs(launcherDir, null, null, librariesDir, null, null, null);

        context = new LauncherContext();
        context.setLauncherDirs(launcherDirs);
    }

    @Test
    void download_shouldThrowWhenLibrariesListIsNull() {
        context.setLibrariesInfos(null);

        LibrariesDownload download = new LibrariesDownload(context);
        assertThrows(LauncherException.class, download::download);
    }

    @Test
    void download_shouldThrowWhenLibrariesListIsEmpty() {
        context.setLibrariesInfos(Collections.emptyList());

        LibrariesDownload download = new LibrariesDownload(context);
        assertThrows(LauncherException.class, download::download);
    }

    @Test
    void download_shouldDownloadLibrary() {
        context.setLibrariesInfos(List.of(TEST_LIBRARY));

        LibrariesDownload download = new LibrariesDownload(context);
        boolean result = download.download();

        assertTrue(result);
        assertTrue(new File(tempDir + "/libraries/" + TEST_LIBRARY.path()).exists());
    }

    @Test
    void download_shouldReturnTrueWhenLibraryAlreadyUpToDate() {
        context.setLibrariesInfos(List.of(TEST_LIBRARY));

        LibrariesDownload download = new LibrariesDownload(context);
        download.download();

        // Second download: file already exists with correct hash
        boolean result = download.download();
        assertTrue(result);
    }

    @AfterEach
    void tearDown() throws Exception {
        Files.walk(tempDir)
                .sorted(Comparator.reverseOrder())
                .map(Path::toFile)
                .forEach(File::delete);
    }
}