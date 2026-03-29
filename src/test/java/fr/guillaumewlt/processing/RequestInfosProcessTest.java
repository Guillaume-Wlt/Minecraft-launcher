package fr.guillaumewlt.processing;

import fr.guillaumewlt.model.directory.LauncherDir;
import fr.guillaumewlt.model.directory.LauncherDirs;
import fr.guillaumewlt.processing.steps.RequestInfosProcess;
import fr.guillaumewlt.workflow.LauncherContext;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.util.Scanner;

import static org.junit.jupiter.api.Assertions.*;

public class RequestInfosProcessTest {

    private LauncherContext context;

    @BeforeEach
    void setUp() {
        LauncherDir launcherDir = new LauncherDir("launcher", "/tmp/launcher/");
        LauncherDirs launcherDirs = new LauncherDirs(launcherDir, null, null, null, null, null, null);

        context = new LauncherContext();
        context.setLauncherDirs(launcherDirs);
    }

    private void setInput(String... lines) {
        String input = String.join("\n", lines) + "\n";
        context.setScanner(new Scanner(new ByteArrayInputStream(input.getBytes())));
    }

    @Test
    void process_shouldUseProvidedUsername() {
        setInput("Guillaume", "1024", "4");

        new RequestInfosProcess(context).process();

        assertEquals("Guillaume", context.getUsername());
    }

    @Test
    void process_shouldUseDefaultUsername_whenInputIsEmpty() {
        setInput("", "512", "2");

        new RequestInfosProcess(context).process();

        assertEquals("Player", context.getUsername());
    }

    @Test
    void process_shouldUseDefaultMinRam_whenInputIsEmpty() {
        setInput("Guillaume", "", "2");

        new RequestInfosProcess(context).process();

        assertEquals("512", context.getMinRam());
    }

    @Test
    void process_shouldUseDefaultMaxRam_whenInputIsEmpty() {
        setInput("Guillaume", "512", "");

        new RequestInfosProcess(context).process();

        assertEquals("2", context.getMaxRam());
    }

    @Test
    void process_shouldStoreProvidedRamValues() {
        setInput("Guillaume", "1024", "8");

        new RequestInfosProcess(context).process();

        assertEquals("1024", context.getMinRam());
        assertEquals("8", context.getMaxRam());
    }
}