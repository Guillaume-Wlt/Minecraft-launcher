package fr.guillaumewlt.processing.steps;

import fr.guillaumewlt.exceptionhandler.LauncherException;
import fr.guillaumewlt.processing.CheckFoldersExistence;
import fr.guillaumewlt.workflow.LauncherContext;

public class InitProcess extends Processes {

    public InitProcess(LauncherContext context) {
        super(context);
    }

    @Override
    public void process() {
        try {
            // TODO Create Folder to store values for launcher (Already downloaded versions, ...)
            CheckFoldersExistence.checkDirectories(launcherDir + "bin/"); // Create [...]/launcher/bin/
            CheckFoldersExistence.checkDirectories(launcherDir + "temp/"); // Create [...]/launcher/temp/
            CheckFoldersExistence.checkDirectories(launcherDir + "versions/");
            CheckFoldersExistence.checkDirectories(launcherDir + "libraries/");
        } catch (LauncherException e) {
            stop(e.getMessage(), 1);
        }
    }
}
