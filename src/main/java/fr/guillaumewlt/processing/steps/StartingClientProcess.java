package fr.guillaumewlt.processing.steps;

import fr.guillaumewlt.workflow.LauncherContext;

import java.io.File;
import java.io.IOException;

public class StartingClientProcess extends Processes {

    public StartingClientProcess(LauncherContext context) {
        super(context);
    }

    @Override
    public void process() {
        try {
            String version = context.getSelectedVersion().selectedVersion();
            String assetsIndex = context.getAssetsIndex().id();

            ProcessBuilder pb = new ProcessBuilder(
                    "java",
                    "-Xmx" + context.getMaxRam() + "G",
                    "-Xms" + context.getMinRam() + "M",
                    "-cp", context.getClassPath(),
                    "net.minecraft.client.main.Main",
                    "--username", context.getUsername(),
                    "--version", version,
                    "--gameDir", context.getLauncherDirs().launcherDir().path(),
                    "--assetsDir", context.getLauncherDirs().assetsDir().path(),
                    "--assetIndex", assetsIndex,
                    "--accessToken", "0",
                    "--userType", "legacy"
            );

            pb.directory(new File(context.getLauncherDirs().launcherDir().path()));
            pb.inheritIO(); // redirige stdout/stderr du jeu vers ta console
            pb.start();

        } catch (IOException e) {
            stop(e.getMessage(), 1);
        }
    }
}
