package fr.guillaumewlt.models;

/**
 * Represents a file or directory entry from the JRE manifest.
 *
 * @param type       entry type: {@code "file"} or {@code "directory"}
 * @param path       relative path of the entry (e.g. {@code "bin/java.exe"})
 * @param sha1       SHA-1 hash of the raw file, or {@code null} for directories
 * @param size       size in bytes of the raw file, or {@code 0} for directories
 * @param url        download URL of the raw file, or {@code null} for directories
 * @param executable whether the file should be marked as executable
 */
public record JREFileInfos(
        String type,
        String path,
        String sha1,
        long size,
        String url,
        boolean executable
) {}
