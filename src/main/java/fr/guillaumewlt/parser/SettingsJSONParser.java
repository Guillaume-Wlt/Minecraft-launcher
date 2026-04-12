package fr.guillaumewlt.parser;

import fr.guillaumewlt.workflow.LauncherContext;
import lombok.AccessLevel;
import lombok.Getter;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

@Getter
public class SettingsJSONParser {
    @Getter(AccessLevel.NONE)
    private File settingsFile;

    private String minRam;
    private String maxRam;
    private String version;
    private String username;
    private int volume = 30;
    private boolean videoPaused = false;

    public SettingsJSONParser(LauncherContext context) {
        if (context == null) return;
        settingsFile = new File(context.getLauncherDirs().configDir().path() + "settings.json");
    }

    public void readSettings() {
        if (settingsFile.exists()) {
            try {
                String content = Files.readString(settingsFile.toPath());
                JSONObject jsonObj = new JSONObject(content);
                minRam = jsonObj.optString("minRam", "512");
                maxRam = jsonObj.optString("maxRam", "2048");
                version = jsonObj.optString("version", null);
                username = jsonObj.optString("username", "Player");
                volume = jsonObj.optInt("volume", 30);
                videoPaused = jsonObj.optBoolean("videoPaused", false);
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }
}
