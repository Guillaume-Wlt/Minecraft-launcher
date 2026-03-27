package fr.guillaumewlt.model;

public record LibraryInfos(
        String name,
        String sha1,
        String path,
        long size,
        String url
) {
}
