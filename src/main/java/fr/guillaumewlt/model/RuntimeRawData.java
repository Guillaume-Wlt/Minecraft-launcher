package fr.guillaumewlt.model;

public record RuntimeInfos(
        String name,
        String sha1,
        long size,
        String url,
        String jreName,
        String jreReleaseDate
) {}
