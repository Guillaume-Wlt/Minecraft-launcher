package fr.guillaumewlt.downloads;

import fr.guillaumewlt.model.directory.LauncherDir;
import fr.guillaumewlt.model.directory.LauncherDirs;
import fr.guillaumewlt.workflow.LauncherContext;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Comparator;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class ManifestDownloadTest {

    private LauncherContext context;
    private Path tempDir;

    @BeforeEach
    void setUp() throws Exception {
        tempDir = Files.createTempDirectory("launcher-test-manifest-");

        // ManifestDownload writes to versionsDir + "test/version_manifest.json"
        Files.createDirectories(tempDir.resolve("versions/test"));

        LauncherDir launcherDir = new LauncherDir("launcher", tempDir + "/");
        LauncherDir versionsDir = new LauncherDir("versions", tempDir + "/versions/");
        LauncherDirs launcherDirs = new LauncherDirs(launcherDir, null, versionsDir, null, null, null, null);

        context = new LauncherContext();
        context.setLauncherDirs(launcherDirs);
    }

    @Test
    void download_shouldCreateManifestFile() {
        ManifestDownload download = new ManifestDownload(context);
        boolean result = download.download();

        assertTrue(result);
        assertTrue(new File(tempDir + "/versions/test/version_manifest.json").exists());
    }

    @Test
    void download_shouldReturnTrueWhenAlreadyUpToDate() {
        ManifestDownload download = new ManifestDownload(context);
        download.download();

        // Second download: file already exists and is up-to-date
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