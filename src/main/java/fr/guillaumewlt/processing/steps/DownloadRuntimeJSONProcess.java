package fr.guillaumewlt.processing.steps;

import fr.guillaumewlt.downloads.DownloadProcess;
import fr.guillaumewlt.exceptionhandler.LauncherException;
import fr.guillaumewlt.utils.LauncherUtils;
import fr.guillaumewlt.workflow.LauncherContext;

import java.nio.file.Path;

public class DownloadRuntimeJSONProcess extends Processes{

    private final String runtimeURL;
    private final String runtimeName;
    private final Path destination;

    public DownloadRuntimeJSONProcess(LauncherContext context) {
        super(context);
        this.runtimeURL = LauncherUtils.getRuntimeJreURL();
        this.runtimeName = LauncherUtils.getRuntimeName();
        this.destination = Path.of(context.getLauncherDirs().runtimeDir().path(), runtimeName);
    }

    @Override
    public void process() {
        try {
            new DownloadProcess(context).downloadToFile(runtimeURL, destination, 0, runtimeName, null, DownloadProcess.HashType.MD5);
        } catch (LauncherException e) {
            stop(e.getMessage(), 1);
        }
    }
}
