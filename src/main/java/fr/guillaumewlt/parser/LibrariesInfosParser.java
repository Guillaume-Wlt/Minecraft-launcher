package fr.guillaumewlt.parser;

import org.json.JSONArray;

public class LibrariesInfosParser extends Parsers {

    private JSONArray libraries;

    public LibrariesInfosParser(JSONArray libraries) {
        this.libraries = libraries;
    }

    @Override
    protected void jsonParser() {
        System.out.println("Libraries Infos :" + libraries.toString(2));

        // TODO Create Libraries Folder
    }
}
