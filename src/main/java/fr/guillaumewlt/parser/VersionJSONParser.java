package fr.guillaumewlt.parser;

import fr.guillaumewlt.exceptionhandler.LauncherException;
import fr.guillaumewlt.utils.ClientJarInfosUtils;
import fr.guillaumewlt.utils.FilePathUtils;
import fr.guillaumewlt.utils.LibrariesInfosUtils;
import fr.guillaumewlt.utils.console.ConsoleMessage;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class VersionJSONParser extends Parsers {

    @Override
    public void jsonParser() {
        try {
            String content = Files.readString(Path.of(FilePathUtils.getSelectedVersionJSONPath())); //read the content of the file.
            JSONObject versionJSON = new JSONObject(content);
            JSONObject client =  versionJSON.getJSONObject("downloads").getJSONObject("client");
            ClientJarInfosUtils.setClientJarInfos(client); // Store the infos the client Jar
            System.out.println(ConsoleMessage.VERSION_JSON_PARSER_CLIENT_JAR_INFOS_MESSAGE.getMessage());
            JSONArray libraries =  versionJSON.getJSONArray("libraries");
            LibrariesInfosUtils.setLibraries(libraries); // Store the infos of the Libraries
            System.out.println(ConsoleMessage.VERSION_JSON_PARSER_LIBRARIES_INFOS_MESSAGE.getMessage());
        } catch (IOException e) {
            throw new LauncherException(ConsoleMessage.VERSION_JSON_PARSER_PARSING_ERR.format(e.getMessage()));
        }
    }
}
