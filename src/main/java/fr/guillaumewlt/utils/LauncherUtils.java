package fr.guillaumewlt.utils;

import lombok.Getter;
import lombok.Setter;

public class LauncherUtils {

    @Getter
    @Setter
    private static String selectedVersionName;

    private LauncherUtils() {}

    public static String getManifestName() {
        return "version_manifest.json";
    }

    public static String getClientJarName() {
        return "client.jar";
    }
}
