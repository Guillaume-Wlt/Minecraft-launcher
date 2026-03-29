package fr.guillaumewlt.downloads;

import fr.guillaumewlt.exceptionhandler.LauncherException;
import fr.guillaumewlt.model.ClientJarInfos;
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
import java.util.Comparator;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class ClientJarDownloadTest {

    private static final String VERSION       = "1.6.4";
    private static final String CLIENT_URL    = "https://launcher.mojang.com/v1/objects/752f93c0bba6c80e13e0e11ecf80e18b3a04cf23/client.jar";
    private static final String CLIENT_SHA1   = "752f93c0bba6c80e13e0e11ecf80e18b3a04cf23";
    private static final long   CLIENT_SIZE   = 4531991L;

    private LauncherContext context;
    private Path tempDir;

    @BeforeEach
    void setUp() throws Exception {
        tempDir = Files.createTempDirectory("launcher-test-clientjar-");
        Files.createDirectories(tempDir.resolve("versions/" + VERSION));

        LauncherDir launcherDir  = new LauncherDir("launcher",  tempDir + "/");
        LauncherDir versionsDir  = new LauncherDir("versions",  tempDir + "/versions/");
        LauncherDirs launcherDirs = new LauncherDirs(launcherDir, null, versionsDir, null, null, null, null);

        context = new LauncherContext();
        context.setSelectedVersion(new SelectedVersion(VERSION, null));
        context.setLauncherDirs(launcherDirs);
    }

    @Test
    void download_shouldThrowWhenUrlIsNull() {
        context.setClientJarInfos(new ClientJarInfos(null, CLIENT_SHA1, CLIENT_SIZE));

        ClientJarDownload download = new ClientJarDownload(context);
        assertThrows(LauncherException.class, download::download);
    }

    @Test
    void download_shouldDownloadAndVerifyIntegrity() {
        context.setClientJarInfos(new ClientJarInfos(CLIENT_URL, CLIENT_SHA1, CLIENT_SIZE));

        ClientJarDownload download = new ClientJarDownload(context);
        boolean result = download.download();

        assertTrue(result);
        assertTrue(new File(tempDir + "/versions/" + VERSION + "/" + VERSION + ".jar").exists());
    }

    @Test
    void download_shouldReturnTrueWhenAlreadyUpToDate() throws Exception {
        context.setClientJarInfos(new ClientJarInfos(CLIENT_URL, CLIENT_SHA1, CLIENT_SIZE));

        ClientJarDownload download = new ClientJarDownload(context);
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