package fr.guillaumewlt.utils;

public class LauncherUtils {

    private LauncherUtils() {}

    public static String getManifestURL() {
        return "https://launchermeta.mojang.com/mc/game/version_manifest.json";
    }

    public static String getManifestName() {
        return "version_manifest.json";
    }

    public static String getClientJarName() {
        return "client.jar";
    }
}
