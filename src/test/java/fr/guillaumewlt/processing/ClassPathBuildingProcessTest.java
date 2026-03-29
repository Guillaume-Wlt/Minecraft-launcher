package fr.guillaumewlt.processing;

import fr.guillaumewlt.model.LibraryInfos;
import fr.guillaumewlt.model.SelectedVersion;
import fr.guillaumewlt.model.directory.LauncherDir;
import fr.guillaumewlt.model.directory.LauncherDirs;
import fr.guillaumewlt.processing.steps.ClassPathBuildingProcess;
import fr.guillaumewlt.workflow.LauncherContext;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class ClassPathBuildingProcessTest {

    private LauncherContext context;

    @BeforeEach
    void setUp() {
        LauncherDir launcherDir  = new LauncherDir("launcher",  "/tmp/launcher/");
        LauncherDir versionsDir  = new LauncherDir("versions",  "/tmp/launcher/versions/");
        LauncherDir librariesDir = new LauncherDir("libraries", "/tmp/launcher/libraries/");
        LauncherDirs launcherDirs = new LauncherDirs(launcherDir, null, versionsDir, librariesDir, null, null, null);

        context = new LauncherContext();
        context.setLauncherDirs(launcherDirs);
        context.setSelectedVersion(new SelectedVersion("1.6.4", null));
        context.setLibrariesInfos(List.of(
                new LibraryInfos("com.mojang:authlib:1.5.21", "hash1",
                        "com/mojang/authlib/1.5.21/authlib-1.5.21.jar", 153302L,
                        "https://libraries.minecraft.net/com/mojang/authlib/1.5.21/authlib-1.5.21.jar"),
                new LibraryInfos("commons-codec:commons-codec:1.9", "hash2",
                        "commons-codec/commons-codec/1.9/commons-codec-1.9.jar", 263965L,
                        "https://libraries.minecraft.net/commons-codec/commons-codec/1.9/commons-codec-1.9.jar")
        ));
    }

    @Test
    void process_shouldBuildClassPathContainingAllLibraries() {
        new ClassPathBuildingProcess(context).process();

        String classPath = context.getClassPath();
        assertNotNull(classPath);
        assertTrue(classPath.contains("authlib-1.5.21.jar"));
        assertTrue(classPath.contains("commons-codec-1.9.jar"));
    }

    @Test
    void process_shouldEndWithClientJar() {
        new ClassPathBuildingProcess(context).process();

        String classPath = context.getClassPath();
        assertTrue(classPath.endsWith("1.6.4/1.6.4.jar"));
    }

    @Test
    void process_shouldContainLibrariesDir() {
        new ClassPathBuildingProcess(context).process();

        String classPath = context.getClassPath();
        assertTrue(classPath.contains("/tmp/launcher/libraries/"));
    }
}