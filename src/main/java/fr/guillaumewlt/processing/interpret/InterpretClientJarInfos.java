package fr.guillaumewlt.processing.interpret;

import fr.guillaumewlt.exceptions.LauncherException;
import fr.guillaumewlt.models.ClientJarInfos;
import fr.guillaumewlt.parser.ClientJarInfosParser;
import fr.guillaumewlt.processing.Processes;
import fr.guillaumewlt.workflow.LauncherContext;

public class InterpretClientJarInfos extends Processes {

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
