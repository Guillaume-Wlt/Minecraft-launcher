package fr.guillaumewlt.processing.interpret;

import fr.guillaumewlt.exceptions.LauncherException;
import fr.guillaumewlt.models.assets.AssetsIndex;
import fr.guillaumewlt.parser.AssetsIndexParser;
import fr.guillaumewlt.processing.Processes;
import fr.guillaumewlt.workflow.LauncherContext;

public class InterpretClientAssetsIndex extends Processes {

    public InterpretClientAssetsIndex(LauncherContext context) {
        super(context);
    }

    @Override
    public void process() {
        try {
            AssetsIndex index = new AssetsIndexParser(context).jsonParser();
            context.setAssetsIndex(index);
        } catch (LauncherException e) {
            stop(e.getMessage(), 1);
        }
    }
}
