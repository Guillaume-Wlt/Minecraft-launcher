package fr.guillaumewlt.utils;

import fr.guillaumewlt.exceptionhandler.LauncherException;
import fr.guillaumewlt.utils.console.ConsoleMessage;

public class FilePathUtils {

    private FilePathUtils() {}

    public static String getManifestPath() { // ManifestPath -> [...]/launcher/versions/<manifest_name>
        if (DirectoryPathUtils.getVersionsDir() == null) {
            throw new LauncherException(ConsoleMessage.DIRECTORYPATH_UTILS_VERSIONS_DIR_PATH_NULL_ERR.getMessage());
        }
        if (LauncherUtils.getManifestName() == null) {
            throw new LauncherException(ConsoleMessage.LAUNCHER_UTILS_MANIFEST_NAME_NULL_ERR.getMessage());
        }
        return DirectoryPathUtils.getVersionsDir() + LauncherUtils.getManifestName();
    }

    public static String getSelectedVersionJSONPath(String selectedVersion) { // SelectedVersionJSONPath -> [...]/launcher/versions/<selected_version>/<selected_version>.json
        if (selectedVersion == null) {
            throw new LauncherException(ConsoleMessage.LAUNCHER_UTILS_SELECTED_VERSION_NAME_NULL_ERR.getMessage());
        }
        if (DirectoryPathUtils.getSelectedVersionDir(selectedVersion) == null) {
            throw new LauncherException(ConsoleMessage.DIRECTORYPATH_UTILS_SELECTED_VERSION_DIR_PATH_NULL_ERR.getMessage());
        }
        return DirectoryPathUtils.getSelectedVersionDir(selectedVersion) + selectedVersion + ".json";
    }
}
