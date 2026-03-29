package fr.guillaumewlt.parser;

import fr.guillaumewlt.exceptionhandler.LauncherException;
import fr.guillaumewlt.model.SelectedVersion;
import fr.guillaumewlt.model.VersionRawData;
import fr.guillaumewlt.model.directory.LauncherDir;
import fr.guillaumewlt.model.directory.LauncherDirs;
import fr.guillaumewlt.workflow.LauncherContext;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Comparator;

import static org.junit.jupiter.api.Assertions.*;

public class VersionJSONParserTest {

    private static final String VERSION     = "1.6.4";
    private static final String VERSION_URL = "https://piston-meta.mojang.com/v1/packages/b71bae449192fbbe1582ff32fb3765edf0b9b0a8/1.6.4.json";

    private LauncherContext context;
    private Path tempDir;

    @BeforeEach
    void setUp() throws Exception {
        tempDir = Files.createTempDirectory("launcher-test-versionjson-parser-");

        // VersionJSONParser reads versionsDir + "1.6.4/1.6.4.json"
        Path versionDir = tempDir.resolve("versions/" + VERSION);
        Files.createDirectories(versionDir);

        // Download 1.6.4.json to the expected path
        Path jsonFile = versionDir.resolve(VERSION + ".json");
        try (InputStream is = URI.create(VERSION_URL).toURL().openStream();
             OutputStream os = Files.newOutputStream(jsonFile)) {
            os.write(is.readAllBytes());
        }

        LauncherDir launcherDir = new LauncherDir("launcher", tempDir + "/");
        LauncherDir versionsDir = new LauncherDir("versions", tempDir + "/versions/");
        LauncherDirs launcherDirs = new LauncherDirs(launcherDir, null, versionsDir, null, null, null, null);

        context = new LauncherContext();
        context.setSelectedVersion(new SelectedVersion(VERSION, VERSION_URL));
        context.setLauncherDirs(launcherDirs);
    }

    @Test
    void jsonParser_shouldReturnVersionRawData_withClientLibrariesAndAssets() {
        VersionRawData result = new VersionJSONParser(context).jsonParser();

        assertNotNull(result);
        assertNotNull(result.clientData());
        assertNotNull(result.librariesData());
        assertNotNull(result.assets());
    }

    @Test
    void jsonParser_shouldThrowException_whenFileDoesNotExist() {
        context.setSelectedVersion(new SelectedVersion("99.99.99", null));

        assertThrows(LauncherException.class, () -> new VersionJSONParser(context).jsonParser());
    }

    @AfterEach
    void tearDown() throws Exception {
        Files.walk(tempDir)
                .sorted(Comparator.reverseOrder())
                .map(Path::toFile)
                .forEach(File::delete);
    }
}