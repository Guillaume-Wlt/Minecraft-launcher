package fr.guillaumewlt.downloads;

import fr.guillaumewlt.model.SelectedVersion;
import fr.guillaumewlt.model.directory.LauncherDir;
import fr.guillaumewlt.model.directory.LauncherDirs;
import fr.guillaumewlt.workflow.LauncherContext;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class VersionJSONDownloadTest {

    private String predefinedVersion;
    private String predefinedURL;
    private LauncherContext context;
    private Path tempDir;          // dossier temporaire créé pour le test

    @BeforeEach
    void setUp() throws Exception {
        predefinedVersion = "1.6.4";
        predefinedURL = "https://piston-meta.mojang.com/v1/packages/b71bae449192fbbe1582ff32fb3765edf0b9b0a8/1.6.4.json";

        // Crée un dossier temporaire pour ne pas polluer le projet
        tempDir = Files.createTempDirectory("launcher-test-");

        // Construit un LauncherDirs minimal pointant vers le dossier temporaire
        LauncherDir launcherDir = new LauncherDir("launcher", tempDir + "/");
        LauncherDir versionsDir = new LauncherDir("versions", tempDir + "/versions/");
        LauncherDirs launcherDirs = new LauncherDirs(launcherDir, null, versionsDir, null, null, null, null);

        // Construit le contexte avec la version et les dossiers
        context = new LauncherContext();
        context.setSelectedVersion(new SelectedVersion(predefinedVersion, predefinedURL));
        context.setLauncherDirs(launcherDirs);
    }

    @Test
    void download_shouldCreateVersionJsonFile() {
        VersionJSONDownload download = new VersionJSONDownload(context, predefinedURL);
        boolean result = download.download();

        assertTrue(result);
        assertTrue(new File(tempDir + "/versions/" + predefinedVersion + "/" + predefinedVersion + ".json").exists());
    }

    @AfterEach
    void tearDown() throws Exception {
        // Supprime les fichiers créés pendant le test
        Files.walk(tempDir)
                .sorted(java.util.Comparator.reverseOrder())
                .map(Path::toFile)
                .forEach(File::delete);
    }
}
