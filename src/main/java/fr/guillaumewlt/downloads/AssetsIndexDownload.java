package fr.guillaumewlt.downloads;

import fr.guillaumewlt.workflow.LauncherContext;

public class AssetsIndexDownload extends Downloads{

    public AssetsIndexDownload(LauncherContext context) {}

    @Override
    public boolean download() {
        return false;
    }

    @Override
    protected void checkRequirements() {

    }
}
