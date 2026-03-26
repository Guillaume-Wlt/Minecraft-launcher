package fr.guillaumewlt.processing.steps;

import fr.guillaumewlt.exceptionhandler.LauncherException;
import fr.guillaumewlt.processing.CheckFoldersExistence;

public class InitProcess extends Processes {

    @Override
    public void process() {
        try {
            // TODO Create Folder to store values for launcher (Already downloaded versions, ...)
            CheckFoldersExistence.checkDirectories(launcherDir + "bin/"); // Create [...]/launcher/bin/
            CheckFoldersExistence.checkDirectories(launcherDir + "temp/"); // Create [...]/launcher/temp/
        } catch (LauncherException e) {
            stop(e.getMessage(), 1);
        }
    }
}
