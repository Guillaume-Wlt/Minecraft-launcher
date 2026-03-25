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

    public static String getManifestName() {
        return "version_manifest.json";
    }

    public static String getManifestUrl() {
        return "https://launchermeta.mojang.com/mc/game/version_manifest.json";
    }
}
