package fr.guillaumewlt.utils;

import fr.guillaumewlt.model.directory.LauncherDirs;
import fr.guillaumewlt.utils.console.ConsoleMessage;

public class DirectoryPathUtils {

    private DirectoryPathUtils() {}

    public static String getSelectedVersionDir(LauncherDirs dirs, String selectedVersion) { // SelectedVersionDir -> [...]/launcher/versions/<selected_version>/
        if (dirs.versionsDir().path() == null) {
            ConsoleMessage.LAUNCHERDIRS_VERSIONS_NULL_ERR.throwException();
        }
        if (selectedVersion == null) {
            ConsoleMessage.SELECTEDVERSION_RECORD_NAME_NULL_ERR.throwException();
        }
        return dirs.versionsDir().path() + selectedVersion + "/";
    }
}
