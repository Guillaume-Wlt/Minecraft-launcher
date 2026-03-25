package fr.guillaumewlt.processing.steps;

import fr.guillaumewlt.processing.CheckFoldersExistence;

public class InitProcess extends Processes {

    @Override
    public void process() {
        // TODO Create Folder to store values for launcher (Already downloaded versions, ...)
        CheckFoldersExistence.checkDirectories(launcherDir + "bin/"); // Create [...]/launcher/bin/
        CheckFoldersExistence.checkDirectories(launcherDir + "temp/"); // Create [...]/launcher/temp/
        CheckFoldersExistence.checkDirectories(launcherDir + "libraries/"); // Create [...]/launcher/libraries/
        CheckFoldersExistence.checkDirectories(launcherDir + "assets/"); // Create [...]/launcher/assets/

    }
}
