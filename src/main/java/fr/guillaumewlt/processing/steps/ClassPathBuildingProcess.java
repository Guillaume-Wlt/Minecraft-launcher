package fr.guillaumewlt.processing.steps;

import fr.guillaumewlt.console.ConsoleMessage;
import fr.guillaumewlt.exceptions.LauncherException;
import fr.guillaumewlt.processing.Processes;
import fr.guillaumewlt.utils.FilePathUtils;
import fr.guillaumewlt.workflow.LauncherContext;

public class ClassPathBuildingProcess extends Processes {

    public ClassPathBuildingProcess(LauncherContext context) {
        super(context);
    }

    @Override
    public void process() {
        try {
            String classPath = FilePathUtils.buildClassPath(context, context.getLauncherDirs());
            context.setClassPath(classPath);
            ConsoleMessage.CLASSPATH_BUILDING_MESSAGE.outPrintln(classPath);
        } catch (LauncherException e) {
            stop(e.getMessage(), 1);
        }
    }
}
