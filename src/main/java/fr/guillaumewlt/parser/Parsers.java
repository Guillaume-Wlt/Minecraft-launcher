package fr.guillaumewlt.parser;

import fr.guillaumewlt.utils.LauncherUtils;

public abstract class Parsers {

    protected String manifestPath = LauncherUtils.getManifestPath();

    protected abstract void jsonParser();
}
