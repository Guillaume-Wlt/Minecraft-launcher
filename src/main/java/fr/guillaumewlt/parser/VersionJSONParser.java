package fr.guillaumewlt.parser;

import fr.guillaumewlt.exceptionhandler.LauncherException;
import fr.guillaumewlt.processing.json.ClientJarInfosProcess;
import fr.guillaumewlt.processing.json.LibrariesInfosProcess;
import fr.guillaumewlt.utils.FilePathUtils;
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
            new ClientJarInfosProcess(client).processClientJarInfos(); // Process the infos of the Client Jar
            JSONArray libraries =  versionJSON.getJSONArray("libraries");
            new LibrariesInfosProcess(libraries).processLibrariesInfos(); // Process the infos of the differents libraries
        } catch (IOException e) {
            throw new LauncherException(e.getMessage());
        }
    }
}
