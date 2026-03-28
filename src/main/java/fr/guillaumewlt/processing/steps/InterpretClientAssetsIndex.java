package fr.guillaumewlt.processing.steps;

import fr.guillaumewlt.exceptionhandler.LauncherException;
import fr.guillaumewlt.model.assets.AssetsIndex;
import fr.guillaumewlt.parser.AssetsIndexParser;
import fr.guillaumewlt.workflow.LauncherContext;

public class InterpretClientAssetsIndex extends Processes{

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
