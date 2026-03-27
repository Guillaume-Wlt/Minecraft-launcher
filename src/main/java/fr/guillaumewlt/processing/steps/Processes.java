package fr.guillaumewlt.processing.steps;

import fr.guillaumewlt.utils.DirectoryPathUtils;
import fr.guillaumewlt.utils.console.ConsoleMessage;
import fr.guillaumewlt.workflow.LauncherContext;

public abstract class Processes {

    protected final LauncherContext context;

    protected String launcherDir = DirectoryPathUtils.getLauncherDir();

    public Processes(LauncherContext context) {
        this.context = context;
    }

    public abstract void process();

    protected void stop(String reason, int exitCode) {
        System.out.println(ConsoleMessage.PROCESSES_FATAL_ERR.format(reason));
        System.exit(exitCode);
    }
}
