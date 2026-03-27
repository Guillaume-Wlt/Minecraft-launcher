package fr.guillaumewlt.processing.steps;

import fr.guillaumewlt.exceptionhandler.LauncherException;
import fr.guillaumewlt.model.ClientJarInfo;
import fr.guillaumewlt.parser.ClientJarInfosParser;
import fr.guillaumewlt.workflow.LauncherContext;

public class InterpretClientJarInfos extends Processes{

    private final LauncherContext context;

    public InterpretClientJarInfos(LauncherContext context){
        this.context = context;
    }

    @Override
    public void process() {
        try {
            ClientJarInfo info = new ClientJarInfosParser(context).jsonParser();
            context.setClientJarInfo(info);
        } catch (LauncherException e) {
            stop(e.getMessage(), 1);
        }
    }
}
