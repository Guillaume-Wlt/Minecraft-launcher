package fr.guillaumewlt.parser;

import fr.guillaumewlt.exceptionhandler.LauncherException;
import fr.guillaumewlt.model.SelectedVersion;
import fr.guillaumewlt.model.directory.LauncherDir;
import fr.guillaumewlt.model.directory.LauncherDirs;
import fr.guillaumewlt.workflow.LauncherContext;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Comparator;
import java.util.Scanner;

import static org.junit.jupiter.api.Assertions.*;

public class ManifestParserTest {

    private static final String VERSION     = "1.6.4";
    private static final String VERSION_URL = "https://piston-meta.mojang.com/v1/packages/b71bae449192fbbe1582ff32fb3765edf0b9b0a8/1.6.4.json";

    // Minimal manifest JSON with one version entry
    private static final String MANIFEST_JSON = "{"
            + "\"versions\": ["
            + "{\"id\": \"" + VERSION + "\", \"url\": \"" + VERSION_URL + "\"},"
            + "{\"id\": \"1.20.1\", \"url\": \"https://example.com/1.20.1.json\"}"
            + "]}";

    private LauncherContext context;
    private Path tempDir;

    @BeforeEach
    void setUp() throws Exception {
        tempDir = Files.createTempDirectory("launcher-test-manifest-parser-");

        // ManifestParser reads versionsDir + "test/version_manifest.json"
        Files.createDirectories(tempDir.resolve("versions/test"));
        File manifestFile = tempDir.resolve("versions/test/version_manifest.json").toFile();
        try (FileWriter fw = new FileWriter(manifestFile)) {
            fw.write(MANIFEST_JSON);
        }

        LauncherDir launcherDir = new LauncherDir("launcher", tempDir + "/");
        LauncherDir versionsDir = new LauncherDir("versions", tempDir + "/versions/");
        LauncherDirs launcherDirs = new LauncherDirs(launcherDir, null, versionsDir, null, null, null, null);

        context = new LauncherContext();
        context.setLauncherDirs(launcherDirs);
    }

    @Test
    void jsonParser_shouldReturnSelectedVersion_whenVersionExists() {
        context.setScanner(new Scanner(new ByteArrayInputStream((VERSION + "\n").getBytes())));

        SelectedVersion result = new ManifestParser(context).jsonparser();

        assertNotNull(result);
        assertEquals(VERSION, result.selectedVersion());
        assertEquals(VERSION_URL, result.url());
    }

    @Test
    void jsonParser_shouldThrowException_whenVersionNotFound() {
        context.setScanner(new Scanner(new ByteArrayInputStream("99.99.99\n".getBytes())));

        assertThrows(LauncherException.class, () -> new ManifestParser(context).jsonparser());
    }

    @AfterEach
    void tearDown() throws Exception {
        Files.walk(tempDir)
                .sorted(Comparator.reverseOrder())
                .map(Path::toFile)
                .forEach(File::delete);
    }
}