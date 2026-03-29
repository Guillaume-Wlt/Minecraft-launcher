package fr.guillaumewlt.downloads;

import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.URI;
import java.nio.file.Path;
import java.nio.file.Paths;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class VersionJSONResourceDownloadTest {

    private static final String VERSION = "1.6.4";
    private static final String URL = "https://piston-meta.mojang.com/v1/packages/b71bae449192fbbe1582ff32fb3765edf0b9b0a8/1.6.4.json";

    @Test
    void download_shouldStoreVersionJsonInResources() throws Exception {
        Path resourcesDir = Paths.get("src", "main", "resources");
        File outputFile = resourcesDir.resolve(VERSION + ".json").toFile();

        try (InputStream is = URI.create(URL).toURL().openStream();
             FileOutputStream fos = new FileOutputStream(outputFile)) {
            fos.write(is.readAllBytes());
        }

        assertTrue(outputFile.exists());
        assertTrue(outputFile.length() > 0);
    }
}