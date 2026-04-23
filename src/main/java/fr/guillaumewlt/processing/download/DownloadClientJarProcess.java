package fr.guillaumewlt.processing.download;

import fr.guillaumewlt.download.DownloadProcess;
import fr.guillaumewlt.exceptions.LauncherException;
import fr.guillaumewlt.processing.Processes;
import fr.guillaumewlt.workflow.LauncherContext;

import java.nio.file.Path;

public class DownloadClientJarProcess extends Processes {

    private final String selectedVersionName;

    private final String selectedClientJarURL;
    private final long selectedClientJarSize;
    private final String selectedClientJarHash;
    private final String selectedClientJarName;

    private final String selectedVersionDir;

    private final Path destination;

    public DownloadClientJarProcess(LauncherContext context) {
        super(context);
        this.selectedVersionName = context.getSelectedVersion().selectedVersion();

        this.selectedClientJarURL = context.getClientJarInfos().url();
        this.selectedClientJarSize = context.getClientJarInfos().size();
        this.selectedClientJarHash = context.getClientJarInfos().sha1();
        this.selectedClientJarName = selectedVersionName + ".jar";

        this.selectedVersionDir = context.getLauncherDirs().versionsDir().path() + selectedVersionName + "/";

        this.destination = Path.of(selectedVersionDir, selectedVersionName + ".jar");
    }

    @Override
    public void process() {
        try {
            new DownloadProcess(context).downloadSHA1(selectedClientJarURL, destination, selectedClientJarSize, selectedClientJarName, selectedClientJarHash);
        } catch (LauncherException e) {
            stop(e.getMessage(), 1);
        }
    }
}
