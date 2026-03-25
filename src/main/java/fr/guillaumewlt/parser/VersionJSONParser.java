package fr.guillaumewlt.parser;

import fr.guillaumewlt.exceptionhandler.LauncherException;
import fr.guillaumewlt.utils.FilePathUtils;
import fr.guillaumewlt.utils.LauncherUtils;
import fr.guillaumewlt.utils.URLUtils;
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
            String downloadURL = client.getString("url");
            URLUtils.setSelectedClientJarURL(downloadURL);
            String versionHash = client.getString("sha1");
            LauncherUtils.setSelectedClientHash(versionHash);
            int clientSize = client.getInt("size");
            LauncherUtils.setSelectedClientSize(clientSize);
            System.out.println("Download URL >> " + downloadURL);
            System.out.println("Client Hash (Sha1) >> " + versionHash);
            System.out.println("Client Size >> " + clientSize / 1024 / 1024 + "Mo");
        } catch (IOException e) {
            throw new LauncherException(e.getMessage());
        }
    }
}
