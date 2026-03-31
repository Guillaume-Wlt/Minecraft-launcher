package fr.guillaumewlt.parser;

import fr.guillaumewlt.model.JREFileInfos;
import fr.guillaumewlt.workflow.LauncherContext;

import java.util.ArrayList;
import java.util.List;

public class JREManifestParser {

    private final LauncherContext context;

    public JREManifestParser(LauncherContext context) {
        this.context = context;
    }

    public List<JREFileInfos> jsonParser() {
        List<JREFileInfos> jreFilesInfos = new ArrayList<>();
//        try {
//            String
//        } catch (IOException e) {
//            throw new LauncherException("");
//        }
        return jreFilesInfos;
    }
}
