package fr.guillaumewlt.parser;

import fr.guillaumewlt.exceptionhandler.LauncherException;
import fr.guillaumewlt.model.JREFileInfos;
import fr.guillaumewlt.workflow.LauncherContext;
import org.json.JSONObject;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class JREManifestParser {

    private final LauncherContext context;
    private final String jreManifestPath;

    public JREManifestParser(LauncherContext context) {
        this.context = context;
        String jreManifestName = context.getVersionRawData().javaVersion();
        jreManifestPath = context.getLauncherDirs().runtimeDir().path() + jreManifestName + "/" + jreManifestName + ".json"; // [...]/launcher/runtime/<jreName>/<jreName>.json
    }

    public List<JREFileInfos> jsonParser() {
        List<JREFileInfos> jreFilesInfos = new ArrayList<>();
        try {
            String content = Files.readString(Path.of(jreManifestPath));
            JSONObject jsonObj = new JSONObject(content);
            JSONObject files = jsonObj.getJSONObject("files");
            for (String key : files.keySet()) {
                JSONObject file = files.getJSONObject(key);
                if (file.getString("type").equals("directory")) {
                    jreFilesInfos.add(new JREFileInfos("directory", key, null, 0, null, false));
                } else {
                    JSONObject downloads = file.getJSONObject("downloads");

                    JSONObject raw = downloads.getJSONObject("raw"); // Get Raw files
                    String sha1 = raw.getString("sha1");
                    long size = raw.getLong("size");
                    String url = raw.getString("url");

                    boolean executable = file.optBoolean("executable", false);
                    String type = file.getString("type");

                    jreFilesInfos.add(new JREFileInfos(type, key, sha1, size, url, executable));
                }
            }
        } catch (IOException e) {
            throw new LauncherException("Error while parsing JSON file", e);
        }
        return jreFilesInfos;
    }
}
