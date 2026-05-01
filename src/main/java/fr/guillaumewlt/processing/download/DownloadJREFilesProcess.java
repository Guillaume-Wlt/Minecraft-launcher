package fr.guillaumewlt.processing.download;

import fr.guillaumewlt.annotations.Retryable;
import fr.guillaumewlt.annotations.WorkerThread;
import fr.guillaumewlt.console.ConsoleMessage;
import fr.guillaumewlt.download.DownloadProcess;
import fr.guillaumewlt.exceptions.LauncherException;
import fr.guillaumewlt.models.JREFileInfos;
import fr.guillaumewlt.processing.Processes;
import fr.guillaumewlt.utils.DirectoryCreatorUtils;
import fr.guillaumewlt.workflow.LauncherContext;

import java.io.File;
import java.nio.file.Path;
import java.util.List;

public class DownloadJREFilesProcess extends Processes {

    private final List<JREFileInfos> jreFilesInfos;

    public DownloadJREFilesProcess(LauncherContext context) {
        super(context);
        this.jreFilesInfos = context.getJreFilesInfos();
    }

    @Retryable(attempts = 3)
    @WorkerThread
    @Override
    public void process() {
        try {
            if (jreFilesInfos == null || jreFilesInfos.isEmpty()) {
                ConsoleMessage.JREFILES_DOWNLOAD_JRE_FILES_LIST_EMPTY_ERR.throwException();
            }

            for (JREFileInfos jreFileInfos : jreFilesInfos) {
                final Path destination = Path.of(context.getLauncherDirs().runtimeDir().path(), context.getVersionRawData().javaVersion(), jreFileInfos.path());
                final String jreURL = jreFileInfos.url();
                final long jreSize = jreFileInfos.size();
                final String exceptedHash = jreFileInfos.sha1();

                if (jreFileInfos.type().equals("directory")) {
                    File newJREDir = new File(String.valueOf(destination));
                    if (!newJREDir.exists()) {
                        newJREDir.mkdirs();
                        continue;
                    }
                    continue;
                }

                checkRequirements(destination);

                new DownloadProcess(context).downloadSHA1(jreURL, destination, jreSize, jreFileInfos.path(), exceptedHash);
            }
        } catch (LauncherException e) {
            stop(e.getMessage(), 1);
        }
    }

    private void checkRequirements(final Path destination) {
        DirectoryCreatorUtils.checkDirectories(String.valueOf(destination.getParent()));
    }
}
