package fr.guillaumewlt.parser;

import fr.guillaumewlt.exceptionhandler.LauncherException;
import fr.guillaumewlt.utils.LauncherUtils;
import fr.guillaumewlt.utils.URLUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Scanner;

public class ManifestParser extends Parsers {

    @Override
    public void jsonParser() {
        System.out.println("Select version to download : ");
        Scanner scanner = new Scanner(System.in);
        String scVersion = scanner.nextLine();
        LauncherUtils.setSelectedVersionName(scVersion); // scanner version // Define by user
        try {
            String content = Files.readString(Path.of(manifestPath));
            JSONObject manifest = new JSONObject(content);
            JSONArray versions = manifest.getJSONArray("versions");
            for (int i = 0; i < versions.length(); i++) {
                JSONObject version = versions.getJSONObject(i);
                String id = version.getString("id");
                if (id.equals(scVersion)) {
                    String url = version.getString("url");
                    URLUtils.setSelectedVersionURL(url);
                    System.out.println("Version URL set to >> " + url);
                    return;
                }
            }
            throw new LauncherException("Version not found");
        } catch (IOException e) {
            throw new LauncherException(e.getMessage());
        }
    }
}
