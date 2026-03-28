package fr.guillaumewlt.model;

/**
 * Holds the download information for the Minecraft client JAR file.
 * Set by {@link fr.guillaumewlt.parser.ClientJarInfosParser#jsonParser()},
 * which reads the {@code downloads.client} object from the version JSON file.
 *
 * @param url  The download URL for the client JAR file,
 *             read from the version JSON field {@code downloads.client.url}.
 * @param sha1 The SHA-1 hash of the client JAR file used for integrity verification,
 *             read from the version JSON field {@code downloads.client.sha1}.
 * @param size The size of the client JAR file in bytes,
 *             read from the version JSON field {@code downloads.client.size}.
 */
public record ClientJarInfos(
        String url,
        String sha1,
        long size
) {}
