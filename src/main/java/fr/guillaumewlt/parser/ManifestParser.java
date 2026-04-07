package fr.guillaumewlt.parser;

import fr.guillaumewlt.exceptionhandler.LauncherException;
import fr.guillaumewlt.model.SelectedVersion;
import fr.guillaumewlt.utils.FilePathUtils;
import fr.guillaumewlt.utils.console.ConsoleMessage;
import fr.guillaumewlt.workflow.LauncherContext;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class ManifestParser {

    private final LauncherContext context;
    private String content;
    private JSONObject manifest;
    private JSONArray versions;

    public ManifestParser(LauncherContext context) {
        this.context = context;
        try {
            this.content = Files.readString(Path.of(FilePathUtils.getManifestPath(context.getLauncherDirs())));
            this.manifest = new JSONObject(content);
            this.versions = manifest.getJSONArray("versions");
        } catch (IOException e) {
            throw new LauncherException(e.getMessage());
        }
    }

    public List<String> getVersions() {
        List<String> versionsList = new ArrayList<>();
        for (int i = 0; i < versions.length(); i++) {
            JSONObject version = versions.getJSONObject(i);
            String id = version.getString("id");
            versionsList.add(id);
        }
        return versionsList;
    }

    public SelectedVersion jsonparser(String selectedVersion) {
        for (int i = 0; i < versions.length(); i++) {
            JSONObject version = versions.getJSONObject(i);
            String id = version.getString("id");
            if (id.equals(selectedVersion)) {
                String url = version.getString("url");
                ConsoleMessage.MANIFEST_PARSER_URL_SET_MESSAGE.outPrintln(url);
                return new SelectedVersion(selectedVersion, url);
            }
        }
        ConsoleMessage.MANIFEST_PARSER_SCANNER_INPUT_VERSION_NOT_FOUND_ERR.throwException(selectedVersion);
        return null;
    }
}
