package fr.guillaumewlt.processing.interpret;

import fr.guillaumewlt.exceptions.LauncherException;
import fr.guillaumewlt.models.JREFileInfos;
import fr.guillaumewlt.parser.JREManifestParser;
import fr.guillaumewlt.processing.Processes;
import fr.guillaumewlt.workflow.LauncherContext;

import java.util.List;

public class InterpretJREManifestProcess extends Processes {

    public InterpretJREManifestProcess(LauncherContext context) {
        super(context);
    }

    @Override
    public void process() {
        try {
            List<JREFileInfos> jreFilesInfos = new JREManifestParser(context).jsonParser();
            context.setJreFilesInfos(jreFilesInfos);
        } catch (LauncherException e) {
            stop(e.getMessage(), 1);
        }
    }
}
