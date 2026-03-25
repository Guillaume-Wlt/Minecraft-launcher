package fr.guillaumewlt.utils;

import fr.guillaumewlt.exceptionhandler.LauncherException;

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
            throw new LauncherException("Invalid JAR path " + e);
        }
    }

    public static String getVersionsDir() { // VersionDir -> [...]/launcher/versions/
        if (DirectoryPathUtils.getLauncherDir() != null) {
            return DirectoryPathUtils.getLauncherDir() + "versions/";
        } else {
            throw new LauncherException("Launcher directory is null");
        }
    }

    public static String getSelectedVersionDir() { // SelectedVersionDir -> [...]/launcher/versions/<selected_version>/
        if (DirectoryPathUtils.getVersionsDir() == null) {
            throw new LauncherException("Versions directory is null");
        }
        if (LauncherUtils.getSelectedVersionName() == null) {
            throw new LauncherException("Version name is null");
        }
        return DirectoryPathUtils.getVersionsDir() + LauncherUtils.getSelectedVersionName() + "/";
    }
}
