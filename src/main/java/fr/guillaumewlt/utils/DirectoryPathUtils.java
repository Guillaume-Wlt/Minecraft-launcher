package fr.guillaumewlt.utils;

import fr.guillaumewlt.exceptionhandler.LauncherException;
import fr.guillaumewlt.model.directories.LauncherDirs;
import fr.guillaumewlt.utils.console.ConsoleMessage;

public class DirectoryPathUtils {

    private DirectoryPathUtils() {}

    public static String getSelectedVersionDir(LauncherDirs dirs, String selectedVersion) { // SelectedVersionDir -> [...]/launcher/versions/<selected_version>/
        if (dirs.versionsDir().path() == null) {
            throw new LauncherException(ConsoleMessage.DIRECTORYPATH_UTILS_VERSIONS_DIR_PATH_NULL_ERR.getMessage());
        }
        if (selectedVersion == null) {
            throw new LauncherException(ConsoleMessage.SELECTEDVERSION_RECORD_NAME_NULL_ERR.getMessage());
        }
        return dirs.versionsDir().path() + selectedVersion + "/";
    }
}
