package fr.guillaumewlt.parser;

import fr.guillaumewlt.utils.FilePathUtils;

public abstract class Parsers {

    protected String manifestPath = FilePathUtils.getManifestPath();

    protected abstract void jsonParser();
}
