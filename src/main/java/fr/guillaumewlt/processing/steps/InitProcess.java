package fr.guillaumewlt.processing.steps;

import fr.guillaumewlt.exceptionhandler.LauncherException;
import fr.guillaumewlt.model.directory.LauncherDir;
import fr.guillaumewlt.model.directory.LauncherDirs;
import fr.guillaumewlt.processing.CheckFoldersExistence;
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
                    new LauncherDir("bin", launcherPath + "bin/"),
                    new LauncherDir("versions", launcherPath + "versions/"),
                    new LauncherDir("libraries", launcherPath + "libraries/"),
                    new LauncherDir("assets", launcherPath + "assets/"),
                    new LauncherDir("assets_indexes", launcherPath + "assets/indexes/"),
                    new LauncherDir("assets_objects", launcherPath + "assets/objects/"),
                    new LauncherDir("runtime", launcherPath + "runtime/")
            );
            context.setLauncherDirs(directories);

            CheckFoldersExistence.checkDirectories(directories.launcherDir().path()); // Create [...]/launcher/
            CheckFoldersExistence.checkDirectories(directories.binDir().path()); // Create [...]/launcher/bin/
            CheckFoldersExistence.checkDirectories(directories.versionsDir().path()); // Create [...]/launcher/versions/
            CheckFoldersExistence.checkDirectories(directories.librariesDir().path()); // Create [...]/launcher/libraries/
            CheckFoldersExistence.checkDirectories(directories.assetsDir().path()); // Create [...]/launcher/assets/
            CheckFoldersExistence.checkDirectories(directories.assetsIndexesDir().path()); // Create [...]/launcher/assets/indexes/
            CheckFoldersExistence.checkDirectories(directories.assetsObjectsDir().path()); // Create [...]/launcher/assets/objects/
            CheckFoldersExistence.checkDirectories(directories.runtimeDir().path()); // Create [...]/launcher/runtime/
        } catch (LauncherException e) {
            stop(e.getMessage(), 1);
        }
    }
}
