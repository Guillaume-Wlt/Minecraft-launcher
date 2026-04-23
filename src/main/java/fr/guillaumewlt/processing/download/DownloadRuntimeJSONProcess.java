package fr.guillaumewlt.processing.download;

import fr.guillaumewlt.download.DownloadProcess;
import fr.guillaumewlt.exceptions.LauncherException;
import fr.guillaumewlt.processing.Processes;
import fr.guillaumewlt.utils.ConstantUtils;
import fr.guillaumewlt.workflow.LauncherContext;

import java.nio.file.Path;

public class DownloadRuntimeJSONProcess extends Processes {

    private final String runtimeURL;
    private final String runtimeName;
    private final Path destination;

    public DownloadRuntimeJSONProcess(LauncherContext context) {
        super(context);
        this.runtimeURL = ConstantUtils.RUNTIME_JRE_URL;
        this.runtimeName = ConstantUtils.RUNTIME_NAME;
        this.destination = Path.of(context.getLauncherDirs().runtimeDir().path(), runtimeName);
    }

    @Override
    public void process() {
        try {
            new DownloadProcess(context).downloadMD5(runtimeURL, destination);
        } catch (LauncherException e) {
            stop(e.getMessage(), 1);
        }
    }
}
