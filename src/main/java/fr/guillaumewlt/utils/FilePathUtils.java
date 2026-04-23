package fr.guillaumewlt.utils;

import fr.guillaumewlt.console.ConsoleMessage;
import fr.guillaumewlt.models.LibraryInfos;
import fr.guillaumewlt.models.directory.LauncherDirs;
import fr.guillaumewlt.workflow.LauncherContext;

public class FilePathUtils {

    private FilePathUtils() {}

    public static String getManifestPath(LauncherDirs dirs) { // ManifestPath -> [...]/launcher/versions/<manifest_name>
        if (dirs.versionsDir().path() == null) {
            ConsoleMessage.LAUNCHERDIRS_VERSIONS_NULL_ERR.throwException();
        }
        if (ConstantUtils.MANIFEST_NAME == null) {
            ConsoleMessage.LAUNCHER_UTILS_MANIFEST_NAME_NULL_ERR.throwException();
        }
        return dirs.versionsDir().path() + ConstantUtils.MANIFEST_NAME;
    }

    public static String getSelectedVersionJSONPath(LauncherDirs dirs, String selectedVersion) { // SelectedVersionJSONPath -> [...]/launcher/versions/<selected_version>/<selected_version>.json
        if (dirs.versionsDir().path() == null) {
            ConsoleMessage.LAUNCHERDIRS_VERSIONS_NULL_ERR.throwException();
        }
        if (selectedVersion == null) {
            ConsoleMessage.SELECTEDVERSION_RECORD_NAME_NULL_ERR.throwException();
        }
        return dirs.versionsDir().path() + selectedVersion + "/" + selectedVersion + ".json";
    }

    public static String buildClassPath(LauncherContext context, LauncherDirs dirs) {
        String separator = System.getProperty("path.separator");
        StringBuilder classPath = new StringBuilder();

        for (LibraryInfos library : context.getLibrariesInfos()) {
            if (!library.libType().equals("native")) {
                classPath.append(context.getLauncherDirs().librariesDir().path());
                classPath.append(library.path());
                classPath.append(separator);
            }
        }

        // Client JAR
        String version = context.getSelectedVersion().selectedVersion();
        classPath.append(context.getLauncherDirs().versionsDir().path());
        classPath.append(version).append("/").append(version).append(".jar");

        return classPath.toString();
    }
}
