package fr.guillaumewlt.processing.steps;

import fr.guillaumewlt.exceptionhandler.LauncherException;
import fr.guillaumewlt.model.ClientJarInfos;
import fr.guillaumewlt.parser.ClientJarInfosParser;
import fr.guillaumewlt.workflow.LauncherContext;

public class InterpretClientJarInfos extends Processes{

    public InterpretClientJarInfos(LauncherContext context){
        super(context);
    }

    @Override
    public void process() {
        try {
            ClientJarInfos info = new ClientJarInfosParser(context).jsonParser();
            context.setClientJarInfos(info);
        } catch (LauncherException e) {
            stop(e.getMessage(), 1);
        }
    }
}
