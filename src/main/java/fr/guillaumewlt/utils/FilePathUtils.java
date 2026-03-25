package fr.guillaumewlt.utils;

import fr.guillaumewlt.exceptionhandler.LauncherException;

public class FilePathUtils {

    private FilePathUtils() {}

    public static String getManifestPath() { // ManifestPath -> [...]/launcher/versions/<manifest_name>
        if (DirectoryPathUtils.getVersionsDir() == null) {
            throw new LauncherException("Versions directory is null");
        }
        if (LauncherUtils.getManifestName() == null) {
            throw new LauncherException("Manifest name is null");
        }
        return DirectoryPathUtils.getVersionsDir() + LauncherUtils.getManifestName();
    }

    public static String getSelectedVersionJSONPath() { // SelectedVersionJSONPath -> [...]/launcher/versions/<selected_version>/<selected_version>.json
        if (DirectoryPathUtils.getSelectedVersionDir() == null) {
            throw new LauncherException("Selected version directory is null");
        }
        if (LauncherUtils.getSelectedVersionName() == null) {
            throw new LauncherException("Version Name is null");
        }
        return DirectoryPathUtils.getSelectedVersionDir() + LauncherUtils.getSelectedVersionName() + ".json";
    }
}
