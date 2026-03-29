package fr.guillaumewlt.processing.steps;

import fr.guillaumewlt.exceptionhandler.LauncherException;
import fr.guillaumewlt.utils.FilePathUtils;
import fr.guillaumewlt.utils.console.ConsoleMessage;
import fr.guillaumewlt.workflow.LauncherContext;

public class ClassPathBuildingProcess extends Processes{

    public ClassPathBuildingProcess(LauncherContext context) {
        super(context);
    }

    @Override
    public void process() {
        try {
            String classPath = FilePathUtils.buildClassPath(context, context.getLauncherDirs());
            context.setClassPath(classPath);
            System.out.println(ConsoleMessage.CLASSPATH_BUILDING_MESSAGE.format(classPath));
        } catch (LauncherException e) {
            stop(e.getMessage(), 1);
        }
    }
}
