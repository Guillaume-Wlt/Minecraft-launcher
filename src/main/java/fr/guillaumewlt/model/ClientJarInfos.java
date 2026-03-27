package fr.guillaumewlt.model;

// Hold the infos from the client Jar
public record ClientJarInfos(
        String url,
        String sha1,
        int size
) {}
