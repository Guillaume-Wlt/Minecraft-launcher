package fr.guillaumewlt.utils;

import lombok.Getter;
import lombok.Setter;
import org.json.JSONObject;

public class ClientJarInfosUtils {
    @Getter
    @Setter
    private static JSONObject clientJarInfos;

    @Getter
    @Setter
    private static String selectedClientHash;

    @Getter
    @Setter
    private static int selectedClientSize;

    @Getter
    @Setter
    private static String selectedClientJarURL;

    public ClientJarInfosUtils() {}

}
