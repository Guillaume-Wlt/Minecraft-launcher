package fr.guillaumewlt.processing.steps;

import fr.guillaumewlt.utils.DirectoryPathUtils;
import fr.guillaumewlt.utils.console.ConsoleMessage;

public abstract class Processes {

    protected String launcherDir = DirectoryPathUtils.getLauncherDir();

    protected abstract void process();

    protected void stop(String reason, int exitCode) {
        System.out.println(ConsoleMessage.PROCESSES_FATAL_ERR.format(reason));
        System.exit(exitCode);
    }
}
