package fr.guillaumewlt.parser;

import fr.guillaumewlt.model.LibraryInfos;
import fr.guillaumewlt.utils.LauncherUtils;
import fr.guillaumewlt.workflow.LauncherContext;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class LibrariesInfosParser {

    private JSONArray libraries;

    public LibrariesInfosParser(LauncherContext context) {
        libraries = context.getVersionRawData().librariesData();
    }

    public List<LibraryInfos> jsonParser() {
        List<LibraryInfos> librariesInfos = new ArrayList<>();

        for (int i = 0; i < libraries.length(); i++) {
            JSONObject library = libraries.getJSONObject(i);
            JSONObject artifact = library.getJSONObject("downloads").getJSONObject("artifact");

            String sha1 = artifact.getString("sha1");
            String path = artifact.getString("path");
            long size = artifact.getLong("size");
            String url = artifact.getString("url");

            String name = library.getString("name");

            boolean shouldAdd = true;

            if (library.has("rules")) {
                shouldAdd = false;
                JSONArray rules = library.getJSONArray("rules");

                for (int j = 0; j < rules.length(); j++) {
                    JSONObject rule = rules.getJSONObject(j);
                    String action = rule.getString("action");
                    if (rule.has("os")) {
                        String osName = rule.getJSONObject("os").getString("name");

                        if (action.equals("allow") && LauncherUtils.currentOs().equals(osName)) {
                            shouldAdd = true;
                            break;
                        }
                    } else if (action.equals("allow")) {
                        shouldAdd = true;
                        break;
                    }
                }
            }

            if (shouldAdd) {
                librariesInfos.add(new LibraryInfos(name, sha1, path, size, url));
            }
        }
        return librariesInfos;
    }
}
