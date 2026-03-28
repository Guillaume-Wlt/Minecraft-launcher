package fr.guillaumewlt.processing.steps;

import fr.guillaumewlt.exceptionhandler.LauncherException;
import fr.guillaumewlt.model.assets.AssetInfos;
import fr.guillaumewlt.parser.AssetsInfosParser;
import fr.guillaumewlt.workflow.LauncherContext;

import java.util.List;

public class InterpretClientAssetsInfos extends Processes{

    public InterpretClientAssetsInfos(LauncherContext context) {
        super(context);
    }

    @Override
    public void process() {
        try {
            List<AssetInfos> assetsInfos = new AssetsInfosParser(context).jsonParser();
            context.setAssetsInfos(assetsInfos);
        } catch (LauncherException e) {
            stop(e.getMessage(), 1);
        }
    }
}
