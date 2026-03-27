package fr.guillaumewlt.model;

public record AssetsIndex(
        String id,
        String sha1,
        long size,
        long totalSize,
        String url
) {}
