package fr.guillaumewlt.utils;

public class LauncherUtils {

    private LauncherUtils() {}

    public static String getCurrentOs() {
        String os = System.getProperty("os.name").toLowerCase();
        if (os.contains("win")) return "windows";
        if (os.contains("mac")) return "osx";
        if (os.contains("linux")) return "linux";
        return "windows"; // default value
    }

    public static String getRuntimeOSKey() {
        String os = System.getProperty("os.name").toLowerCase();
        String arch = System.getProperty("os.arch").toLowerCase();

        String archKey;
        if (arch.equals("aarch64")) archKey = "arm64";
        else if (arch.equals("x86") || arch.equals("i386")) archKey = "x86";
        else archKey = "x64"; // amd64, x86_64

        if (os.contains("win")) return "windows-" + archKey;
        if (os.contains("mac")) return "mac-os-" + (archKey.equals("arm64") ? "-arm64" : "");
        if (os.contains("linux")) return archKey.equals("x86") ? "linux-i386" : "linux";
        return "windows-x64"; // default
    }

    public static String getRuntimeJreURL() {
        return "https://launchermeta.mojang.com/v1/products/java-runtime/2ec0cc96c44e5a76b9c8b7c39df7210883d12871/all.json";
    }

    public static String getAssetsURL() {
        return "https://resources.download.minecraft.net/";
    }

    public static String getManifestURL() {
        return "https://launchermeta.mojang.com/mc/game/version_manifest.json";
    }

    public static String getRuntimeName() {
        return "runtime_manifest.json";
    }

    public static String getManifestName() {
        return "version_manifest.json";
    }

    public static String getClientJarName() {
        return "client.jar";
    }
}
