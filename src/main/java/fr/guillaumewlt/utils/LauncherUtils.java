package fr.guillaumewlt.utils;

public class LauncherUtils {

    private LauncherUtils() {}

    public static String currentOs() {
        String os = System.getProperty("os.name").toLowerCase();
        if (os.contains("win")) return "windows";
        if (os.contains("mac")) return "osx";
        if (os.contains("linux")) return "linux";
        return "windows"; // default value
    }

    public static String getAssetsURL() {
        return "https://resources.download.minecraft.net/";
    }

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
