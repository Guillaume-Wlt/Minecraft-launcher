package fr.guillaumewlt.utils;

import fr.guillaumewlt.exceptionhandler.LauncherException;
import fr.guillaumewlt.utils.console.ConsoleMessage;

import java.io.File;
import java.net.URISyntaxException;

public class DirectoryPathUtils {

    private DirectoryPathUtils() {}

    public static String getLauncherDir() { // LauncherDir -> [...]/launcher/
        try {
            return new File(DirectoryPathUtils.class
                    .getProtectionDomain()
                    .getCodeSource()
                    .getLocation()
                    .toURI())
                    .getParent() + "/launcher/";
        } catch (URISyntaxException e) {
            throw new LauncherException(ConsoleMessage.DIRECTORYPATH_UTILS_LAUNCHER_DIR_INVALID_PATH_ERR.format(e.getMessage()));
        }
    }

    public static String getVersionsDir() { // VersionDir -> [...]/launcher/versions/
        if (DirectoryPathUtils.getLauncherDir() != null) {
            return DirectoryPathUtils.getLauncherDir() + "versions/";
        } else {
            throw new LauncherException(ConsoleMessage.DIRECTORYPATH_UTILS_LAUNCHER_DIR_PATH_NULL_ERR.getMessage());
        }
    }

    public static String getSelectedVersionDir(String selectedVersion) { // SelectedVersionDir -> [...]/launcher/versions/<selected_version>/
        if (DirectoryPathUtils.getVersionsDir() == null) {
            throw new LauncherException(ConsoleMessage.DIRECTORYPATH_UTILS_VERSIONS_DIR_PATH_NULL_ERR.getMessage());
        }
        if (selectedVersion == null) {
            throw new LauncherException("Version name is null");
        }
        return DirectoryPathUtils.getVersionsDir() + selectedVersion + "/";
    }
}
