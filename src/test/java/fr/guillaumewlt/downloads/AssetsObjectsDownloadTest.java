package fr.guillaumewlt.downloads;

import fr.guillaumewlt.exceptionhandler.LauncherException;
import fr.guillaumewlt.model.assets.AssetInfos;
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
import java.util.Comparator;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class AssetsObjectsDownloadTest {

    // Small asset from Minecraft 1.6 assets
    private static final AssetInfos TEST_ASSET = new AssetInfos(
            "minecraft/sounds/mob/chicken/step1.ogg",
            "9b22f72c4e43c8a614ada91e5d5fbb3bcdb73f16",
            3218L
    );

    private LauncherContext context;
    private Path tempDir;

    @BeforeEach
    void setUp() throws Exception {
        tempDir = Files.createTempDirectory("launcher-test-assets-");

        LauncherDir launcherDir      = new LauncherDir("launcher",       tempDir + "/");
        LauncherDir assetsObjectsDir = new LauncherDir("objects",        tempDir + "/assets/objects/");
        LauncherDirs launcherDirs    = new LauncherDirs(launcherDir, null, null, null, null, null, assetsObjectsDir);

        context = new LauncherContext();
        context.setLauncherDirs(launcherDirs);
    }

    @Test
    void download_shouldThrowWhenAssetsListIsNull() {
        context.setAssetsInfos(null);

        AssetsObjectsDownload download = new AssetsObjectsDownload(context);
        assertThrows(LauncherException.class, download::download);
    }

    @Test
    void download_shouldThrowWhenAssetsListIsEmpty() {
        context.setAssetsInfos(Collections.emptyList());

        AssetsObjectsDownload download = new AssetsObjectsDownload(context);
        assertThrows(LauncherException.class, download::download);
    }

    @Test
    void download_shouldDownloadAsset() {
        context.setAssetsInfos(List.of(TEST_ASSET));

        AssetsObjectsDownload download = new AssetsObjectsDownload(context);
        boolean result = download.download();

        String expectedPath = tempDir + "/assets/objects/"
                + TEST_ASSET.hash().substring(0, 2) + "/" + TEST_ASSET.hash();
        assertTrue(result);
        assertTrue(new File(expectedPath).exists());
    }

    @Test
    void download_shouldReturnTrueWhenAssetAlreadyUpToDate() {
        context.setAssetsInfos(List.of(TEST_ASSET));

        AssetsObjectsDownload download = new AssetsObjectsDownload(context);
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