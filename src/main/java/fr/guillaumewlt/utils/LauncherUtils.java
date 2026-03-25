package fr.guillaumewlt.utils;

import fr.guillaumewlt.exceptionhandler.LauncherException;
import lombok.Getter;
import lombok.Setter;

import java.io.File;
import java.net.URISyntaxException;

public class LauncherUtils {

    @Getter
    @Setter
    private static String versionName;

    @Getter
    @Setter
    private static String versionUrl;

    private LauncherUtils() {}

    public static String getLauncherDir() { // LauncherDir -> [...]/launcher/
        try {
            return new File(LauncherUtils.class
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
        if (LauncherUtils.getLauncherDir() != null) {
            return LauncherUtils.getLauncherDir() + "versions/";
        } else {
            throw new LauncherException("Launcher directory is null");
        }
    }

    public static String getManifestPath() { // ManifestPath -> [...]/launcher/versions/<manifest_name>
        if (LauncherUtils.getVersionsDir() == null) {
            throw new LauncherException("Versions directory is null");
        }
        if (LauncherUtils.getManifestName() == null) {
            throw new LauncherException("Manifest name is null");
        }
        return LauncherUtils.getVersionsDir() + LauncherUtils.getManifestName();
    }

    public static String getSelectedVersionDir() { // SelectedVersionDir -> [...]/launcher/versions/<selected_version>/
        if (LauncherUtils.getVersionsDir() == null) {
            throw new LauncherException("Versions directory is null");
        }
        if (LauncherUtils.getVersionName() == null) {
            throw new LauncherException("Version name is null");
        }
        return LauncherUtils.getVersionsDir() + LauncherUtils.getVersionName() + "/";
    }

    public static String getSelectedVersionJSONPath() {
        if (LauncherUtils.getSelectedVersionDir() == null) {
            throw new LauncherException("Selected version directory is null");
        }
        if (LauncherUtils.getVersionsDir() == null) {
            throw new LauncherException("Version Name is null");
        }
        return LauncherUtils.getSelectedVersionDir() + LauncherUtils.getVersionName() + ".json";
    }

    public static String getManifestName() {
        return "version_manifest.json";
    }

    public static String getManifestUrl() {
        return "https://launchermeta.mojang.com/mc/game/version_manifest.json";
    }
}
