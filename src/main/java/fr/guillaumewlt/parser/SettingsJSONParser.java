package fr.guillaumewlt.parser;

import fr.guillaumewlt.workflow.LauncherContext;
import lombok.Getter;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

public class SettingsJSONParser {

    private final LauncherContext context;

    private File settingsFile;
    @Getter
    private String minRam;
    @Getter
    private String maxRam;
    @Getter
    private String version;
    @Getter
    private String username;

    public SettingsJSONParser(LauncherContext context) {
        this.context = context;
        if (context == null) return;
        settingsFile = new File(context.getLauncherDirs().configDir().path() + "settings.json");
    }

    public void readSettings() {
        if (settingsFile.exists()) {
            try {
                String content = Files.readString(settingsFile.toPath());
                JSONObject jsonObj = new JSONObject(content);
                if (jsonObj.has("minRam")) {
                    minRam = jsonObj.getString("minRam");
                } else {
                    System.out.println("The minRam has not been set");
                }
                if (jsonObj.has("maxRam")) {
                    maxRam = jsonObj.getString("maxRam");
                } else {
                    System.out.println("The maxRam has not been set");
                }
                if (jsonObj.has("version")) {
                    version = jsonObj.getString("version");
                } else {
                    System.out.println("The version has not been set");
                }
                if (jsonObj.has("username")) {
                    username = jsonObj.getString("username");
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }
}
