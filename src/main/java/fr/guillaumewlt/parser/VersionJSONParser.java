package fr.guillaumewlt.parser;

import fr.guillaumewlt.exceptionhandler.LauncherException;
import fr.guillaumewlt.utils.LauncherUtils;
import org.json.JSONObject;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class VersionJSONParser extends Parsers {

    @Override
    public void jsonParser() {
        try {
            String content = Files.readString(Path.of(LauncherUtils.getSelectedVersionJSONPath())); //read the content of the file.
            JSONObject versionJSON = new JSONObject(content);
            JSONObject client =  versionJSON.getJSONObject("downloads").getJSONObject("client");
            String downloadURL = client.getString("url");
            System.out.println(downloadURL);

        } catch (IOException e) {
            throw new LauncherException(e.getMessage());
        }
    }
}
