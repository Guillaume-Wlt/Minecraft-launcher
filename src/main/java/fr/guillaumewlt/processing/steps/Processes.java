package fr.guillaumewlt.processing.steps;

import fr.guillaumewlt.utils.DirectoryPathUtils;

public abstract class Processes {

    protected String launcherDir = DirectoryPathUtils.getLauncherDir();

    protected abstract void process();
}
