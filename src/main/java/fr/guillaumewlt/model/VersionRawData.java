package fr.guillaumewlt.model;

import org.json.JSONArray;
import org.json.JSONObject;

public record VersionRawData(
        JSONObject clientData,
        JSONArray librariesData
) {}
