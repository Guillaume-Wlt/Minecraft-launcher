package fr.guillaumewlt.processing;

import fr.guillaumewlt.console.ConsoleMessage;
import fr.guillaumewlt.workflow.LauncherContext;

public abstract class Processes {

    protected final LauncherContext context;

    public Processes(LauncherContext context) {
        this.context = context;
    }

    public abstract void process();

    protected void stop(String reason, int exitCode) {
        ConsoleMessage.PROCESSES_FATAL_ERR.errPrintln(reason);
        System.exit(exitCode);
    }
}
