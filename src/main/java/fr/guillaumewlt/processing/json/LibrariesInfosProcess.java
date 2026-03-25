package fr.guillaumewlt.processing.json;

import org.json.JSONArray;

public class LibrariesInfosProcess {

    private JSONArray libraries;

    public LibrariesInfosProcess(JSONArray libraries) {
        this.libraries = libraries;
    }

    public void processLibrariesInfos() {
        System.out.println("Libraries Infos :" + libraries.toString(2));

        // TODO Create Libraries Folder
    }
}
