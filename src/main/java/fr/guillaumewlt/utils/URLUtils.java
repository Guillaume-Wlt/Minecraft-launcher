package fr.guillaumewlt.utils;

import lombok.Getter;
import lombok.Setter;

public class URLUtils {
    @Getter
    @Setter
    private static String selectedVersionURL;

    private URLUtils() {}

    public static String getManifestURL() {
        return "https://launchermeta.mojang.com/mc/game/version_manifest.json";
    }
}
