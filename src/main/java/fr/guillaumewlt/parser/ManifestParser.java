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
import java.util.Scanner;

public class ManifestParser {

    private final LauncherContext context;

    public ManifestParser(LauncherContext context) {
        this.context = context;
    }

    public SelectedVersion jsonparser() {
        System.out.println(ConsoleMessage.MANIFEST_PARSER_SCANNER_INPUT_MESSAGE.getMessage());
        Scanner scanner = new Scanner(System.in); // scanner version // Define by user
        String selectedVersion = scanner.nextLine();

        try {
            String content = Files.readString(Path.of(FilePathUtils.getManifestPath()));
            JSONObject manifest = new JSONObject(content);
            JSONArray versions = manifest.getJSONArray("versions");
            for (int i = 0; i < versions.length(); i++) {
                JSONObject version = versions.getJSONObject(i);
                String id = version.getString("id");
                if (id.equals(selectedVersion)) {
                    String url = version.getString("url");
                    System.out.println(ConsoleMessage.MANIFEST_PARSER_URL_SET_MESSAGE.format(url));
                    return new SelectedVersion(selectedVersion, url);
                }
            }
            throw new LauncherException(ConsoleMessage.MANIFEST_PARSER_SCANNER_INPUT_VERSION_NOT_FOUND_ERR.format(selectedVersion));
        } catch (IOException e) {
            throw new LauncherException(e.getMessage());
        }
    }
}
