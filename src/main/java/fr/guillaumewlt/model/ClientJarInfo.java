package fr.guillaumewlt.model;

// Hold the infos from the client Jar
public record ClientJarInfo(
        String url,
        String sha1,
        int size
) {}
