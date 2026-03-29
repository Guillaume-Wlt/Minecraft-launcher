package fr.guillaumewlt.parser;

import fr.guillaumewlt.model.assets.AssetInfos;
import fr.guillaumewlt.model.assets.AssetsIndex;
import fr.guillaumewlt.model.directory.LauncherDir;
import fr.guillaumewlt.model.directory.LauncherDirs;
import fr.guillaumewlt.workflow.LauncherContext;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.FileWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Comparator;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class AssetsInfosParserTest {

    private static final String ASSETS_ID = "test-index";

    // Minimal assets index JSON
    private static final String ASSETS_INDEX_JSON = "{"
            + "\"objects\": {"
            + "\"minecraft/sounds/mob/chicken/step1.ogg\": {"
            + "\"hash\": \"9b22f72c4e43c8a614ada91e5d5fbb3bcdb73f16\","
            + "\"size\": 3218"
            + "},"
            + "\"minecraft/sounds/mob/cow/hurt1.ogg\": {"
            + "\"hash\": \"a1aa83e6a3b9f63a18b0f948b0c7e1450fc94f84\","
            + "\"size\": 7217"
            + "}"
            + "}"
            + "}";

    private LauncherContext context;
    private Path tempDir;

    @BeforeEach
    void setUp() throws Exception {
        tempDir = Files.createTempDirectory("launcher-test-assets-parser-");

        // AssetsInfosParser reads assetsIndexesDir + assetsIndex.id() + ".json"
        Files.createDirectories(tempDir.resolve("assets/indexes"));
        File indexFile = tempDir.resolve("assets/indexes/" + ASSETS_ID + ".json").toFile();
        try (FileWriter fw = new FileWriter(indexFile)) {
            fw.write(ASSETS_INDEX_JSON);
        }

        LauncherDir launcherDir       = new LauncherDir("launcher",       tempDir + "/");
        LauncherDir assetsIndexesDir  = new LauncherDir("indexes",        tempDir + "/assets/indexes/");
        LauncherDirs launcherDirs     = new LauncherDirs(launcherDir, null, null, null, null, assetsIndexesDir, null);

        context = new LauncherContext();
        context.setLauncherDirs(launcherDirs);
        context.setAssetsIndex(new AssetsIndex(ASSETS_ID, "fakehash", 100L, 200L, "https://example.com"));
    }

    @Test
    void jsonParser_shouldReturnAssetsList() {
        List<AssetInfos> result = new AssetsInfosParser(context).jsonParser();

        assertNotNull(result);
        assertEquals(2, result.size());
    }

    @Test
    void jsonParser_shouldContainCorrectHashAndSize() {
        List<AssetInfos> result = new AssetsInfosParser(context).jsonParser();

        AssetInfos chicken = result.stream()
                .filter(a -> a.hash().equals("9b22f72c4e43c8a614ada91e5d5fbb3bcdb73f16"))
                .findFirst()
                .orElse(null);

        assertNotNull(chicken);
        assertEquals(3218L, chicken.size());
    }

    @AfterEach
    void tearDown() throws Exception {
        Files.walk(tempDir)
                .sorted(Comparator.reverseOrder())
                .map(Path::toFile)
                .forEach(File::delete);
    }
}