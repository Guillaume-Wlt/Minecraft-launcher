package fr.guillaumewlt.parser;

import fr.guillaumewlt.exceptionhandler.LauncherException;
import fr.guillaumewlt.model.VersionRawData;
import fr.guillaumewlt.utils.FilePathUtils;
import fr.guillaumewlt.utils.console.ConsoleMessage;
import fr.guillaumewlt.workflow.LauncherContext;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class VersionJSONParser {

    private final LauncherContext context;

    public VersionJSONParser(LauncherContext context) {
        this.context = context;
    }

    public VersionRawData jsonParser() {
        try {
            String content = Files.readString(Path.of(FilePathUtils.getSelectedVersionJSONPath(context.getSelectedVersion().selectedVersion()))); //read the content of the file.
            JSONObject versionJSON = new JSONObject(content);
            JSONObject client =  versionJSON.getJSONObject("downloads").getJSONObject("client");
            JSONArray libraries =  versionJSON.getJSONArray("libraries");

            System.out.println(ConsoleMessage.VERSION_JSON_PARSER_CLIENT_JAR_INFOS_MESSAGE.getMessage());
            System.out.println(ConsoleMessage.VERSION_JSON_PARSER_LIBRARIES_INFOS_MESSAGE.getMessage());
            return new VersionRawData(client, libraries);
        } catch (IOException e) {
            throw new LauncherException(ConsoleMessage.VERSION_JSON_PARSER_PARSING_ERR.format(e.getMessage()));
        }
    }
}
