package fr.guillaumewlt.utils;

import fr.guillaumewlt.exceptionhandler.LauncherException;
import fr.guillaumewlt.model.directory.LauncherDirs;
import fr.guillaumewlt.utils.console.ConsoleMessage;

public class FilePathUtils {

    private FilePathUtils() {}

    public static String getManifestPath(LauncherDirs dirs) { // ManifestPath -> [...]/launcher/versions/<manifest_name>
        if (dirs.versionsDir().path() == null) {
            throw new LauncherException(ConsoleMessage.LAUNCHERDIRS_VERSIONS_NULL_ERR.getMessage());
        }
        if (LauncherUtils.getManifestName() == null) {
            throw new LauncherException(ConsoleMessage.LAUNCHER_UTILS_MANIFEST_NAME_NULL_ERR.getMessage());
        }
        return dirs.versionsDir().path() + LauncherUtils.getManifestName();
    }

    public static String getSelectedVersionJSONPath(LauncherDirs dirs, String selectedVersion) { // SelectedVersionJSONPath -> [...]/launcher/versions/<selected_version>/<selected_version>.json
        if (dirs.versionsDir().path() == null) {
            throw new LauncherException(ConsoleMessage.LAUNCHERDIRS_VERSIONS_NULL_ERR.getMessage());
        }
        if (selectedVersion == null) {
            throw new LauncherException(ConsoleMessage.SELECTEDVERSION_RECORD_NAME_NULL_ERR.getMessage());
        }
        return dirs.versionsDir().path() + selectedVersion + "/" + selectedVersion + ".json";
    }
}
