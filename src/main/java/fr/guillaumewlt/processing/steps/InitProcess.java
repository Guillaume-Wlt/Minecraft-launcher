package fr.guillaumewlt.processing.steps;

import fr.guillaumewlt.exceptions.LauncherException;
import fr.guillaumewlt.models.directory.LauncherDir;
import fr.guillaumewlt.models.directory.LauncherDirs;
import fr.guillaumewlt.processing.Processes;
import fr.guillaumewlt.utils.DirectoryCreatorUtils;
import fr.guillaumewlt.workflow.LauncherContext;

import java.io.File;
import java.net.URISyntaxException;

public class InitProcess extends Processes {

    private String launcherPath;

    public InitProcess(LauncherContext context) {
        super(context);
        try {
            launcherPath = new File(InitProcess.class.getProtectionDomain().getCodeSource().getLocation().toURI()).getParent() + "/launcher/";
        } catch (URISyntaxException e) {
            stop(e.getMessage(), 1);
        }
    }

    @Override
    public void process() {
        try {
            if (launcherPath == null) {
                stop("Launcher path is null", 1);
            }
            LauncherDirs directories = new LauncherDirs(
                    new LauncherDir("Launcher", launcherPath),
                    new LauncherDir("config", launcherPath + "config/"),
                    new LauncherDir("bin", launcherPath + "bin/"),
                    new LauncherDir("versions", launcherPath + "versions/"),
                    new LauncherDir("libraries", launcherPath + "libraries/"),
                    new LauncherDir("assets", launcherPath + "assets/"),
                    new LauncherDir("assets_indexes", launcherPath + "assets/indexes/"),
                    new LauncherDir("assets_objects", launcherPath + "assets/objects/"),
                    new LauncherDir("runtime", launcherPath + "runtime/")
            );
            context.setLauncherDirs(directories);

            for (LauncherDir dir : directories.getAllDirs()) {
                DirectoryCreatorUtils.checkDirectories(dir.path()); // Check pour l'existence de chaque dossier sur le disque
            }
        } catch (LauncherException e) {
            stop(e.getMessage(), 1);
        }
    }
}
